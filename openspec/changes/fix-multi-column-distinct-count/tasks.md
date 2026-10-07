# Tasks: fix-multi-column-distinct-count

## 1. 判断逻辑（Java 侧）

- [x] 1.1 `MapperConstants` 新增 `MULTI_COLUMN_DISTINCT = "multiColumnDistinct"`
- [x] 1.2 判定改为**真实列数**：`QueryHelper` 新增 `countSelectColumns(entityClass, selectedProperties, ignoredProperties)`（列收集逻辑与 `toSelectColumnsExpression` 的 `List<String> columns` 同源，可抽私有方法复用）；`BaseDynamicQuery.toQueryParamMap()` 在计算完两个列表达式后 `paramMap.put(MULTI_COLUMN_DISTINCT, isDistinct() && count > 1)`，**该 put 置于 `putAll(customDynamicQueryParams)` 之后**——框架内部标志，`queryParam("multiColumnDistinct", ...)` 不可覆盖。按列数而非 `contains(",")` 判定的理由：逗号启发式会误判"单个含逗号且自身可为 NULL 的表达式"（实测 `COALESCE(a,b)` 场景内联 1 → 包裹 2，静默改变 NULL 语义）
- [x] 1.3 `DynamicQueryProvider` 新增静态谓词 `isMultiColumnDistinct(boolean distinct, String column)`（`distinct && column != null && column.contains(",")`——distinct 合取必须并入，防止未 distinct 多列被静默施加 DISTINCT 语义；property 路径列为裸串、无法可靠计数，启发式是记录在案的已知限制而非缺陷）

## 2. 统一模板渲染

- [x] 2.1 `DynamicQuerySqlHelper` 新增 `getCountColumnsClause(String multiColumnTest, String multiCols, String singleInner, String otherwiseInner)`：flat 三分支 `<choose>`，分支文本 = `COUNT(*) FROM ( SELECT DISTINCT {multiCols}` / `COUNT({singleInner})` / `COUNT({otherwiseInner})`（多列 when 在前）。**参数契约（M3）**：三个片段均为可直接内嵌模板的文本（自带 `${}` 占位与现状空格填充），不是裸 OGNL 名——query 版字面值：multiCols = `getSelectColumnsClause()`、singleInner = `"DISTINCT (" + getSelectUnAsColumnsClause() + ") "`、otherwiseInner = `" ${countKey} "`。javadoc 留 databaseId 方言扩展位 TODO
- [x] 2.2 `DynamicQuerySqlHelper` 新增 `getCountDistinctCloseClause(String multiColumnTest)`：产出 `<if test="...">) mdq_count</if>`，注释绑定与头 when 条件一致
- [x] 2.3 `DynamicQueryProvider.selectCount(Class)`（仅 query 版使用）改调 2.1，**同时把外层格式串从 `SELECT %s COUNT(%s) ` 改为 `SELECT %s %s`**（否则多列分支被外层 `)` 提前截断，见 design D4）：multiColumnTest 传 `dynamicQueryParams.multiColumnDistinct`，三片段按 2.1 的 query 版字面值传入（countKey 为构建期字面量：单 PK 列名或 `*`）
- [x] 2.4 `DynamicQueryProvider.selectCountByDynamicQuery`：WHERE 与 `${mdq_last_sql}` 之间插入 2.2 的闭括号片段
- [x] 2.5 `DynamicQueryProvider.selectCountPropertyByDynamicQuery`：模板加 `<bind name="multiColumnDistinct" value="@...DynamicQueryProvider@isMultiColumnDistinct(dynamicQueryParams.distinct, column)"/>`（置于既有 bind 之后，OGNL 可引用前者），改调 2.1（multiColumnTest 传 bind 变量；三片段按现状字节构造：multiCols = `" ${column} "`、singleInner = `"DISTINCT (${column} ) "`、otherwiseInner = `" ${column}  "`——与现状分支文本逐字节一致），模板末尾（WHERE 后）插入 2.2

## 3. 测试

- [x] 3.1 测试数据准备。注意跨库 schema 漂移：仅 h2 有 `student` 表与 `product.description` 列，mysql/postgresql/sqlserver 的 `product` 无可空 `description`；`users` 列名 h2 为 `user_name` 而其余库为 `username`。故用例要么只用四库共有的结构（`product.price` 可空 + `category_id`），要么按 profile 分支。数据优先在用例内插入并自行清理，不要改 `schema-*.sql` 的公共预置数据——测试无 `@Transactional`/`@Rollback`，共享同一个内存库且不隔离，新增预置行会波及既有断言（如 `DynamicMapperTest` 对 `id >= 20` 的计数断言）。NULL 组合（重复组合、一列 NULL、两列全 NULL）可用产品/分类组合构造
- [x] 3.2 谓词与判定单测（纯 JUnit，仿 `QueryHelperTest` 模式，无需 Spring）：① property 谓词 `(true, null)`/`(true, "a")`/`(true, "a, b")`/`(false, "a, b")` → `false`/`false`/`true`/`false`；② `QueryHelper.countSelectColumns` 对 User 实体：无 select → 3、`select(id)` → 1、`select(id, userName)` → 2、`ignore(id, userName)` → 1；③ `toQueryParamMap` 输出的 `multiColumnDistinct`：无 select + distinct → true、`select(a)` + distinct → false、非 distinct → false
- [x] 3.3 BoundSql 形状断言（`@SpringBootTest` 注入 `SqlSessionFactory`（DemoTest 已有先例），`configuration.getMappedStatement("<UserDao FQN>.selectCountByDynamicQuery")` 后 `getBoundSql(参数)` 渲染——注意参数须为含 `dynamicQuery`（property 版另含 `column`）键的 Map，不能直接传 `toQueryParamMap()` 结果）：多列 → 含 `COUNT(*) FROM ( SELECT DISTINCT` 且含 `) mdq_count`（**不得用 endsWith**——其后还有 `${mdq_last_sql}`）、子查询列带 AS 别名、last 在派生表外；未显式 select 的全列形态 → 同为子查询包裹；单列 → 含 `COUNT(DISTINCT (`；非 distinct → 与升级前逐字节一致；property 版多列/单列各一条；property 版未 distinct + `"a, b"` → 守护断言走 otherwise（不出现 SELECT DISTINCT）；**覆盖守护**：`queryParam("multiColumnDistinct", false)` 不影响多列判定
- [x] 3.4 H2 数值用例：多列 distinct count == `SELECT DISTINCT a, b` 列表行数（含 NULL 组合计一行）；未显式 select 的全列 distinct count 数值正确；View 实体同名列（`ProductView`，含 `description` 重名）多列 distinct count 不报 `Duplicate column name`；单列 distinct count 排除 NULL 行为与升级前一致
- [x] 3.5 NormPaging 用例（`NormPagingQuery` 与 `NormPagingQueryWrapper` 两个重载）：`select(a, b)` + `setDistinct(true)` + `calcTotal(true)` 不抛错，total/pages 正确
- [x] 3.6 property 版 `"a, b"` 数值用例（H2）
- [x] 3.7 跨库人工验收——**当前环境无法自动化**：pom 无 MySQL 驱动、无可用实例、docker 不可用，`application-mysql.properties` 指向内网 `192.168.8.190`，postgresql/sqlserver profile 亦指向内网 `169.254.240.190`。人工步骤：在真实 MySQL 上跑多列 / 全列 / 单列 / property 版四条用例（必须，确认无 ERROR 1241 且数值与 H2 一致）；PostgreSQL 与 SQL Server 建议同样跑一遍（子查询 + AS 别名为标准 SQL，风险低但零自动化覆盖）。结果记录到变更说明。（若后续补上驱动与 CI 数据库服务，此任务应升回自动化用例）
- [x] 3.8 默认 h2 profile 全量回归通过

## 4. 发布

- [x] 4.1 CHANGELOG：需先确认是否恢复维护——本文件最后更新于 2019（v2.0.12），而 3.2.29–3.2.36 的历次版本提升均未写入 CHANGELOG。若确定恢复，按既有风格在顶部新增条目（标题用 `**Bug fix**` 或与主流一致的 `**Bug**`，含版本号与日期链接），内容必须包含：① distinct count 列表达式多于一列时跨库修复（含未显式 select 的全列兜底形态）；② 渲染形态从行值内联变为子查询包裹（结果不变）；③ **property 版裸列串遇视图同名列从「H2/PG 可用」变为报 `Duplicate column name`——破坏性变化**，规避方式：改用 query 版（`select(a, b) + setDistinct(true)`）或为列串自行加别名；④ 派生表改用 AS 别名列、NULL 组合语义说明、单字段零变化；否则②③改为在 release notes / PR 描述中记录
- [x] 4.2 pom.xml 版本 3.2.36 → 3.2.37

## 实施注记（不改变任务状态，仅记录实施期修正）

任务 2.1/2.3/2.5 文本中的片段字面值基于"MyBatis choose 内空白被丢弃"的推导，实施期经探针实证修正（详见 design.md 的 Implementation Notes）：实际依赖 mybatis 3.5.6，其 `DynamicContext` 以 `StringJoiner(" ")` 连接根级节点输出，且 `SqlHelper.fromTable` 自带尾随空格。修正后的字面值以 `CountSqlShapeTest` 的精确断言为准，四个未触碰分支（query 单列 distinct / query 非 distinct / property 单列 / property 非 distinct）已对照升级前基线验证逐字节一致。
另：任务 4.1 ③ 的字面文本（「H2/PG 可用」）在 CHANGELOG 中按实际验证范围收窄为「H2 可用（实测）；PostgreSQL/SQL Server 未实测」，并已标注 SQL Server 行值构造器结论为 T-SQL 文法推断。
另：任务 4.1 的「在顶部新增条目」与该文件实际惯例不符（git 历史显示既有条目为升序、底部追加），v3.2.37 条目已按文件真实惯例追加至末尾（v2.0.12 之后），并随条目附停更说明。

## 任务 3.7 跨库人工验收结果（2026-09-24，docker 容器实测，已勾选）

环境：本机 docker（MySQL 5.7 / PostgreSQL 9.6 / SQL Server 2017 容器）。驱动侧按任务 3.7 预留的升回条件补齐 test 作用域依赖：新增 `mysql:mysql-connector-java:5.1.49`，将 `postgresql:postgresql:9.1-901.jdbc4`（无法实现 Hikari 所需的 `isValid()`）升级为 `org.postgresql:postgresql:42.2.29`。

- **MySQL 5.7（必须项，maven 自动化验收）**：27 个测试全绿（多列/全列/单列/property 四条数值用例 + NormPaging 两个重载 + 形状断言 + 纯单测），无 ERROR 1241，数值与 H2 一致。原始 bug 证据对：旧内联写法 `COUNT(DISTINCT (a, b))` 在该实例复现 `ERROR 1241 (21000): Operand should contain 1 column(s)`，新子查询写法正常返回。
- **PostgreSQL 9.6（建议项，maven 自动化验收）**：同套 27 个测试全绿，无异常。
- **SQL Server 2017（建议项，sqlcmd 直接验证四条形态）**：多列 query 版（AS 别名）= 5、全列 = 6、单列内联 = 3（NULL 排除）、property 版 = 5，全部正常；旧行值内联写法报 `Incorrect syntax near ','`——证实 SQL Server 不支持行值构造器（设计文档原先为文法推断，现升级为实测）。
- 视图同名列用例仅 h2 schema 有 `product.description` 列（跨库 schema 漂移，任务 3.1 已记录），故视图回归仍由 h2 覆盖；MySQL/PG/SQL Server 的验收不含该场景。

## 评审后修复注记（2026-09-24，外部 AI review 驱动）

- 3.7 验收后 CHANGELOG 首条"SQL Server 未实测"已与实际验收矛盾，已更新为"SQL Server 2017 容器实测复现 `Incorrect syntax near ','`"；破坏性变化条目同步明确"该场景 H2 实测、PG/MSSQL 未实测此场景"。openspec/config.yaml 环境边界已同步（驱动已补、docker 可用）。
- query 版多列分支别名改为唯一 `mdq_col_<i>`（详见 design.md 实施期补充），修复字段名蛇形化碰撞导致的派生表重复列名问题。
- 新增/修改的 main 注释按 config.yaml 规范转为英文、补齐 Javadoc、TODO 加 owner。
- mdq_col_N 别名修复后跨库复验（2026-09-24）：H2 全量 290 通过；MySQL 5.7 / PostgreSQL 9.6 maven 验收各 28 通过；SQL Server 2017 四条形态 sqlcmd 验证通过（多列/全列/单列/property），旧行值写法仍复现 `Incorrect syntax near ','`。视图同名列场景仍由 H2 覆盖（schema 漂移）。
- 2026-09-24 全量回归（290 个用例，非 H2 库）结论：MySQL 5.7 25 错 / PostgreSQL 9.6 38 错 / SQL Server 2017 45 错，**全部为既有跨库 schema 漂移或测试环境约束，本变更引入 0 个**。分类：① users.user_name 仅 H2 有（其余库为 username）——DynamicMapperTest/DbFilterTest 等；② product.description 仅 H2 有——View/Demo/DistinctCountH2Test 视图用例；③ student 表仅 H2 有——LogicPagingTest；④ PG/MSSQL schema 脚本缺 bug 表——DemoTest 批量用例；⑤ GroupedQueryProvider 的无列名派生表 `) AS a` 在 SQL Server 报 "No column name was specified"——既有问题，本次未触及；⑥ 本变更新增的 DistinctCountH2Test/NormPagingDistinctCountTest 夹具用显式主键 JDBC 插入，SQL Server IDENTITY 拒绝（`IDENTITY_INSERT OFF`）——测试写法约束，count SQL 本身已用 sqlcmd 四条形态验证。**无任何失败涉及新 count 代码**（无 mdq_ / COUNT(DISTINCT / selectCount 渲染错误）。存量用例跨库可跑性属历史欠账，不在本变更范围。
- 2026-09-24 收尾清理（第二条外部 review 驱动）：删除临时基线捕获测试 BaselineCaptureTest（无断言、仅 stdout，其信息已固化为 CountSqlShapeTest 的逐字节断言），套件计数 290 → 289；`DynamicQueryProvider.selectCount` 的 Javadoc 按 config.yaml 规范补齐 `@param entityClass` 与 `@return`（doclint 模式下原报两条警告，已消除）。
- 2026-09-25 补充边界/异常用例 16 个（套件 289 → 305）：判定边界（恰 2 列=多列下边界、select/ignore 优先级、重复 select 去重、0 列与未知属性、空表达式、逗号位置边界值）、异常路径（entityClass=null NPE、未知属性过滤器 PropertyNotFoundInternalException、渲染缺 dynamicQuery 参数 BuilderException）、H2 数值边界（空作用域=0、恰一行=1、全 NULL 组合坍缩=1 与单列全 NULL=0 对照、property 含/无逗号 CASE 表达式 3 vs 2 锁定披露限制）、NormPaging 越界页自动回退。全部通过。
- 2026-09-25 新增 16 个边界/异常用例后三库复验（夹具加 SQL Server IDENTITY_INSERT 方言分支，使同一套 maven 用例可跑四库）：MySQL 5.7 / PostgreSQL 9.6 / SQL Server 2017 均 45 例中 44 通过，唯一失败均为既有视图漂移用例（product.description 仅 H2 有）。过程中发现并披露 property 路径在 SQL Server 的新限制：派生表表达式列必须显式命名，裸串含不带别名的表达式（如 CASE/CONCAT）会报 `No column name was specified`，调用方需自行加别名——已更新测试（表达式自带别名）与 CHANGELOG 披露。
- 2026-09-25 第三轮外部 review（Codex 对抗评审）验证处置：①发现 3（边界夹具清理谓词恒假 `>=30000 AND <10000`）**属实并已修复**——改为有效离散区间 `[30000, 40000)`，并将 9 处夹具插入移入 try 块以覆盖部分插入失败场景；实测确认原泄漏被 10000 段测试的宽域预清理掩盖（顺序运行通过），但"各段自理"不变量确已破坏，修复后 305 全绿。②发现 1（public selectCount 返回不平衡片段）：事实成立，属 proposal/design 已披露的契约演进（旧"平衡"输出对多列本就是 MySQL 报错 SQL），配对警告 javadoc 已到位，不改。③发现 2（逗号嗅探误报）：全部事实成立且已在 CHANGELOG/design/spec/javadoc/测试五处披露并锁定；其"顶层逗号解析"建议技术上可行但与设计记录的取舍（D3：裸串无法可靠计数，保持简单启发式+披露）相悖，属设计决策待定，未实施。
- 2026-09-25 第四轮外部评审验证处置：lambda 重载误判（映射列表达式含逗号 → 类型化 API 静默变语义，design Non-Goal 假设"恒产单列名"被探针证伪）**属实并已修复**——property 判定改为顶层逗号解析，单表达式回归标量路径（升级前语义），CHANGELOG/spec/design 同步改写披露，CASE 用例期望翻转为 2/2。发现 1（selectCount 不平衡）维持上轮"已披露契约演进"结论。
- 顶层逗号解析修复后四库复验（2026-09-25）：H2 全量 306 通过；MySQL 5.7 / PostgreSQL 9.6 / SQL Server 2017 四类各 46 例均仅 1 错（既有视图漂移），翻转后的 property CASE 用例（单表达式标量语义 = 2）四库全部通过。
- 2026-09-25 判定机制定案（用户拍板引入 JSqlParser）：property 判定改语法树解析（shaded JSqlParser 2.0，内嵌产物 jar、reduced pom 零声明），手写状态机删除；过程实测暴露并修复了依赖版本冲突的活体案例（直接依赖 4.6 顶掉 PageHelper 5.1.10 所需 2.0 → 其 SQL Server 方言 NoSuchMethodError → 对齐 2.0 解决），最终四库复验：H2 306 全绿，MySQL/PG/MSSQL 各 46 例仅剩既有视图漂移。
- 2026-09-25 健壮性加固（复查发现项，实证后修复）：property 判定的 JSqlParser 为递归下降解析，实测列串嵌套约 2000 层括号即 `StackOverflowError` 且穿透原 catch（`Error` 不属 RuntimeException）——升级前的字符扫描器对任意输入都不崩溃，此为引入解析器带来的健壮性回归。修复：catch 并入 `StackOverflowError`（该调用无状态无资源持有，回退标量安全），探针验证 100–50000 全深度正常返回；H2 全量 306 保持全绿。
- 2026-09-25 第五轮外部评审验证处置：发现 1（shade 未限定 artifactSet → 5.1MB fat jar 内嵌全部 compile 依赖未重定位 + reduced pom 错删这些依赖 → 下游类影子化）**属实、critical、已修复**——artifactSet 限定仅内嵌 jsqlparser + 签名文件过滤 + reduced pom 保留其余 compile 依赖声明 + .gitignore 收录 dependency-reduced-pom.xml；复验：jar 5.1MB → 645KB、外来类计数归零、下游模拟（无 net.sf.jsqlparser 类路径调用判定）仍正确。发现 2（SQL Server 方言语法 TRY_CAST/AT TIME ZONE 解析失败回退标量）：实证 2.0 确实解析失败（4.6 可解析）——但回退渲染的是升级前行值形态，H2/PG 仍正确计数、MySQL/MSSQL 响亮报错，与升级前行为一致，**非回归而是覆盖缺口**；升级 pagehelper+jsqlparser 至 4.x 可补齐，属依赖策略决策待定。发现 3（selectCount 不平衡）：重复发现，维持"已披露契约演进"结论。
