package com.github.wz2cool.dynamic.mybatis;

import com.github.wz2cool.dynamic.*;
import com.github.wz2cool.dynamic.exception.PropertyNotFoundException;
import com.github.wz2cool.dynamic.helper.CommonsHelper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.security.InvalidParameterException;
import java.util.*;

/**
 * @author Frank
 */
public class QueryHelper {
    private final EntityCache entityCache = EntityCache.getInstance();
    private final ExpressionHelper expressionHelper = new ExpressionHelper();

    // region filter

    public ParamExpression toWhereExpression(Class entityClass, final BaseFilterDescriptor[] filters) {
        if (filters == null || filters.length == 0) {
            return new ParamExpression();
        }

        String expression = "";
        Map<String, Object> paramMap = new LinkedHashMap<>();
        for (BaseFilterDescriptor baseFilterDescriptor : filters) {
            ParamExpression paramExpression = toWhereExpression(entityClass, baseFilterDescriptor);
            if (paramExpression != null) {
                paramMap.putAll(paramExpression.getParamMap());

                if (StringUtils.isEmpty(expression)) {
                    expression = paramExpression.getExpression();
                } else {
                    expression = String.format("%s %s %s",
                            expression, baseFilterDescriptor.getCondition(), paramExpression.getExpression());
                }
            }
        }

        expression = String.format("(%s)", expression);
        ParamExpression paramExpression = new ParamExpression();
        paramExpression.setExpression(expression);
        paramExpression.getParamMap().putAll(paramMap);
        return paramExpression;
    }

    ParamExpression toWhereExpression(Class entityClass, final BaseFilterDescriptor baseFilterDescriptor) {
        if (baseFilterDescriptor instanceof FilterDescriptor) {
            return toWhereExpression(entityClass, (FilterDescriptor) baseFilterDescriptor);
        } else if (baseFilterDescriptor instanceof FilterGroupDescriptor) {
            FilterGroupDescriptor filterGroupDescriptor = (FilterGroupDescriptor) baseFilterDescriptor;
            return toWhereExpression(entityClass, filterGroupDescriptor.getFilters());
        } else if (baseFilterDescriptor instanceof CustomFilterDescriptor) {
            CustomFilterDescriptor customFilterDescriptor = (CustomFilterDescriptor) baseFilterDescriptor;
            return toWhereExpression(customFilterDescriptor);
        } else {
            return new ParamExpression();
        }
    }

    ParamExpression toWhereExpression(final CustomFilterDescriptor customFilterDescriptor) {
        Map<String, Object> paramMap = new HashMap<>();
        Object[] params = customFilterDescriptor.getParams();
        String expression = customFilterDescriptor.getExpression();
        for (int i = 0; i < params.length; i++) {
            String genParamName = String.format("param_custom_filter_%s", UUID.randomUUID().toString().replace("-", ""));
            expression = expression.replace(String.format("{%s}", i), String.format("#{%s}", genParamName));
            paramMap.put(genParamName, params[i]);
        }

        ParamExpression paramExpression = new ParamExpression();
        paramExpression.setExpression(expression);
        paramExpression.getParamMap().putAll(paramMap);
        return paramExpression;
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

    public ParamExpression toSortExpression(final Class entityClass, final BaseSortDescriptor... sorts) {
        if (entityClass == null || sorts == null || sorts.length == 0) {
            return new ParamExpression();
        }

        String expression = "";
        Map<String, Object> paramMap = new LinkedHashMap<>();
        for (BaseSortDescriptor sort : sorts) {
            ParamExpression paramExpression = toSortExpression(entityClass, sort);
            if (paramExpression != null) {
                paramMap.putAll(paramExpression.getParamMap());

                if (StringUtils.isEmpty(expression)) {
                    expression = paramExpression.getExpression();
                } else if (StringUtils.isNotBlank(paramExpression.getExpression())) {
                    expression = String.format("%s, %s", expression, paramExpression.getExpression());
                }
            }
        }
        ParamExpression paramExpression = new ParamExpression();
        paramExpression.setExpression(expression);
        paramExpression.getParamMap().putAll(paramMap);
        return paramExpression;
    }

    ParamExpression toSortExpression(final Class entityClass, final BaseSortDescriptor baseSortDescriptor) {
        if (baseSortDescriptor instanceof SortDescriptor) {
            return toSortExpression(entityClass, (SortDescriptor) baseSortDescriptor);
        } else if (baseSortDescriptor instanceof CustomSortDescriptor) {
            return toSortExpression((CustomSortDescriptor) baseSortDescriptor);
        } else {
            return new ParamExpression();
        }
    }

    ParamExpression toSortExpression(final Class entityClass, final SortDescriptor sortDescriptor) {
        ParamExpression paramExpression = new ParamExpression();
        ColumnInfo columnInfo = entityCache.getColumnInfo(entityClass, sortDescriptor.getPropertyPath());
        String expression = String.format("%s %s", columnInfo.getQueryColumn(), sortDescriptor.getSortDirection());
        paramExpression.setExpression(expression);
        return paramExpression;
    }

    ParamExpression toSortExpression(final CustomSortDescriptor customSortDescriptor) {
        if (customSortDescriptor == null) {
            return new ParamExpression();
        }

        Map<String, Object> paramMap = new HashMap<>();
        Object[] params = customSortDescriptor.getParams();
        String expression = customSortDescriptor.getExpression();
        for (int i = 0; i < params.length; i++) {
            String genParamName = String.format("param_custom_sort_%s",
                    UUID.randomUUID().toString().replace("-", ""));
            expression = expression.replace(String.format("{%s}", i), String.format("#{%s}", genParamName));
            paramMap.put(genParamName, params[i]);
        }

        ParamExpression paramExpression = new ParamExpression();
        paramExpression.setExpression(expression);
        paramExpression.getParamMap().putAll(paramMap);
        return paramExpression;
    }
    // endregion

    public String toSelectColumnsExpression(final Class entityClass,
                                            final String[] selectedProperties,
                                            final String[] ignoredProperties,
                                            final boolean mapUnderscoreToCamelCase) {
        ColumnInfo[] columnInfos = entityCache.getColumnInfos(entityClass);
        List<String> columns = new ArrayList<>();
        boolean isSelectedPropertiesNotEmpty = ArrayUtils.isNotEmpty(selectedProperties);
        boolean isIgnoredPropertiesNotEmpty = ArrayUtils.isNotEmpty(ignoredProperties);
        for (ColumnInfo columnInfo : columnInfos) {
            String fieldName = columnInfo.getField().getName();
            boolean needSelectColumn;
            if (isSelectedPropertiesNotEmpty) {
                needSelectColumn = ArrayUtils.contains(selectedProperties, fieldName);
            } else if (isIgnoredPropertiesNotEmpty) {
                needSelectColumn = !ArrayUtils.contains(ignoredProperties, fieldName);
            } else {
                needSelectColumn = true;
            }

            if (needSelectColumn) {
                // 这里我们需要判断一下，是否设置了 @column ,如果有的话，我们不做驼峰
                String useFieldName = mapUnderscoreToCamelCase ? EntityHelper.camelCaseToUnderscore(fieldName) : fieldName;
                String column = String.format("%s AS %s", columnInfo.getQueryColumn(), useFieldName);
                columns.add(column);
            }
        }
        return String.join(", ", columns);
    }

    String toAllColumnsExpression(final Class entityClass) {
        ColumnInfo[] columnInfos = entityCache.getColumnInfos(entityClass);
        List<String> columns = new ArrayList<>();
        for (ColumnInfo columnInfo : columnInfos) {
            String column = String.format("%s AS %s",
                    columnInfo.getQueryColumn(),
                    EntityHelper.camelCaseToUnderscore(columnInfo.getField().getName()));
            columns.add(column);
        }
        return String.join(", ", columns);
    }

    ColumnInfo getColumnInfo(final Class entityClass, final String propertyName) {
        return entityCache.getColumnInfo(entityClass, propertyName);
    }

    /**
     * Valid filters.
     *
     * @param entityClass the entity class
     * @param filters     the filters
     * @throws PropertyNotFoundException the property not found exception
     */
    void validFilters(final Class entityClass, final BaseFilterDescriptor... filters)
            throws PropertyNotFoundException {
        if (filters == null || filters.length == 0) {
            return;
        }

        for (BaseFilterDescriptor filter : filters) {
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
    void validSorts(final Class entityClass, final BaseSortDescriptor... sorts)
            throws PropertyNotFoundException {
        if (sorts == null || sorts.length == 0) {
            return;
        }

        for (BaseSortDescriptor sort : sorts) {
            if (sort instanceof SortDescriptor) {
                SortDescriptor useSort = (SortDescriptor) sort;
                String propertyPath = useSort.getPropertyPath();
                if (!entityCache.hasProperty(entityClass, propertyPath)) {
                    String errMsg = String.format("Can't find property %s in %s", propertyPath, entityClass);
                    throw new PropertyNotFoundException(errMsg);
                }
            }
        }
    }
}
