# Design: fix-multi-column-distinct-count

## Context

count SQL 由 `@SelectProvider` 启动期生成模板、运行期渲染参数。`DynamicQueryProvider.selectCount()`（69-76 行）与 `selectCountPropertyByDynamicQuery()`（55-67 行）两处手写了 `DISTINCT (%s)` 格式串，多列时渲染成行值构造器 `COUNT(DISTINCT (a, b))`——只有 H2/PG 接受；MySQL 报 ERROR 1241，SQL Server 亦不支持行值构造器（T-SQL 文法推断，未实测）。触发不限于显式多字段 select：未调用 `select()` 时列表达式兜底为全列逗号拼接（`QueryHelper.toSelectColumnsExpression` 空参时 `needSelectColumn = true`），任何 distinct + count 在上述数据库都会触发。

**实测范围（务必区分已验证与未验证）**：本机以 H2 2.0.206 实测（含 `MODE=MySQL`）——`COUNT(DISTINCT (a, b))` 通过（渲染为 `ROW(A, B)`），`COUNT(DISTINCT a, b)`（MySQL 扩展）语法错；`MODE=MySQL` 同样接受行值形态，故 H2 无法替代 MySQL 验证。MySQL 报 ERROR 1241 的结论来自用户 bug 报告与四库文法文档（H2 官方文法 COUNT 只收单 expression），**本机未实测 MySQL**（无驱动、无实例、docker 不可用）。

**派生表约束（实测发现，决定实现细节）**：内联行值形式不需要给列起名，而派生表要求列名唯一。`ProductView` 存在 `product.description` 与 `category.description` 两个同名列，实测：内联形态通过、子查询 + 无别名列（`unAsSelectColumnsExpression`）报 `Duplicate column name "DESCRIPTION"`、子查询 + AS 别名列（`selectColumnsExpression`）通过。故子查询分支必须用 AS 别名形式（见 D4），否则会把 View 实体上"今天能跑"的场景变成回归。

约束：默认测试 profile 是 H2，任何修法必须让 H2 CI 保持绿；count 模板末尾有 `${mdq_last_sql}`（property 版没有）；库内已有子查询先例（`GroupedQueryProvider.selectCountByGroupedQuery` 的 `) AS a`，且位于 getLastClause 之前）。

## Goals / Non-Goals

**Goals:**

- 多列 distinct count 在 h2/mysql/postgresql/sqlserver 四库生成正确且语义一致的 SQL（四库共有的 SQL 标准子集，验证边界见 Context）
- 计数结果与 `SELECT DISTINCT` 列表查询行数严格一致（含 NULL 组合计一行）
- 消灭重复的 count 模板格式串，判断逻辑收进 Java
- 单列与非 distinct 路径渲染**逐字节**零变化
- 用户零改动零配置，升级即修复

**Non-Goals:**

- 不引入 SQL 解析器或方言抽象（databaseId 扩展仅留注释）
- 不新增多字段 `selectDistinct(a, b)` 便捷 API（`select(a,b) + setDistinct(true)` 已覆盖，留作后续功能）
- 不处理空 select 列表的病态输入：实体无可选列（或 select 指定项全不存在）时列数为 0，判定为非多列、走单列分支，渲染 `COUNT(DISTINCT (  ) )` 的语法错与升级前一致——既有行为，本次不修
- 列表查询（`selectByDynamicQuery`）的 distinct 渲染是独立模板片段（`<if distinct>distinct</if>` + 列子句，本身可移植），本次不改动
- lambda 版 `selectCountPropertyByDynamicQueryInternal` 经 `getQueryColumnByProperty` 恒产单列名，永不触发多列分支，不为其加判定
- 不改动 GroupedQuery 计数（其已是无条件子查询包裹）

## Decisions

### D1: 多列用 PageHelper 式子查询包裹，而非内联变体

生成 `SELECT COUNT(*) FROM ( SELECT DISTINCT a, b FROM t WHERE ... ) mdq_count`。

- 备选"去括号学 MyBatis-Plus 透传"：H2/PG 直接语法错，默认测试库 CI 变红。否决。
- 备选"jOOQ 式方言感知渲染"：库无 dialect 概念，databaseId 多数应用未配置，配置侵入大。否决（留注释扩展位）。
- 备选"CONCAT 拼接模拟"：分隔符碰撞 + NULL 语义脏。否决。
- 理由：内联形式互斥（Context），子查询是唯一四库通用形态；语义与列表查询严格一致；库内已有先例。
- **子查询内的列必须用 AS 别名形式**（`selectColumnsExpression`，即列表查询所用的同一子句），不能用 `unAsSelectColumnsExpression`：派生表要求列名唯一，View 实体存在 `product.description` / `category.description` 等同名列，无别名会报 `Duplicate column name`（实测，见 Context）。带别名后子查询的选择列表与列表查询逐字相同，语义一致性论证更强；别名取字段名（或驼峰转下划线），字段名唯一故别名唯一。property 路径的列是用户传入的裸串，无别名可用，视图场景的同名列由调用方自行规避。

### D2: 单列维持内联 `COUNT(DISTINCT ( x ))`，不统一包裹

`(a)` 是括号化标量（四库通用，这就是单列从未踩雷的原因），`(a, b)` 是行值构造器。关键在 NULL 语义：标量 `COUNT(DISTINCT x)` 排除 NULL，子查询包裹后 NULL 组合计一行——单列包裹是静默改变存量结果。多列行值形式对 NULL 的处理（组合视为相等）恰与 `SELECT DISTINCT` 一致，故包裹对多列语义无损。

### D3: 判断机制分层——按"条件"命名，判断放 Java，模板变哑

- query 路径：按**真实列数**判定，不用 `contains(",")`。`toQueryParamMap()` 调 `QUERY_HELPER.countSelectColumns(entityClass, selectedProperties, ignoredProperties)`（QueryHelper 新增公共方法，列收集逻辑与 `toSelectColumnsExpression` 同源），`paramMap.put(MULTI_COLUMN_DISTINCT, isDistinct() && count > 1)`。按列数判定的理由：逗号启发式存在真实误判——单个含逗号且自身可为 NULL 的表达式（如 `COALESCE(a, b)`）会被误判为多列而包裹，NULL 语义由「排除」静默变为「组合计一行」（实测 `COALESCE` 场景 1 → 2，与 D2 否决单列包裹的理由同源）；列数判定从根上消灭这类误判，也使「选中列多于一个」的字面定义与实现严格一致。放 param map 而非 bind：它是查询参数的天然载体；且该 key 属框架内部标志——put 置于 `putAll(customDynamicQueryParams)` **之后**，`queryParam("multiColumnDistinct", ...)` 不可覆盖（守护用例锁定）。参数按条件命名（`multiColumnDistinct`）而非机制命名（如 `countAsSubQuery`）：将来 dialect 扩展时同条件可渲染不同形态，名字不撒谎。
- property 路径：column 是独立 `@Param`（用户裸串，无结构信息、无法可靠计数），故保留「列串含逗号」启发式：模板新增 `<bind name="multiColumnDistinct" value="@...DynamicQueryProvider@isMultiColumnDistinct(dynamicQueryParams.distinct, column)"/>`（bind 在 dynamicQueryParams 之后求值，OGNL 可引用），谓词 `distinct && column != null && column.contains(",")` 一行可单测。**distinct 合取必须并入判定**：谓词若只判列形状，"未 distinct + 多列列串"的 property count 会从既有报错（`COUNT( a, b )` 语法错）静默变为 distinct 子查询计数，无请求地施加 DISTINCT 语义。`<bind>` 调静态方法是库内既有模式；同时消灭 OGNL 单引号字符字面量（`,` 在 OGNL 中是 Character，`String.contains` 收 CharSequence，隐式转换不可靠）陷阱。**启发式已知限制**（随 release notes 披露）：单个含逗号且自身可为 NULL 的表达式会被包裹，NULL 语义由排除变为计一行（实测 `COALESCE` 1 → 2）；视图同名列同理需调用方自行加别名。至此两路判定的**可观察行为**统一：distinct 且（真实）多列 → 子查询。
- 参照 PageHelper 哲学：能安全简化就简化（otherwise 简单计数、单列内联），有疑虑就保守包裹（多列、GroupedQuery）。不引入其 SQL 解析器——我们的 SQL 是自产的，结构生成时已知。

### D4: 模板为 flat 三分支 `<choose>`，外层格式串同步调整，未触碰分支逐字节不变

**外层格式串必须同步改**：`selectCount()` 现为 `String.format("SELECT %s COUNT(%s) ", ...)`——`COUNT(` 与尾部 `)` 在 choose 外面。若保留，多列分支会被外层 `)` 提前截断（渲染成 `...SELECT DISTINCT cols) FROM t`，派生表在 FROM/WHERE 之前就被关掉）。故外层改为 `SELECT %s %s`，`COUNT(` 与闭合 `)` 全部移入分支文本：

```
<choose>
  <when test="{multiColumnTest}">COUNT(*) FROM ( SELECT DISTINCT ${multiCols}</when>
  <when test="dynamicQueryParams.distinct">COUNT(DISTINCT (${unAsCols}) )</when>
  <otherwise>COUNT(${otherwiseCols})</otherwise>
</choose>
```

渲染契约：**单列与 otherwise 分支 SHALL 与现状逐字节一致**。现状字节（推导自格式串 + MyBatis 3.4.6 `ChooseHandler` 走 `XNode.getChildren()`、choose 内空白被丢弃）：单列 distinct = `COUNT(DISTINCT ( V ) )`、query otherwise = `COUNT( PK )`、property otherwise = `COUNT( V  )`（双空格）。实现要点：单列分支 = `COUNT(DISTINCT (` + `getSelectUnAsColumnsClause()`（自带前后空格）+ `) )`；otherwise 分支 = `COUNT(` + 现状 otherwise 片段 + `)`（query 传 countKey 字面量、property 传现状的 `${column} ` 片段，其尾随空格保留）。多列分支为新增形态，无字节契约；`${multiCols}` 用 AS 别名列（`getSelectColumnsClause()`，见 D1 末条）。`multiColumnTest` 两路取值：query 版 `dynamicQueryParams.multiColumnDistinct`，property 版 bind 变量 `multiColumnDistinct`——多列 when 排在 distinct when 之前（条件蕴含）。otherwise 的 countKey 是**构建期 Java 字面量**（主键列名或 `*`）；property 版 otherwise 是运行期 `${column}`。拒绝"保留外层 `COUNT(` 包装、多列分支输出 `*) FROM (`"的写法——外层 `)` 会提前关掉派生表，`FROM t` 落到子查询之外。

注意 `getCountColumnsClause` 需要**三个**片段参数（multiCols / singleInner / otherwiseInner，均为可直接内嵌的模板片段——含 `${}` 占位与现状空格填充，契约见 tasks 2.1）；property 路径三处都由 `${column}` 按现状填充构造；头 when 与尾 if 由同一 `multiColumnTest` 字符串驱动。

### D5: 闭括号置于 `${mdq_last_sql}` 之前

与 GroupedQuery 先例（`) AS a` 在 getLastClause 之前）同构；保持 last() 语义"追加到最终 SQL 末尾"不变——若放 last 之后，`last("LIMIT 5")` 会落进派生表，把总数变成"去重后前 5 行计数"。

### D6: 统一 helper 归属——片段进 SqlHelper，谓词进 Provider

`DynamicQuerySqlHelper` 新增 `getCountColumnsClause(multiColumnTest, multiCols, singleInner, otherwiseInner)`（产三分支 choose）与 `getCountDistinctCloseClause(multiColumnTest)`（产尾部 `<if>...`) mdq_count</if>`），两个 Provider 改调它们；谓词 `isMultiColumnDistinct(boolean distinct, String column)` 作 `DynamicQueryProvider` 静态方法（bind 目标的既有归属）。**片段参数契约（M3）**：三个片段均为"可直接内嵌进模板的文本"，自带 `${}` 占位与现状空格填充——query 版字面值：multiCols = `getSelectColumnsClause()`（` ${dynamicQueryParams.selectColumnsExpression} `）、singleInner = `"DISTINCT (" + getSelectUnAsColumnsClause() + ") "`、otherwiseInner = `" ${countKey} "`；property 版三者均由 `${column}` 按现状填充构造（`" ${column} "` / `"DISTINCT (${column} ) "` / `" ${column}  "`）。派生表别名 `mdq_count`（MySQL 无别名报 ERROR 1248；库内既有派生表别名先例是 GroupedQuery 的 `AS a`）。count 路径本无 sort 子句，SQL Server 派生表禁 ORDER BY 限制不触发；hint/first 位置与现状等价。

## Risks / Trade-offs

- [DISTINCT 派生表不可被 derived_merge 合并，多一次去重结果物化] → 该路径修复前是报错路径，多为报表类低频查询；`getCountColumnsClause` javadoc 留 databaseId 方言扩展位 TODO（mysql 无括号内联 / h2、pg 带括号内联），默认不启用。
- [头 when 与尾 if 条件字符串重复，可能改一处漏一处] → 两者由同一 helper 的同一参数生成；模板注释绑定提醒。
- [PG/H2 用户渲染形态变化] → 结果经 NULL 组合验证严格一致（多列内联 ≡ 子查询，实测含 NULL 组合），CHANGELOG 说明；未触碰分支逐字节不变（见 D4）。
- [property 路径逗号启发式的已知限制] → 单个含逗号且自身可为 NULL 的表达式被包裹后 NULL 语义由排除变为计一行（实测 `COALESCE` 1 → 2）；该路径列为调用方裸串、无法可靠计数，已在 proposal/release notes 披露；query 路径已用列数判定消除此类问题。
- [`customDynamicQueryParams` 覆盖 flag 的 footgun] → flag put 置于 `putAll(customDynamicQueryParams)` 之后，用户 `queryParam("multiColumnDistinct", ...)` 不生效（框架内部标志），守护用例锁定。
- [View 实体同名列在派生表内报 Duplicate column name] → 子查询分支改用 AS 别名列（见 D1 末条），别名由唯一字段名派生；需补 View 实体上的回归用例（tasks 3.4）。
- [MyBatis `<bind>` OGNL 静态调用对 `@Param` 变量的可见性] → 与现有 `getDynamicQueryParamInternal(dynamicQuery, ...)` 同机制；BoundSql 形状用例兜底验证。
- [mysql / postgresql / sqlserver profile 均无法在本机/CI 闭环] → pom 无 MySQL 驱动、无 docker，三个 profile 指向内网地址（192.168.8.190 / 169.254.240.190）；tasks 3.7 降级为人工验收（MySQL 必做，PG/SQL Server 建议做），自动化覆盖以 H2 形状/数值断言为准；若后续 CI 补数据库服务再升回自动化。

## Migration Plan

升级即生效，无配置、无数据迁移、无 API 签名变化（`DynamicQueryProvider.selectCount(Class)` 为 public static，渲染行为变化对外可见、签名不变）；回滚即回退版本。**release notes 必须披露两项行为变化**：① 多列 distinct count 渲染形态从行值内联变为子查询（计数结果不变）；② **property 路径（`selectCountPropertyByDynamicQuery` 裸列串）遇视图同名列从「H2/PG 可用」变为「Duplicate column name 报错」**——规避方式：改用 query 版（`select(a, b) + setDistinct(true)`，子查询自动带 AS 别名）或为列串自行加别名。单字段路径渲染与语义零变化。

## Open Questions

1. CI 是否补 MySQL 服务（决定 tasks 3.7 能否从人工验收升回自动化）——当前默认：人工验收，H2 形状/数值断言作为自动化底线。
2. CHANGELOG 是否恢复维护（本文件自 2019 年 v2.0.12 起停更，3.2.29–3.2.36 历次发版均未写入）——当前默认：恢复，新增 v3.2.37 条目并注明历史断层；若决定不恢复，tasks 4.1 改为 release notes / PR 描述记录。

其余（命名、helper 归属、单列策略、模板形状、databaseId 扩展位）已在探索阶段收敛。
