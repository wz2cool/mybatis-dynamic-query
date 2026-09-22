# Tasks: fix-multi-column-distinct-count

## 1. 判断逻辑（Java 侧）

- [ ] 1.1 `MapperConstants` 新增 `MULTI_COLUMN_DISTINCT = "multiColumnDistinct"`
- [ ] 1.2 判定改为**真实列数**：`QueryHelper` 新增 `countSelectColumns(entityClass, selectedProperties, ignoredProperties)`（列收集逻辑与 `toSelectColumnsExpression` 的 `List<String> columns` 同源，可抽私有方法复用）；`BaseDynamicQuery.toQueryParamMap()` 在计算完两个列表达式后 `paramMap.put(MULTI_COLUMN_DISTINCT, isDistinct() && count > 1)`，**该 put 置于 `putAll(customDynamicQueryParams)` 之后**——框架内部标志，`queryParam("multiColumnDistinct", ...)` 不可覆盖。按列数而非 `contains(",")` 判定的理由：逗号启发式会误判"单个含逗号且自身可为 NULL 的表达式"（实测 `COALESCE(a,b)` 场景内联 1 → 包裹 2，静默改变 NULL 语义）
- [ ] 1.3 `DynamicQueryProvider` 新增静态谓词 `isMultiColumnDistinct(boolean distinct, String column)`（`distinct && column != null && column.contains(",")`——distinct 合取必须并入，防止未 distinct 多列被静默施加 DISTINCT 语义；property 路径列为裸串、无法可靠计数，启发式是记录在案的已知限制而非缺陷）

## 2. 统一模板渲染

- [ ] 2.1 `DynamicQuerySqlHelper` 新增 `getCountColumnsClause(String multiColumnTest, String multiCols, String singleInner, String otherwiseInner)`：flat 三分支 `<choose>`，分支文本 = `COUNT(*) FROM ( SELECT DISTINCT {multiCols}` / `COUNT({singleInner})` / `COUNT({otherwiseInner})`（多列 when 在前）。**参数契约（M3）**：三个片段均为可直接内嵌模板的文本（自带 `${}` 占位与现状空格填充），不是裸 OGNL 名——query 版字面值：multiCols = `getSelectColumnsClause()`、singleInner = `"DISTINCT (" + getSelectUnAsColumnsClause() + ") "`、otherwiseInner = `" ${countKey} "`。javadoc 留 databaseId 方言扩展位 TODO
- [ ] 2.2 `DynamicQuerySqlHelper` 新增 `getCountDistinctCloseClause(String multiColumnTest)`：产出 `<if test="...">) mdq_count</if>`，注释绑定与头 when 条件一致
- [ ] 2.3 `DynamicQueryProvider.selectCount(Class)`（仅 query 版使用）改调 2.1，**同时把外层格式串从 `SELECT %s COUNT(%s) ` 改为 `SELECT %s %s`**（否则多列分支被外层 `)` 提前截断，见 design D4）：multiColumnTest 传 `dynamicQueryParams.multiColumnDistinct`，三片段按 2.1 的 query 版字面值传入（countKey 为构建期字面量：单 PK 列名或 `*`）
- [ ] 2.4 `DynamicQueryProvider.selectCountByDynamicQuery`：WHERE 与 `${mdq_last_sql}` 之间插入 2.2 的闭括号片段
- [ ] 2.5 `DynamicQueryProvider.selectCountPropertyByDynamicQuery`：模板加 `<bind name="multiColumnDistinct" value="@...DynamicQueryProvider@isMultiColumnDistinct(dynamicQueryParams.distinct, column)"/>`（置于既有 bind 之后，OGNL 可引用前者），改调 2.1（multiColumnTest 传 bind 变量；三片段按现状字节构造：multiCols = `" ${column} "`、singleInner = `"DISTINCT (${column} ) "`、otherwiseInner = `" ${column}  "`——与现状分支文本逐字节一致），模板末尾（WHERE 后）插入 2.2

## 3. 测试

- [ ] 3.1 测试数据准备。注意跨库 schema 漂移：仅 h2 有 `student` 表与 `product.description` 列，mysql/postgresql/sqlserver 的 `product` 无可空 `description`；`users` 列名 h2 为 `user_name` 而其余库为 `username`。故用例要么只用四库共有的结构（`product.price` 可空 + `category_id`），要么按 profile 分支。数据优先在用例内插入并自行清理，不要改 `schema-*.sql` 的公共预置数据——测试无 `@Transactional`/`@Rollback`，共享同一个内存库且不隔离，新增预置行会波及既有断言（如 `DynamicMapperTest` 对 `id >= 20` 的计数断言）。NULL 组合（重复组合、一列 NULL、两列全 NULL）可用产品/分类组合构造
- [ ] 3.2 谓词与判定单测（纯 JUnit，仿 `QueryHelperTest` 模式，无需 Spring）：① property 谓词 `(true, null)`/`(true, "a")`/`(true, "a, b")`/`(false, "a, b")` → `false`/`false`/`true`/`false`；② `QueryHelper.countSelectColumns` 对 User 实体：无 select → 3、`select(id)` → 1、`select(id, userName)` → 2、`ignore(id, userName)` → 1；③ `toQueryParamMap` 输出的 `multiColumnDistinct`：无 select + distinct → true、`select(a)` + distinct → false、非 distinct → false
- [ ] 3.3 BoundSql 形状断言（`@SpringBootTest` 注入 `SqlSessionFactory`（DemoTest 已有先例），`configuration.getMappedStatement("<UserDao FQN>.selectCountByDynamicQuery")` 后 `getBoundSql(参数)` 渲染——注意参数须为含 `dynamicQuery`（property 版另含 `column`）键的 Map，不能直接传 `toQueryParamMap()` 结果）：多列 → 含 `COUNT(*) FROM ( SELECT DISTINCT` 且含 `) mdq_count`（**不得用 endsWith**——其后还有 `${mdq_last_sql}`）、子查询列带 AS 别名、last 在派生表外；未显式 select 的全列形态 → 同为子查询包裹；单列 → 含 `COUNT(DISTINCT (`；非 distinct → 与升级前逐字节一致；property 版多列/单列各一条；property 版未 distinct + `"a, b"` → 守护断言走 otherwise（不出现 SELECT DISTINCT）；**覆盖守护**：`queryParam("multiColumnDistinct", false)` 不影响多列判定
- [ ] 3.4 H2 数值用例：多列 distinct count == `SELECT DISTINCT a, b` 列表行数（含 NULL 组合计一行）；未显式 select 的全列 distinct count 数值正确；View 实体同名列（`ProductView`，含 `description` 重名）多列 distinct count 不报 `Duplicate column name`；单列 distinct count 排除 NULL 行为与升级前一致
- [ ] 3.5 NormPaging 用例（`NormPagingQuery` 与 `NormPagingQueryWrapper` 两个重载）：`select(a, b)` + `setDistinct(true)` + `calcTotal(true)` 不抛错，total/pages 正确
- [ ] 3.6 property 版 `"a, b"` 数值用例（H2）
- [ ] 3.7 跨库人工验收——**当前环境无法自动化**：pom 无 MySQL 驱动、无可用实例、docker 不可用，`application-mysql.properties` 指向内网 `192.168.8.190`，postgresql/sqlserver profile 亦指向内网 `169.254.240.190`。人工步骤：在真实 MySQL 上跑多列 / 全列 / 单列 / property 版四条用例（必须，确认无 ERROR 1241 且数值与 H2 一致）；PostgreSQL 与 SQL Server 建议同样跑一遍（子查询 + AS 别名为标准 SQL，风险低但零自动化覆盖）。结果记录到变更说明。（若后续补上驱动与 CI 数据库服务，此任务应升回自动化用例）
- [ ] 3.8 默认 h2 profile 全量回归通过

## 4. 发布

- [ ] 4.1 CHANGELOG：需先确认是否恢复维护——本文件最后更新于 2019（v2.0.12），而 3.2.29–3.2.36 的历次版本提升均未写入 CHANGELOG。若确定恢复，按既有风格在顶部新增条目（标题用 `**Bug fix**` 或与主流一致的 `**Bug**`，含版本号与日期链接），内容必须包含：① distinct count 列表达式多于一列时跨库修复（含未显式 select 的全列兜底形态）；② 渲染形态从行值内联变为子查询包裹（结果不变）；③ **property 版裸列串遇视图同名列从「H2/PG 可用」变为报 `Duplicate column name`——破坏性变化**，规避方式：改用 query 版（`select(a, b) + setDistinct(true)`）或为列串自行加别名；④ 派生表改用 AS 别名列、NULL 组合语义说明、单字段零变化；否则②③改为在 release notes / PR 描述中记录
- [ ] 4.2 pom.xml 版本 3.2.36 → 3.2.37
