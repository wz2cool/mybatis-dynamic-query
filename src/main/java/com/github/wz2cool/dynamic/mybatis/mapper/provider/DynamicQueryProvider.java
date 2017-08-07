package com.github.wz2cool.dynamic.mybatis.mapper.provider;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Frank
 * \* Date: 8/7/2017
 * \* Time: 1:56 PM
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class DynamicQueryProvider extends MapperTemplate {
    public DynamicQueryProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String selectCountByDynamicQuery(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.selectCount(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(DynamicQuerySqlHelper.dynamicWhereClause());
        return sql.toString();
    }
}