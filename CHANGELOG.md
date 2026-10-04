# Change Log

## [v1.0.1](https://github.com/wz2cool/mybatis-dynamic-query/tree/v1.0.1) (2017-07-24)

**Feature:**
- merge "QueryColumn" annotation and "DbColumn" annotation to Column annotation.
- change "DbTable" annotation to "Table" annotation

## [v1.0.2](https://github.com/wz2cool/mybatis-dynamic-query/tree/v1.0.1) (2017-08-02)
**Feature**
- add bulk insert expression
- add delete expression
- add jdbcType and insertIgnore property in Column annotation
- add jdbcType when generating insert/update/delete expression enhancement
- add method to support lambda expression to get field name.

**Bug fix**
- [sql server] update will throw exception bug

## [v2.0.0](https://github.com/wz2cool/mybatis-dynamic-query/tree/v2.0.0) (2017-08-14)
**Feature**
- integrate tk.mybatis.mapper
- add DynamicQueryMapper
- remove generating insert/delete method since we can use DynamicQueryMapper.
- add CustomFilterDescriptor
- support serialize FilterDescriptor/FilterGroupDescriptor/CustomFilterDescriptor to json

## [v2.0.1](https://github.com/wz2cool/mybatis-dynamic-query/tree/v2.0.1) (2017-09-11)
**Feature**
- support serialize DynamicQuery to json

## [v2.0.2](https://github.com/wz2cool/mybatis-dynamic-query/tree/v2.0.2) (2017-10-05)
**Feature**
- add custom sort descriptor
- get query column ([tableName].[columnName])
- createInstance for MybatisQueryProvider 

## [v2.0.3](https://github.com/wz2cool/mybatis-dynamic-query/tree/v2.0.3) (2018-11-14)
**Feature**
- add Select Fields(columns)

## [v2.0.4](https://github.com/wz2cool/mybatis-dynamic-query/tree/v2.0.4) (2018-11-18)
**Feature**
- change select fields to select property

## [v2.0.5](https://github.com/wz2cool/mybatis-dynamic-query/tree/v2.0.5) (2018-11-18)
**bug**
- change "selectProperties" field to "selectedProperties"

## [v2.0.6](https://github.com/wz2cool/mybatis-dynamic-query/tree/v2.0.6) (2018-11-18)
**Feature**
- add MapUnderscoreToCamelCase to dynamic query

## [v2.0.7](https://github.com/wz2cool/mybatis-dynamic-query/tree/v2.0.7) (2019-04-12)
**Feature**
- remove MapUnderscoreToCamelCase from dynamic query
- read MapUnderscoreToCamelCase from config

## [v2.0.9](https://github.com/wz2cool/mybatis-dynamic-query/tree/v2.0.9) (2019-04-13)
**Bug**
- fix mapping issue

## [v2.0.10](https://github.com/wz2cool/mybatis-dynamic-query/tree/v2.0.10) (2019-05-10)
**Bug**
- change selectCount result type from long to int

## v2.0.11 (2019-05-10)
**Bug**
- fix sonar issue

## v2.0.12 (2019-05-22)
**feature**
- add InsertList support

> 注：CHANGELOG 自 2019 年 v2.0.12 起停更，3.2.29–3.2.36 的历次发版未写入，自 v3.2.37 起恢复维护。

## [v3.2.37](https://github.com/wz2cool/mybatis-dynamic-query/tree/v3.2.37) (2026-10-04)

**Bug fix**
- distinct count 的列表达式多于一个时（含未显式 `select()` 时兜底的全列形态），跨数据库修复：MySQL 报 `ERROR 1241 (Operand should contain 1 column(s))`（SQL Server 不支持行值构造器——已在 SQL Server 2017 容器实测复现 `Incorrect syntax near ','`）的问题，现在统一渲染为 `SELECT COUNT(*) FROM ( SELECT DISTINCT <cols> FROM <table> <where> ) mdq_count`（query 版 `select(a, b) + setDistinct(true)` 与 property 版 `selectCountPropertyByDynamicQuery("a, b", query)` 同等适用）。
- 渲染形态从行值内联 `COUNT(DISTINCT (a, b))` 变为上述子查询包裹，计数结果不变（多列组合视为相等，NULL 组合计一行，与 `SELECT DISTINCT` 列表查询严格一致）。
- query 版子查询内的列使用按列下标生成的唯一别名（`mdq_col_0` …），保证派生表列名唯一，View 实体同名列（如 `product.description` / `category.description`）不再报 `Duplicate column name`（property 版子查询使用裸列串，见下一条）。
- **破坏性变化**：property 版（`selectCountPropertyByDynamicQuery`）传入的裸列串遇到视图同名列时，从「H2 可用（实测）」变为报 `Duplicate column name`（该场景在 H2 实测报错；PostgreSQL/SQL Server 未实测此场景——PostgreSQL 的派生表允许同名列输出，大概率不受影响）。规避方式：改用 query 版（`select(a, b) + setDistinct(true)`，子查询自动带唯一别名），或为列串自行加别名。property 版的多列判定经 **内嵌 SQL 语法解析器**（JSqlParser，已 shade 重定位内嵌进本库 jar，不新增下游依赖、不产生版本冲突）确定：逗号位于括号或引号内的单个表达式（如 `COALESCE(a, b)`、`CONCAT(a, ',')`）维持标量 distinct，NULL 排除语义与升级前一致；仅当列串存在多个列时才走子查询包裹。注意 SQL Server 要求派生表列必须显式命名——多列列串中含**不带别名的表达式**时在该库会报 `No column name was specified`（纯列引用自动得名，不受影响），表达式请自行加别名。
- 单字段 distinct count 渲染与语义零变化（标量 distinct 排除 NULL 行为不变）；未启用 distinct 的 count 渲染零变化。
