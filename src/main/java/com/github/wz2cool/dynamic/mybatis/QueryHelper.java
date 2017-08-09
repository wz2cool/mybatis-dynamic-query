package com.github.wz2cool.dynamic.mybatis;

import com.github.wz2cool.dynamic.*;
import com.github.wz2cool.exception.PropertyNotFoundException;
import com.github.wz2cool.helper.CommonsHelper;
import org.apache.commons.lang3.StringUtils;

import java.security.InvalidParameterException;
import java.util.*;

/**
 * Created by Frank on 7/12/2017.
 */
public class QueryHelper {
    private final EntityCache entityCache = EntityCache.getInstance();
    private final ExpressionHelper expressionHelper = new ExpressionHelper();

    // region filter
    public ParamExpression toWhereExpression(Class entityClass, final FilterDescriptorBase[] filters)
            throws PropertyNotFoundException {
        if (filters == null || filters.length == 0) {
            return new ParamExpression();
        }

        validFilters(entityClass, filters);
        validFilters(entityClass, filters);

        String expression = "";
        Map<String, Object> paramMap = new LinkedHashMap<>();
        for (FilterDescriptorBase filterDescriptorBase : filters) {
            ParamExpression paramExpression = toWhereExpression(entityClass, filterDescriptorBase);
            if (paramExpression != null) {
                paramMap.putAll(paramExpression.getParamMap());

                if (StringUtils.isEmpty(expression)) {
                    expression = paramExpression.getExpression();
                } else {
                    expression = String.format("%s %s %s",
                            expression, filterDescriptorBase.getCondition(), paramExpression.getExpression());
                }
            }
        }

        expression = String.format("(%s)", expression);
        ParamExpression paramExpression = new ParamExpression();
        paramExpression.setExpression(expression);
        paramExpression.getParamMap().putAll(paramMap);
        return paramExpression;
    }

    ParamExpression toWhereExpression(Class entityClass, final FilterDescriptorBase filterDescriptorBase)
            throws PropertyNotFoundException {
        if (filterDescriptorBase instanceof FilterDescriptor) {
            return toWhereExpression(entityClass, (FilterDescriptor) filterDescriptorBase);
        } else if (filterDescriptorBase instanceof FilterGroupDescriptor) {
            FilterGroupDescriptor filterGroupDescriptor = (FilterGroupDescriptor) filterDescriptorBase;
            return toWhereExpression(entityClass, filterGroupDescriptor.getFilters());
        } else if (filterDescriptorBase instanceof CustomFilterDescriptor) {
            CustomFilterDescriptor customFilterDescriptor = (CustomFilterDescriptor) filterDescriptorBase;
            ParamExpression paramExpression = new ParamExpression();
            paramExpression.setExpression(customFilterDescriptor.getExpression());
            paramExpression.getParamMap().putAll(customFilterDescriptor.getParamMap());
            return paramExpression;
        } else {
            return new ParamExpression();
        }
    }

    private ParamExpression toWhereExpression(final Class entityClass, final FilterDescriptor filterDescriptor) {
        String propertyPath = filterDescriptor.getPropertyPath();
        FilterOperator operator = filterDescriptor.getOperator();
        Object[] filterValues = getFilterValues(filterDescriptor);

        String expression;
        // keep order.
        Map<String, Object> paramMap = new LinkedHashMap<>();
        if (operator == FilterOperator.BETWEEN) {
            String paramPlaceholder1;
            String paramPlaceholder2;
            paramPlaceholder1 =
                    String.format("param_%s_BETWEEN_%s", propertyPath, UUID.randomUUID().toString().replace("-", ""));
            paramPlaceholder2 =
                    String.format("param_%s_BETWEEN_%s", propertyPath, UUID.randomUUID().toString().replace("-", ""));
            expression = generateFilterExpression(entityClass, filterDescriptor, paramPlaceholder1, paramPlaceholder2);
            paramMap.put(paramPlaceholder1, filterValues[0]);
            paramMap.put(paramPlaceholder2, filterValues[1]);
        } else if (operator == FilterOperator.IN || operator == FilterOperator.NOT_IN) {
            List<String> paramPlaceholders = new ArrayList<>();
            for (Object filterValue : filterValues) {
                String paramPlaceholder =
                        String.format("param_%s_%s_%s", propertyPath, operator, UUID.randomUUID().toString().replace("-", ""));
                paramPlaceholders.add(paramPlaceholder);
                paramMap.put(paramPlaceholder, filterValue);
            }

            String[] paramPlaceholdersArray = paramPlaceholders.toArray(new String[paramPlaceholders.size()]);
            expression = generateFilterExpression(entityClass, filterDescriptor, paramPlaceholdersArray);
        } else {
            String paramPlaceholder;
            paramPlaceholder =
                    String.format("param_%s_%s_%s", propertyPath, operator, UUID.randomUUID().toString().replace("-", ""));
            expression = generateFilterExpression(entityClass, filterDescriptor, paramPlaceholder);

            Object filterValue = processSingleFilterValue(operator, filterValues[0]);
            paramMap.put(paramPlaceholder, filterValue);
        }

        ParamExpression paramExpression = new ParamExpression();
        paramExpression.setExpression(expression);
        paramExpression.getParamMap().putAll(paramMap);
        return paramExpression;
    }

    Object processSingleFilterValue(final FilterOperator operator, final Object filterValue) {
        Object result;
        if (operator == FilterOperator.START_WITH) {
            result = (filterValue == null ? "" : filterValue) + "%";
        } else if (operator == FilterOperator.END_WITH) {
            result = "%" + (filterValue == null ? "" : filterValue);
        } else if (operator == FilterOperator.CONTAINS) {
            result = "%" + (filterValue == null ? "" : filterValue) + "%";
        } else {
            result = filterValue;
        }
        return result;
    }

    String generateFilterExpression(
            final Class entityClass, final FilterDescriptor filterDescriptor, final String... paramPlaceholders) {
        String propertyPath = filterDescriptor.getPropertyPath();
        Object value = filterDescriptor.getValue();
        ColumnInfo columnInfo = entityCache.getColumnInfo(entityClass, propertyPath);

        return expressionHelper.getExpression(filterDescriptor.getOperator(), columnInfo, value, paramPlaceholders);
    }

    Object[] getFilterValues(final FilterDescriptor filterDescriptor) {
        FilterOperator operator = filterDescriptor.getOperator();
        Object filterValue = filterDescriptor.getValue();
        if (operator == FilterOperator.IN || operator == FilterOperator.NOT_IN) {
            if (CommonsHelper.isArrayOrCollection(filterValue)) {
                return CommonsHelper.getCollectionValues(filterValue);
            } else {
                String errMsg = "filter value of \"IN\" or \"NOT_IN\" operator must be array or collection";
                throw new InvalidParameterException(errMsg);
            }
        }

        if (operator == FilterOperator.BETWEEN) {
            if (CommonsHelper.isArrayOrCollection(filterValue)) {
                Object[] filterValues = CommonsHelper.getCollectionValues(filterValue);
                if (filterValues.length != 2) {
                    String errMsg = "if \"BETWEEN\" operator, the count of filter value must be 2";
                    throw new InvalidParameterException(errMsg);
                }
                return filterValues;
            } else {
                String errMsg = "If \"BETWEEN\" operator, filter value must be array or collection";
                throw new InvalidParameterException(errMsg);
            }
        }

        if (CommonsHelper.isArrayOrCollection(filterValue)) {
            String errMsg = "if not \"BETWEEN\", \"IN\" or \"NOT_IN\" operator, filter value can not be array or collection.";
            throw new InvalidParameterException(errMsg);
        }

        if (filterValue == null) {
            return new Object[]{null};
        } else {
            return new Object[]{filterValue};
        }
    }

    // endregion

    // region sort
    private String toSortExpression(final Class entityClass, final SortDescriptor sortDescriptor) {
        ColumnInfo columnInfo = entityCache.getColumnInfo(entityClass, sortDescriptor.getPropertyPath());
        return String.format("%s %s", columnInfo.getQueryColumn(), sortDescriptor.getSortDirection());
    }

    public String toSortExpression(final Class entityClass, final SortDescriptor... sorts)
            throws PropertyNotFoundException {
        if (entityClass == null || sorts == null || sorts.length == 0) {
            return "";
        }

        validSorts(entityClass, sorts);

        String[] sortExpressions = Arrays.stream(sorts)
                .map(x -> toSortExpression(entityClass, x)).toArray(String[]::new);

        return String.join(", ", sortExpressions);
    }
    // endregion

    public String toAllColumnsExpression(final Class entityClass) {
        ColumnInfo[] columnInfos = entityCache.getColumnInfos(entityClass);
        List<String> columns = new ArrayList<>();
        for (ColumnInfo columnInfo : columnInfos) {
            String column = String.format("%s AS %s",
                    columnInfo.getQueryColumn(),
                    EntityHelper.camelCaseToUnderscore(columnInfo.getField().getName()));
            columns.add(column);
        }
        return String.join(",", columns);
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

}
