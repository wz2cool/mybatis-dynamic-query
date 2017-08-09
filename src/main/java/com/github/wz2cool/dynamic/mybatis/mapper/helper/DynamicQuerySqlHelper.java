package com.github.wz2cool.dynamic.mybatis.mapper.helper;

import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperContant;

public class DynamicQuerySqlHelper {
    public static String getBindFilterParams() {
        StringBuilder sql = new StringBuilder();
        sql.append("<bind name=\"");
        sql.append(MapperContant.FILTER_PARAMS).append("\" ");
        sql.append("value=\"");
        sql.append("@com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicQueryProvider");
        sql.append("@getWhereQueryParamInternal(");
        sql.append(MapperContant.ENTITY_CLASS).append(",");
        sql.append(MapperContant.FILTERS).append(")");
        sql.append("\"/>");
        return sql.toString();
    }

    public static String getWhereClause() {
        String newExpression = String.format("%s.%s", MapperContant.FILTER_PARAMS, MapperContant.WHERE_EXPRESSION);
        return String.format("<if test=\"%s != null and %s != ''\">WHERE ${%s}</if>",
                newExpression, newExpression, newExpression);
    }

    public static String getBindSortParms() {
        StringBuilder sql = new StringBuilder();
        sql.append("<bind name=\"");
        sql.append(MapperContant.SORT_PARAMS).append("\" ");
        sql.append("value=\"");
        sql.append("@com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicQueryProvider");
        sql.append("@getWhereQueryParamMap(");
        sql.append(MapperContant.ENTITY_CLASS).append(",");
        sql.append("'").append(MapperContant.SORT_EXPRESSION).append("',");
        sql.append(MapperContant.SORT_PARAMS).append(")");
        sql.append("\"/>");
        return sql.toString();
    }
}