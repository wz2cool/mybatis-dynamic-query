package com.github.wz2cool.dynamic.mybatis;

import com.github.wz2cool.dynamic.BaseFilterDescriptor;
import com.github.wz2cool.dynamic.DynamicQuery;
import com.github.wz2cool.dynamic.BaseSortDescriptor;
import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.lambda.GetPropertyFunction;
import com.github.wz2cool.dynamic.model.PropertyInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * The type Mybatis query provider.
 */
public class MybatisQueryProvider<T> {
    private static final QueryHelper QUERY_HELPER = new QueryHelper();
    private final DynamicQuery<T> dynamicQuery;
    private String whereExpressionPlaceholder;
    private String sortExpressionPlaceholder;
    private String columnsExpressionPlaceHolder;

    private MybatisQueryProvider() {
        dynamicQuery = new DynamicQuery<>();
    }

    public static <T> MybatisQueryProvider<T> createInstance(final Class<T> entityClass) {
        MybatisQueryProvider<T> mybatisQueryProvider = new MybatisQueryProvider<>();
        mybatisQueryProvider.dynamicQuery.setEntityClass(entityClass);
        return mybatisQueryProvider;
    }

    public static <T> MybatisQueryProvider<T> createInstance(
            final Class<T> entityClass,
            String columnsExpressionPlaceHolder) {
        MybatisQueryProvider<T> mybatisQueryProvider = new MybatisQueryProvider<>();
        mybatisQueryProvider.dynamicQuery.setEntityClass(entityClass);
        mybatisQueryProvider.columnsExpressionPlaceHolder = columnsExpressionPlaceHolder;
        return mybatisQueryProvider;
    }

    public MybatisQueryProvider<T> addFilters(
            final String whereExpressionPlaceholder,
            final BaseFilterDescriptor... filters) {
        this.whereExpressionPlaceholder = whereExpressionPlaceholder;
        this.dynamicQuery.setFilters(filters);
        return this;
    }

    public MybatisQueryProvider<T> addSorts(
            final String sortExpressionPlaceholder,
            final BaseSortDescriptor... sorts) {
        this.sortExpressionPlaceholder = sortExpressionPlaceholder;
        this.dynamicQuery.setSorts(sorts);
        return this;
    }

    public Map<String, Object> toQueryParam() {
        return MybatisQueryProvider.getQueryParamMap(
                this.dynamicQuery,
                this.whereExpressionPlaceholder,
                this.sortExpressionPlaceholder,
                this.columnsExpressionPlaceHolder);
    }

    /// region static method
    public static <T> String getQueryColumn(
            final GetPropertyFunction<T> getFieldFunc) {
        PropertyInfo propertyInfo = CommonsHelper.getPropertyInfo(getFieldFunc);
        String propertyName = propertyInfo.getPropertyName();
        Class ownerClass = propertyInfo.getOwnerClass();
        ColumnInfo columnInfo = QUERY_HELPER.getColumnInfo(ownerClass, propertyName);
        return columnInfo.getQueryColumn();
    }

    private static <T> Map<String, Object> getQueryParamMap(
            final DynamicQuery<T> dynamicQuery,
            final String whereExpressionPlaceholder,
            final String sortExpressionPlaceholder,
            final String columnsExpressionPlaceHolder) {
        if (dynamicQuery == null) {
            throw new NullPointerException("dynamicQuery");
        }

        Map<String, Object> result = new HashMap<>();
        Class<T> entityClass = dynamicQuery.getEntityClass();
        if (StringUtils.isNotBlank(whereExpressionPlaceholder)) {
            BaseFilterDescriptor[] filters = dynamicQuery.getFilters();
            Map<String, Object> whereQueryParamMap =
                    getWhereQueryParamMap(entityClass, whereExpressionPlaceholder, filters);
            result.putAll(whereQueryParamMap);
        }

        if (StringUtils.isNotBlank(sortExpressionPlaceholder)) {
            BaseSortDescriptor[] sorts = dynamicQuery.getSorts();
            Map<String, Object> sortQueryParamMap =
                    getSortQueryParamMap(entityClass, sortExpressionPlaceholder, sorts);
            result.putAll(sortQueryParamMap);
        }

        if (StringUtils.isNotBlank(columnsExpressionPlaceHolder)) {
            String allColumnExpression = getAllColumnsExpression(entityClass);
            result.put(columnsExpressionPlaceHolder, allColumnExpression);
        }

        return result;
    }

    /**
     * Gets where query param map.
     *
     * @param entityClass                the entity class
     * @param whereExpressionPlaceholder the where expression placeholder
     * @param filters                    the filters
     * @return the where query param map
     */
    public static Map<String, Object> getWhereQueryParamMap(
            final Class entityClass,
            final String whereExpressionPlaceholder,
            final BaseFilterDescriptor... filters) {

        if (StringUtils.isBlank(whereExpressionPlaceholder)) {
            throw new NullPointerException("whereExpressionPlaceholder");
        }

        ParamExpression paramExpression = getWhereExpression(entityClass, filters);
        Map<String, Object> queryParams = new LinkedHashMap<>();
        queryParams.put(whereExpressionPlaceholder, paramExpression.getExpression());
        queryParams.putAll(paramExpression.getParamMap());

        return queryParams;
    }


    /**
     * Gets where expression.
     *
     * @param entityClass the entity class
     * @param filters     the filters
     * @return the where expression
     */
    public static ParamExpression getWhereExpression(final Class entityClass, final BaseFilterDescriptor... filters) {
        return QUERY_HELPER.toWhereExpression(entityClass, filters);
    }

    /**
     * Gets sort query param map.
     *
     * @param entityClass               the entity class
     * @param sortExpressionPlaceholder the sort expression placeholder
     * @param sorts                     the sorts
     * @return the sort query param map
     */
    public static Map<String, Object> getSortQueryParamMap(
            final Class entityClass,
            final String sortExpressionPlaceholder,
            final BaseSortDescriptor... sorts) {
        if (StringUtils.isBlank(sortExpressionPlaceholder)) {
            throw new NullPointerException("sortExpressionPlaceholder");
        }

        ParamExpression sortExpression = getSortExpression(entityClass, sorts);
        Map<String, Object> queryParams = new LinkedHashMap<>();
        queryParams.put(sortExpressionPlaceholder, sortExpression.getExpression());
        queryParams.putAll(sortExpression.getParamMap());
        return queryParams;
    }

    /**
     * Gets sort expression.
     *
     * @param entityClass the entity class
     * @param sorts       the sorts
     * @return the sort expression
     */
    public static ParamExpression getSortExpression(final Class entityClass, final BaseSortDescriptor... sorts) {
        return QUERY_HELPER.toSortExpression(entityClass, sorts);
    }

    public static String getAllColumnsExpression(final Class entityClass) {
        return QUERY_HELPER.toAllColumnsExpression(entityClass);
    }
    /// endregion
}
