package com.github.wz2cool.dynamic.mybatis.mapper.helper;

import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;

/**
 * @author Frank
 */
public class GroupedQuerySqlHelper {
    private GroupedQuerySqlHelper() {
        throw new UnsupportedOperationException();
    }

    private static final String FIRST_SQL = String.format("${%s.mdq_first_sql} ", MapperConstants.GROUPED_QUERY_PARAMS);
    private static final String LAST_SQL = String.format(" ${%s.mdq_last_sql}", MapperConstants.GROUPED_QUERY_PARAMS);
    private static final String HINT_SQL = String.format(" ${%s.mdq_hint_sql} ", MapperConstants.GROUPED_QUERY_PARAMS);
    ;

    public static String getBindFilterParams(boolean isMapUnderscoreToCamelCase) {
        StringBuilder sql = new StringBuilder();
        sql.append("<bind name=\"");
        sql.append(MapperConstants.GROUPED_QUERY_PARAMS).append("\" ");
        sql.append("value=\"");
        sql.append("@com.github.wz2cool.dynamic.mybatis.mapper.provider.GroupedQueryProvider");
        sql.append("@getGroupedQueryParamInternal(");
        sql.append(MapperConstants.GROUPED_QUERY).append(", ").append(isMapUnderscoreToCamelCase).append(")");
        sql.append("\"/>");
        return sql.toString();
    }

    public static String getSelectColumnsClause() {
        return String.format(" ${%s.%s} ", MapperConstants.GROUPED_QUERY_PARAMS, MapperConstants.SELECT_COLUMNS_EXPRESSION);
    }

    public static String getWhereClause() {
        String newExpression = String.format("%s.%s", MapperConstants.GROUPED_QUERY_PARAMS, MapperConstants.WHERE_EXPRESSION);
        return String.format("<if test=\"%s != null and %s != ''\">WHERE ${%s}</if>",
                newExpression, newExpression, newExpression);
    }

    public static String getGroupByClause() {
        String newExpression = String.format("%s.%s", MapperConstants.GROUPED_QUERY_PARAMS, MapperConstants.GROUP_COLUMNS_EXPRESSION);
        return String.format("<if test=\"%s != null and %s != ''\">GROUP BY ${%s}</if>",
                newExpression, newExpression, newExpression);
    }

    public static String getHavingClause() {
        String newExpression = String.format("%s.%s", MapperConstants.GROUPED_QUERY_PARAMS, MapperConstants.HAVING_EXPRESSION);
        return String.format("<if test=\"%s != null and %s != ''\">HAVING ${%s}</if>",
                newExpression, newExpression, newExpression);
    }

    public static String getSortClause() {
        String newExpression = String.format("%s.%s", MapperConstants.GROUPED_QUERY_PARAMS, MapperConstants.SORT_EXPRESSION);
        return String.format("<if test=\"%s != null and %s != ''\">ORDER BY ${%s}</if>",
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