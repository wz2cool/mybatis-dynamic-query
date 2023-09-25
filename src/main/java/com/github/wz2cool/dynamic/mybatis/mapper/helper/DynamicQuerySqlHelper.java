package com.github.wz2cool.dynamic.mybatis.mapper.helper;

import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import tk.mybatis.mapper.entity.IDynamicTableName;

/**
 * @author Frank
 */
public class DynamicQuerySqlHelper {
    private DynamicQuerySqlHelper() {
        throw new UnsupportedOperationException();
    }

    public static String getBindFilterParams(boolean isMapUnderscoreToCamelCase) {
        StringBuilder sql = new StringBuilder();
        sql.append("<bind name=\"");
        sql.append(MapperConstants.DYNAMIC_QUERY_PARAMS).append("\" ");
        sql.append("value=\"");
        sql.append("@com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicQueryProvider");
        sql.append("@getDynamicQueryParamInternal(");
        sql.append(MapperConstants.DYNAMIC_QUERY).append(", ").append(isMapUnderscoreToCamelCase).append(")");
        sql.append("\"/>");
        return sql.toString();
    }

    public static String getUpdateBindFilterParams(boolean isMapUnderscoreToCamelCase) {
        StringBuilder sql = new StringBuilder();
        sql.append("<bind name=\"");
        sql.append(MapperConstants.DYNAMIC_QUERY_PARAMS).append("\" ");
        sql.append("value=\"");
        sql.append("@com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicQueryProvider");
        sql.append("@getUpdateQueryParamInternal(");
        sql.append(MapperConstants.DYNAMIC_QUERY).append(", ").append(isMapUnderscoreToCamelCase).append(")");
        sql.append("\"/>");
        return sql.toString();
    }

    public static String getSelectColumnsClause() {
        return String.format(" ${%s.%s} ", MapperConstants.DYNAMIC_QUERY_PARAMS, MapperConstants.SELECT_COLUMNS_EXPRESSION);
    }

    public static String getSelectUnAsColumnsClause() {
        return String.format(" ${%s.%s} ", MapperConstants.DYNAMIC_QUERY_PARAMS, MapperConstants.UN_AS_SELECT_COLUMNS_EXPRESSION);
    }

    public static String getWhereClause() {
        String newExpression = String.format("%s.%s", MapperConstants.DYNAMIC_QUERY_PARAMS, MapperConstants.WHERE_EXPRESSION);
        return String.format("<if test=\"%s != null and %s != ''\">WHERE ${%s}</if>",
                newExpression, newExpression, newExpression);
    }

    public static String getSortClause() {
        String newExpression = String.format("%s.%s", MapperConstants.DYNAMIC_QUERY_PARAMS, MapperConstants.SORT_EXPRESSION);
        return String.format("<if test=\"%s != null and %s != ''\">ORDER BY ${%s}</if>",
                newExpression, newExpression, newExpression);
    }

    public static String getSetClause() {
        String newExpression = String.format("%s.%s", MapperConstants.DYNAMIC_QUERY_PARAMS, MapperConstants.SET_EXPRESSION);
        return String.format("<if test=\"%s != null and %s != ''\">SET ${%s}</if>",
                newExpression, newExpression, newExpression);
    }

    public static String getSelectMax() {
        return String.format("SELECT MAX(${%s})", MapperConstants.COLUMN);
    }

    public static String getSelectMin() {
        return String.format("SELECT MIN(${%s})", MapperConstants.COLUMN);
    }

    public static String getSelectSum() {
        return String.format("SELECT SUM(${%s})", MapperConstants.COLUMN);
    }

    public static String getSelectAvg() {
        return String.format("SELECT AVG(${%s})", MapperConstants.COLUMN);
    }

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

    /**
     * 获取表名 - 支持动态表名
     *
     * @param entityClass
     * @param tableName
     * @return
     */
    public static String getDynamicTableName(Class<?> entityClass, String tableName) {
        if (IDynamicTableName.class.isAssignableFrom(entityClass)) {
            StringBuilder sql = new StringBuilder();
            sql.append("<choose>");
            sql.append("<when test=\"@tk.mybatis.mapper.util.OGNL@isDynamicParameter(_parameter) and dynamicTableName != null and dynamicTableName != ''\">");
            sql.append("${dynamicTableName}\n");
            sql.append("</when>");
            //不支持指定列的时候查询全部列
            sql.append("<otherwise>");
            sql.append(tableName);
            sql.append("</otherwise>");
            sql.append("</choose>");
            return sql.toString();
        } else {
            return tableName;
        }
    }
}