package com.github.wz2cool.dynamic.mybatis;

import com.github.wz2cool.dynamic.DynamicQuery;
import com.github.wz2cool.dynamic.FilterDescriptorBase;
import com.github.wz2cool.dynamic.SortDescriptor;
import com.github.wz2cool.exception.PropertyNotFoundException;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * The type Mybatis query provider.
 */
public class MybatisQueryProvider {
    private final static QueryHelper queryHelper = new QueryHelper();

    public static <T> Map<String, Object> getQueryParamMap(
            final DynamicQuery<T> dynamicQuery,
            final String whereExpressionPlaceholder,
            final String sortExpressionPlaceholder,
            final String columnsExpressionPlaceHolder) throws PropertyNotFoundException {
        if (dynamicQuery == null) {
            throw new NullPointerException("dynamicQuery");
        }

        Map<String, Object> result = new HashMap<>();
        Class<T> entityClass = dynamicQuery.getEntityClass();
        if (StringUtils.isNotBlank(whereExpressionPlaceholder)) {
            FilterDescriptorBase[] filters = dynamicQuery.getFilters();
            Map<String, Object> whereQueryParamMap =
                    getWhereQueryParamMap(entityClass, whereExpressionPlaceholder, filters);
            result.putAll(whereQueryParamMap);
        }

        if (StringUtils.isNotBlank(sortExpressionPlaceholder)) {
            SortDescriptor[] sorts = dynamicQuery.getSorts();
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
     * @throws PropertyNotFoundException the property not found exception
     */
    public static Map<String, Object> getWhereQueryParamMap(
            final Class entityClass,
            final String whereExpressionPlaceholder,
            final FilterDescriptorBase... filters) throws PropertyNotFoundException {

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
     * @throws PropertyNotFoundException the property not found exception
     */
    public static ParamExpression getWhereExpression(final Class entityClass, final FilterDescriptorBase... filters)
            throws PropertyNotFoundException {
        return queryHelper.toWhereExpression(entityClass, filters);
    }

    /**
     * Gets sort query param map.
     *
     * @param entityClass               the entity class
     * @param sortExpressionPlaceholder the sort expression placeholder
     * @param sorts                     the sorts
     * @return the sort query param map
     * @throws PropertyNotFoundException the property not found exception
     */
    public static Map<String, Object> getSortQueryParamMap(
            final Class entityClass,
            final String sortExpressionPlaceholder,
            final SortDescriptor... sorts) throws PropertyNotFoundException {
        if (StringUtils.isBlank(sortExpressionPlaceholder)) {
            throw new NullPointerException("sortExpressionPlaceholder");
        }

        String sortExpression = getSortExpression(entityClass, sorts);
        Map<String, Object> queryParams = new LinkedHashMap<>();
        queryParams.put(sortExpressionPlaceholder, sortExpression);
        return queryParams;
    }

    /**
     * Gets sort expression.
     *
     * @param entityClass the entity class
     * @param sorts       the sorts
     * @return the sort expression
     * @throws PropertyNotFoundException the property not found exception
     */
    public static String getSortExpression(final Class entityClass, final SortDescriptor... sorts)
            throws PropertyNotFoundException {
        return queryHelper.toSortExpression(entityClass, sorts);
    }

    public static String getAllColumnsExpression(final Class entityClass) {
        return queryHelper.toAllColumnsExpression(entityClass);
    }
}
