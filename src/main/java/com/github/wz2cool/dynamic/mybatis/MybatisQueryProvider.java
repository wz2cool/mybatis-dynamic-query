package com.github.wz2cool.dynamic.mybatis;

import com.github.wz2cool.dynamic.FilterDescriptor;
import com.github.wz2cool.dynamic.FilterDescriptorBase;
import com.github.wz2cool.dynamic.FilterGroupDescriptor;
import com.github.wz2cool.dynamic.SortDescriptor;
import com.github.wz2cool.exception.InternalRuntimeException;
import com.github.wz2cool.exception.PropertyNotFoundException;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.*;


/**
 * The type Mybatis query provider.
 */
public class MybatisQueryProvider {
    private final EntityCache entityCache = EntityCache.getInstance();
    private final QueryHelper queryHelper = new QueryHelper();

    /**
     * Gets where query param map.
     *
     * @param entityClass                the entity class
     * @param whereExpressionPlaceholder the where expression placeholder
     * @param filters                    the filters
     * @return the where query param map
     * @throws PropertyNotFoundException the property not found exception
     */
    public Map<String, Object> getWhereQueryParamMap(
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
    public ParamExpression getWhereExpression(final Class entityClass, final FilterDescriptorBase... filters)
            throws PropertyNotFoundException {
        validFilters(entityClass, filters);
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
    public Map<String, Object> getSortQueryParamMap(
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
    public String getSortExpression(final Class entityClass, final SortDescriptor... sorts)
            throws PropertyNotFoundException {
        validSorts(entityClass, sorts);
        return queryHelper.toSortExpression(entityClass, sorts);
    }

    /**
     * Valid filters.
     *
     * @param entityClass the entity class
     * @param filters     the filters
     * @throws PropertyNotFoundException the property not found exception
     */
    void validFilters(final Class entityClass, final FilterDescriptorBase... filters)
            throws PropertyNotFoundException {
        if (filters == null || filters.length == 0) {
            return;
        }

        for (FilterDescriptorBase filter : filters) {
            if (filter instanceof FilterDescriptor) {
                FilterDescriptor useFilter = (FilterDescriptor) filter;
                String propertyPath = useFilter.getPropertyPath();
                if (!entityCache.hasProperty(entityClass, propertyPath)) {
                    String errMsg = String.format("Can't find property %s in %s", propertyPath, entityClass);
                    throw new PropertyNotFoundException(errMsg);
                }
            } else if (filter instanceof FilterGroupDescriptor) {
                FilterGroupDescriptor userGroupFilter = (FilterGroupDescriptor) filter;
                validFilters(entityClass, userGroupFilter.getFilters());
            }
        }
    }

    /**
     * Valid sorts.
     *
     * @param entityClass the entity class
     * @param sorts       the sorts
     * @throws PropertyNotFoundException the property not found exception
     */
    void validSorts(final Class entityClass, final SortDescriptor... sorts)
            throws PropertyNotFoundException {
        if (sorts == null || sorts.length == 0) {
            return;
        }

        for (SortDescriptor sort : sorts) {
            String propertyPath = sort.getPropertyPath();
            if (!entityCache.hasProperty(entityClass, propertyPath)) {
                String errMsg = String.format("Can't find property %s in %s", propertyPath, entityClass);
                throw new PropertyNotFoundException(errMsg);
            }
        }
    }

    /**
     * Gets field value.
     *
     * @param obj   the obj
     * @param field the field
     * @return the field value
     */
    Object getFieldValue(final Object obj, final Field field) {
        try {
            return field.get(obj);
        } catch (IllegalAccessException ex) {
            throw new InternalRuntimeException(ex);
        }
    }
}
