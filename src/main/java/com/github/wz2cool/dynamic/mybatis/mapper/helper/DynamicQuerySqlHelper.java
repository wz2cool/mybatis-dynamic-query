package com.github.wz2cool.dynamic.mybatis.mapper.helper;

import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import tk.mybatis.mapper.entity.IDynamicTableName;
import tk.mybatis.mapper.util.StringUtil;

/**
 * @author Frank
 */
@SuppressWarnings("Duplicates")
public class DynamicQuerySqlHelper {

    private static final String FIRST_SQL = "${dynamicQueryParams.mdq_first_sql} ";
    private static final String LAST_SQL = " ${dynamicQueryParams.mdq_last_sql}";
    private static final String HINT_SQL = " ${dynamicQueryParams.mdq_hint_sql} ";

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
        return String.format("SELECT %s MAX(${%s})", getHintClause(), MapperConstants.COLUMN);
    }

    public static String getSelectMin() {
        return String.format("SELECT %s MIN(${%s})", getHintClause(), MapperConstants.COLUMN);
    }

    public static String getSelectSum() {
        return String.format("SELECT %s SUM(${%s})", getHintClause(), MapperConstants.COLUMN);
    }

    public static String getSelectAvg() {
        return String.format("SELECT %s AVG(${%s})", getHintClause(), MapperConstants.COLUMN);
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

    public static String getDynamicTableName(Class<?> entityClass, String tableName, String parameterName) {
        if (IDynamicTableName.class.isAssignableFrom(entityClass)) {
            if (StringUtil.isNotEmpty(parameterName)) {
                StringBuilder sql = new StringBuilder();
                sql.append("<choose>");
                sql.append("<when test=\"@tk.mybatis.mapper.util.OGNL@isDynamicParameter(" + parameterName + ") and " + parameterName + ".dynamicTableName != null and " + parameterName + ".dynamicTableName != ''\">");
                sql.append("${" + parameterName + ".dynamicTableName}");
                sql.append("</when>");
                sql.append("<otherwise>");
                sql.append(tableName);
                sql.append("</otherwise>");
                sql.append("</choose>");
                return sql.toString();
            } else {
                return getDynamicTableName(entityClass, tableName);
            }
        } else {
            return tableName;
        }
    }

    public static String updateTable(Class<?> entityClass, String defaultTableName, String entityName) {
        StringBuilder sql = new StringBuilder();
        sql.append(String.format("UPDATE %s ", getHintClause()));
        sql.append(getDynamicTableName(entityClass, defaultTableName, entityName));
        sql.append(" ");
        return sql.toString();
    }

    public static String deleteFromTable(Class<?> entityClass, String defaultTableName) {
        StringBuilder sql = new StringBuilder();
        sql.append(String.format("DELETE %s FROM ", getHintClause()));
        sql.append(getDynamicTableName(entityClass, defaultTableName));
        sql.append(" ");
        return sql.toString();
    }

    public static String getLastClause() {
        return LAST_SQL;
    }

    public static String getFirstClause() {
        return FIRST_SQL;
    }

    public static String getHintClause() {
        return HINT_SQL;
    }
}