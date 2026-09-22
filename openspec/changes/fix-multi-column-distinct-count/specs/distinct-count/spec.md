# distinct-count Spec

## ADDED Requirements

### Requirement: 多列 DISTINCT 计数 SHALL 生成跨数据库可移植的子查询包裹 SQL
当查询启用 distinct 且选中列多于一个时，count 类方法（`selectCountByDynamicQuery`）SHALL 渲染为 `SELECT COUNT(*) FROM ( SELECT DISTINCT <cols> FROM <table> <where> ) mdq_count`（派生表别名必须存在），而非行值构造器内联形式 `COUNT(DISTINCT (a, b))`。子查询内的列 SHALL 使用带 AS 别名的列表达式（与列表查询同一子句），以保证派生表列名唯一。

#### Scenario: 多列 distinct count 在 H2 下数值正确
- **WHEN** 对含重复组合与 NULL 组合的数据执行 `DynamicQuery.select(a, b).setDistinct(true)` 后调用 `selectCountByDynamicQuery`
- **THEN** 返回值与 `SELECT DISTINCT a, b` 列表查询的行数严格相等（NULL 组合计一行）

#### Scenario: 多列 distinct count 在 MySQL 下不再报错
- **WHEN** 在真实 MySQL 上执行同一多列 distinct count（自动化环境无 MySQL，本场景为人工验收）
- **THEN** 不抛出 `ERROR 1241 (Operand should contain 1 column(s))`，且返回值与 H2 下一致

#### Scenario: 渲染 SQL 形状
- **WHEN** 通过 `MappedStatement.getBoundSql` 渲染多列 distinct count 的 SQL
- **THEN** SQL 包含 `COUNT(*) FROM ( SELECT DISTINCT` 且包含 `) mdq_count`（`${mdq_last_sql}` 位于其右，不得以 endsWith 断言）；子查询列带 AS 别名

#### Scenario: View 实体同名列不回归
- **WHEN** 对含同名列的 View 实体（如 `product.description` 与 `category.description`）启用 distinct 执行多列 count
- **THEN** 不抛 `Duplicate column name`（依赖带别名列表达式），计数正确

#### Scenario: 未显式 select 的全列 distinct count
- **WHEN** 不调用 `select()`、直接 `setDistinct(true)` 后执行 `selectCountByDynamicQuery`（列表达式兜底为全列拼接，多于单列）
- **THEN** SQL 同为子查询包裹形态，计数与 `SELECT DISTINCT` 全列列表查询行数一致

### Requirement: 单列 DISTINCT 计数 SHALL 维持内联标量形式
当查询启用 distinct 且选中列只有一个时，count SHALL 维持内联标量形式（与现状渲染逐字节一致，含逗号的单表达式列亦按单列处理），NULL 排除语义不得改变。

#### Scenario: 单列 distinct count 渲染形状不变
- **WHEN** 通过 BoundSql 渲染 `selectDistinct(x)`（或 `select(x)`）+ `setDistinct(true)` 的 count SQL
- **THEN** SQL 含 `COUNT(DISTINCT (` 内联形状，不含派生表

#### Scenario: 单列 NULL 语义不变
- **WHEN** 对含 NULL 值列执行单列 distinct count
- **THEN** 计数排除 NULL 行，与升级前行为一致

#### Scenario: 含逗号的单表达式列不误判
- **WHEN** 实体某列的列表达式自身含逗号（如 `CONCAT(a, b)`）且启用 distinct，在 query 路径（`select(x)`）执行 count
- **THEN** 按真实列数（1 列）判定为单列，维持内联形式，不进入子查询分支，计数语义与现状一致

### Requirement: 非 DISTINCT 计数 SHALL 与现状逐字节一致
未启用 distinct 时，count SHALL 维持既有渲染（主键列或 `*` 的 `COUNT(...)`），不得引入子查询包裹，且渲染结果与升级前逐字节一致（含行值构造器内联形式 `COUNT(DISTINCT (a, b))`——property 路径未 distinct 的多列列串仍渲染该形态，其执行报错属既有行为，不属本变更修复范围）。

#### Scenario: 普通 count 渲染等价
- **WHEN** 渲染未启用 distinct 的 count SQL
- **THEN** SQL 与既有版本逐字节一致，不含 `SELECT DISTINCT` 与派生表

### Requirement: property 版计数 SHALL 支持多列并保持单列行为
`selectCountPropertyByDynamicQuery` 的列为调用方传入的裸串（无结构信息），判定采用「列串含逗号」启发式：传入含逗号的列串且启用 distinct 时 SHALL 走子查询包裹；传入单列时 SHALL 维持内联形式；未启用 distinct 时 SHALL NOT 施加 DISTINCT 语义（该形态的执行报错属既有行为，不属本变更修复范围）。已知限制（升级前 H2 可用 → 升级后报错，见 proposal/design 披露）：列串含同名列（如视图的 `product.description, category.description`）时派生表列名冲突，调用方须自行加别名。

#### Scenario: property 版多列子查询包裹
- **WHEN** 传入 `"a, b"` 并启用 distinct 执行 `selectCountPropertyByDynamicQuery`
- **THEN** SQL 为 `SELECT COUNT(*) FROM ( SELECT DISTINCT a, b ... ) mdq_count`，计数正确

#### Scenario: property 版未 distinct 的多列列串不施加 DISTINCT
- **WHEN** 传入 `"a, b"` 但查询未启用 distinct
- **THEN** 渲染与升级前逐字节一致（otherwise 分支），SQL 中不出现 `SELECT DISTINCT` 子查询

#### Scenario: property 版单列维持内联
- **WHEN** 传入单列 `"a"` 并启用 distinct
- **THEN** SQL 含 `COUNT(DISTINCT (` 内联形状，与升级前逐字节一致，不含派生表

### Requirement: 分页 calcTotal SHALL 复用同一计数路径
`selectByNormalPaging` 在 `calcTotal` 启用时 SHALL 通过同一 count 模板计算总数，多列 distinct 分页不报错且 total 正确（`NormPagingQuery` 与 `NormPagingQueryWrapper` 两个重载同等适用）。

#### Scenario: NormPaging 多列 distinct calcTotal
- **WHEN** 分别以 `NormPagingQuery`（`select(a, b)` + `setDistinct(true)` + `calcTotal(true)`）与 `NormPagingQueryWrapper`（`create(query)`，query 带同样的 distinct 设置）执行分页
- **THEN** 均不抛异常，`total` 等于去重列表行数，`pages` 计算正确
