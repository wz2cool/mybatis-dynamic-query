package com.github.wz2cool.dynamic.mybatis.mapper.constant;

/**
 * @author Frank
 */
public class MapperConstants {
    private MapperConstants() {
        throw new UnsupportedOperationException();
    }

    public static final String DISTINCT = "distinct";
    public static final String COLUMN = "column";
    public static final String DYNAMIC_QUERY = "dynamicQuery";
    public static final String DYNAMIC_QUERY_PARAMS = "dynamicQueryParams";
    public static final String GROUPED_QUERY = "groupedQuery";
    public static final String GROUPED_QUERY_PARAMS = "groupedQueryParams";
    public static final String SELECT_COLUMNS_EXPRESSION = "selectColumnsExpression";
    public static final String GROUP_COLUMNS_EXPRESSION = "groupColumnsExpression";
    public static final String WHERE_EXPRESSION = "whereExpression";
    public static final String HAVING_EXPRESSION = "havingExpression";
    public static final String SORT_EXPRESSION = "sortExpression";
    public static final String SET_EXPRESSION = "setExpression";
    public static final String UN_AS_SELECT_COLUMNS_EXPRESSION = "unAsSelectColumnsExpression";
}