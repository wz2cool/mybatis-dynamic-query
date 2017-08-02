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
