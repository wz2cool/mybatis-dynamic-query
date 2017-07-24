package com.github.wz2cool.dynamic.mybatis;

import com.github.wz2cool.dynamic.*;
import com.github.wz2cool.helper.CommonsHelper;
import org.apache.commons.lang3.StringUtils;

import java.security.InvalidParameterException;
import java.util.*;

/**
 * Created by Frank on 7/12/2017.
 */
class QueryHelper {
    private final EntityCache entityCache = EntityCache.getInstance();
    private final DbExpressionHelper dbExpressionHelper;

    public QueryHelper(DatabaseType databaseType) {
        this.dbExpressionHelper = new DbExpressionHelper(databaseType);
    }

    // region filter
    ParamExpression toWhereExpression(Class entityClass, final FilterDescriptorBase[] filters) {
        if (filters == null || filters.length == 0) {
            return new ParamExpression();
        }

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

    ParamExpression toWhereExpression(Class entityClass, final FilterDescriptorBase filterDescriptorBase) {
        if (filterDescriptorBase instanceof FilterDescriptor) {
            return toWhereExpression(entityClass, (FilterDescriptor) filterDescriptorBase);
        } else if (filterDescriptorBase instanceof FilterGroupDescriptor) {
            FilterGroupDescriptor filterGroupDescriptor = (FilterGroupDescriptor) filterDescriptorBase;
            return toWhereExpression(entityClass, filterGroupDescriptor.getFilters());
        } else {
            return new ParamExpression();
        }
    }

    private ParamExpression toWhereExpression(final Class entityClass, final FilterDescriptor filterDescriptor) {
        String propertyPath = filterDescriptor.getPropertyPath();
        FilterOperator operator = filterDescriptor.getOperator();
        String[] filterValues = getFilterValues(filterDescriptor);

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
            for (String filterValue : filterValues) {
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

            String filterValue = processSingleFilterValue(operator, filterValues[0]);
            paramMap.put(paramPlaceholder, filterValue);
        }

        ParamExpression paramExpression = new ParamExpression();
        paramExpression.setExpression(expression);
        paramExpression.getParamMap().putAll(paramMap);
        return paramExpression;
    }

    String processSingleFilterValue(final FilterOperator operator, final String filterValue) {
        String result;
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

        return dbExpressionHelper.getExpression(filterDescriptor.getOperator(), columnInfo, value, paramPlaceholders);
    }

    String[] getFilterValues(final FilterDescriptor filterDescriptor) {
        FilterOperator operator = filterDescriptor.getOperator();
        Object filterValue = filterDescriptor.getValue();
        if (operator == FilterOperator.IN || operator == FilterOperator.NOT_IN) {
            if (CommonsHelper.isArrayOrCollection(filterValue)) {
                return cleanFilterValues(CommonsHelper.getCollectionValues(filterValue));
            } else {
                String errMsg = "filter value of \"IN\" or \"NOT_IN\" operator must be array or collection";
                throw new InvalidParameterException(errMsg);
            }
        }

        if (operator == FilterOperator.BETWEEN) {
            if (CommonsHelper.isArrayOrCollection(filterValue)) {
                String[] filterValues = cleanFilterValues(CommonsHelper.getCollectionValues(filterValue));
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
            return new String[]{null};
        } else {
            return cleanFilterValues(filterValue);
        }
    }

    String[] cleanFilterValues(final Object... filterValues) {
        if (filterValues == null || filterValues.length == 0) {
            return new String[0];
        }

        Collection<String> newFilterValues = new ArrayList<>();
        for (Object filterValue : filterValues) {
            String useFilterValue = cleanFilterValue(CommonsHelper.toStringSafe(filterValue));
            newFilterValues.add(cleanFilterValue(useFilterValue));
        }
        return newFilterValues.toArray(new String[newFilterValues.size()]);
    }

    String cleanFilterValue(String originalFilterValue) {
        if (StringUtils.isBlank(originalFilterValue)) {
            return "";
        }
        String result = originalFilterValue;
        result = StringUtils.strip(result, "\"");
        result = StringUtils.strip(result, "'");
        result = StringUtils.strip(result, "`");
        return result;
    }

    // endregion

    // region sort
    private String toSortExpression(final Class entityClass, final SortDescriptor sortDescriptor) {
        ColumnInfo columnInfo = entityCache.getColumnInfo(entityClass, sortDescriptor.getPropertyPath());
        return String.format("%s %s", columnInfo.getQueryColumn(), sortDescriptor.getSortDirection());
    }

    String toSortExpression(final Class entityClass, final SortDescriptor... sorts) {
        if (entityClass == null || sorts == null || sorts.length == 0) {
            return "";
        }

        String[] sortExpressions = Arrays.stream(sorts)
                .map(x -> toSortExpression(entityClass, x)).toArray(String[]::new);

        return String.join(", ", sortExpressions);
    }
    // endregion
}
