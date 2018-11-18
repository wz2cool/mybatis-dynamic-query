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

## [v2.0.4](https://github.com/wz2cool/mybatis-dynamic-query/tree/v2.0.3) (2018-11-18)
**Feature**
- change select fields to select property