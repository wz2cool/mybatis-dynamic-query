package com.github.wz2cool.dynamic.mybatis.mapper.helper;

import tk.mybatis.mapper.mapperhelper.SqlHelper;

public class EnhancedSqlHelper extends SqlHelper {

    /**
     * insert ignore into tableName - 动态表名
     *
     * @param entityClass
     * @param defaultTableName
     * @return
     */
    public static String insertIgnoreIntoTable(Class<?> entityClass, String defaultTableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT IGNORE INTO ");
        sql.append(getDynamicTableName(entityClass, defaultTableName));
        sql.append(" ");
        return sql.toString();
    }
}
