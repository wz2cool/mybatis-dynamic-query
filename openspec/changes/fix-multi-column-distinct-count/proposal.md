# Proposal: fix-multi-column-distinct-count

## Why

DISTINCT count 在列表达式多于一列时生成的 SQL 为 `COUNT(DISTINCT (a, b))`——这是行值构造器写法，只有 H2/PostgreSQL 接受；MySQL 报 `ERROR 1241 (21000): Operand should contain 1 column(s)`，SQL Server 亦不支持行值构造器（T-SQL 文法推断，未实测）。触发不限于显式多字段 select：未调用 `select()` 时列表达式兜底为全列逗号拼接（`QueryHelper.toSelectColumnsExpression`），任何 `setDistinct(true)` + count 在上述数据库都会触发。默认测试 profile 是 H2（恰好接受该写法，且其 `MODE=MySQL` 同样接受），且没有任何多列 distinct + count 用例，导致 bug 潜伏至今。受影响链路：`selectCountByDynamicQuery`、`selectCountPropertyByDynamicQuery`、以及 `selectByNormalPaging` 的 calcTotal（内部复用前者）。

## What Changes

- 多列 distinct count 的 SQL 渲染改为子查询包裹：`SELECT COUNT(*) FROM ( SELECT DISTINCT a, b FROM t WHERE ... ) mdq_count`，且子查询内使用带 AS 别名的列子句（与列表查询同一子句）——派生表要求列名唯一，View 实体的同名列（如 `product.description` / `category.description`）用无别名形式会报 `Duplicate column name`（已实测）。该形态是唯一同时满足 h2/mysql/postgresql/sqlserver 的形态（内联写法 MySQL 与 H2 互斥），且语义与 SELECT DISTINCT 列表查询严格一致（count == 列表行数，含 NULL 组合计一行）。
- 单列 distinct count 维持内联 `COUNT(DISTINCT (x))` 不变——标量 distinct 排除 NULL，包裹成子查询会静默改变 NULL 语义，且单列括号写法本就全库可移植。
- 非 distinct count 维持简单计数（主键列或 `*`），渲染路径等价。
- query 路径：`BaseDynamicQuery.toQueryParamMap()` 新增 `multiColumnDistinct` 布尔参数，判定用**真实列数**（`isDistinct() && 列数 > 1`，QueryHelper 新增列数统计）——不做"表达式含逗号"启发式，从根上消除误判：单个含逗号且自身可为 NULL 的表达式（如 `COALESCE(a, b)`）若被误判为多列而包裹，NULL 语义会由排除变为计一行（实测 1 → 2）。
- property 路径（`selectCountPropertyByDynamicQuery`，column 为独立 `@Param` 裸串、无结构信息）：判定保留「列串含逗号」启发式，模板新增 `<bind>` 调用 `DynamicQueryProvider.isMultiColumnDistinct(distinct, column)` 静态谓词（含 distinct 合取，防止未 distinct 的多列列串被静默施加 DISTINCT 语义），判断逻辑收进 Java，避免模板内 OGNL 单引号字符字面量陷阱。**已知限制**：单个含逗号且自身可为 NULL 的表达式会被包裹，NULL 语义随之变化（随 release notes 披露）。
- count 列子句模板去重收敛：`DynamicQuerySqlHelper` 新增两个模板构建方法（三分支 count 列子句 choose、派生表闭括号 if），消灭 `DynamicQueryProvider` 两处重复的 `DISTINCT (%s)` 手写 format（该类为此挂着 `@SuppressWarnings("Duplicates")`，本次消除其中一处成因）。
- NormPaging calcTotal 复用同一模板，自动修复。
- CHANGELOG / release notes 记录语义变化（注意：CHANGELOG 最后更新于 2019 年 v2.0.12，3.2.x 历次发版均未写入，需先确认是否恢复维护）。
- 预留扩展位（仅注释）：将来配置了 MyBatis databaseId 的应用可按方言切回原生内联（mysql 无括号 / h2、pg 带括号），默认不启用。

## Capabilities

### New Capabilities

- `distinct-count`: DISTINCT 计数的 SQL 生成行为——多列走可移植的子查询包裹（子查询列带 AS 别名，保证派生表列名唯一），单列维持内联，非 distinct 走简单计数；四种数据库（h2/mysql/postgresql/sqlserver）下形态合法，多列计数与 SELECT DISTINCT 列表查询行数严格一致（验证边界：自动化仅覆盖 H2，其余库为形态推断 + 人工验收）。

### Modified Capabilities

（无——现有 openspec/specs/ 为空，本次为首次能力定义）

## Impact

- **代码**：`MapperConstants`（+1 常量）、`QueryHelper`（+1 列数统计方法）、`BaseDynamicQuery.toQueryParamMap()`（+1 行参数计算）、`DynamicQuerySqlHelper`（+2 模板构建方法）、`DynamicQueryProvider`（2 个方法改调统一 helper + 1 个静态谓词）。无 mapper 接口签名变化，无新依赖；注意 `DynamicQueryProvider.selectCount(Class)` 是 **public static** 方法（对外可见），本次改变其渲染行为、签名不变。
- **行为语义**：多列 distinct count 在 MySQL/SQL Server 等不支持行值构造器的库从报错变正常；PG/H2 用户渲染形态从内联变子查询，结果一致（NULL 组合计一行，实测）。单列路径与非 distinct 路径渲染**逐字节**零变化。**View 实体同名列场景**：query 版经子查询的 AS 别名列修复（该带别名列子句为列表查询既有物，非新增）；**property 版裸列串遇同名列从「H2/PG 可用」变为报 `Duplicate column name`——破坏性变化**，规避方式与披露要求见 design Migration / tasks 4.1。
- **测试**：新增 BoundSql 形状断言（仓库首个 SQL 形状测试手法）、H2 数值/一致性用例（含 NULL 组合、未显式 select 的全列形态、View 同名列）、NormPaging calcTotal 用例（两个重载）、property 版 `"a, b"` 用例（含未 distinct 的守护断言）、判定/谓词纯单测（`QueryHelperTest` 模式）。**MySQL/PG/SQL Server 侧无法自动化**（pom 无 MySQL 驱动、无实例、docker 不可用），降为人工验收项（tasks 3.7）。
- **环境验证边界**：H2 结论为实测（含 `MODE=MySQL`，该模式行为与 MySQL 不同，无法替代 MySQL 验证）；MySQL ERROR 1241 来自用户报告与四库文法文档，SQL Server 结论为 T-SQL 文法推断，均未在本机复现。
- **用户**：除上列 property 版同名列场景外，零改动零配置，升级即修复。
