package com.github.wz2cool.dynamic.mybatis;

import com.github.wz2cool.dynamic.FilterDescriptor;
import com.github.wz2cool.dynamic.FilterDescriptorBase;
import com.github.wz2cool.dynamic.FilterGroupDescriptor;
import com.github.wz2cool.dynamic.SortDescriptor;
import com.github.wz2cool.exception.InternalRuntimeException;
import com.github.wz2cool.exception.PropertyNotFoundException;
import com.github.wz2cool.helper.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Frank on 7/12/2017.
 */
public class MybatisQueryProvider {
    private final EntityCache entityCache = EntityCache.getInstance();
    private final QueryHelper queryHelper;

    public MybatisQueryProvider(DatabaseType databaseType) {
        queryHelper = new QueryHelper(databaseType);
    }

    public ParamExpression getInsertExpression(final Object tableEntity) {
        if (tableEntity == null) {
            throw new NullPointerException("tableEntity");
        }

        Class tableClass = tableEntity.getClass();
        DbColumnInfo[] dbColumnInfos = entityCache.getDbColumnInfos(tableClass);
        Map<String, Object> propValueMap = new LinkedHashMap<>();
        Collection<String> columns = new ArrayList<>();
        Collection<String> placeholders = new ArrayList<>();

        for (DbColumnInfo dbColumnInfo : dbColumnInfos) {
            Field propertyField = dbColumnInfo.getField();
            Object value = getFieldValue(tableEntity, propertyField);
            propValueMap.put(propertyField.getName(), value);
            columns.add(dbColumnInfo.getDbColumnName());
            placeholders.add(toPlaceholder(propertyField.getName()));
        }

        String tableName = EntityHelper.getTableName(tableClass);
        String columnStr = String.join(", ", columns);
        String placeholderStr = String.join(", ", placeholders);
        String expression = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, columnStr, placeholderStr);
        ParamExpression insertExpression = new ParamExpression();
        insertExpression.setExpression(expression);
        insertExpression.getParamMap().putAll(propValueMap);
        return insertExpression;
    }

    public ParamExpression getUpdateExpression(final Object tableEntity, final FilterDescriptorBase... filters)
            throws PropertyNotFoundException {
        if (tableEntity == null) {
            throw new NullPointerException("tableEntity");
        }
        Class tableClass = tableEntity.getClass();
        validFilters(tableClass, filters);

        Map<String, Object> allParamMap = new LinkedHashMap<>();
        DbColumnInfo[] dbColumnInfos = entityCache.getDbColumnInfos(tableClass);
        Map<String, Object> propValueMap = new LinkedHashMap<>();
        Collection<String> setValueStrings = new ArrayList<>();

        for (DbColumnInfo dbColumnInfo : dbColumnInfos) {
            Field field = dbColumnInfo.getField();
            Object value = getFieldValue(tableEntity, field);
            if (dbColumnInfo.isUpdateIfNull() || value != null) {
                propValueMap.put(field.getName(), value);
                String setValueString =
                        String.format("`%s`=%s", dbColumnInfo.getDbColumnName(), toPlaceholder(field.getName()));
                setValueStrings.add(setValueString);
            }
        }

        allParamMap.putAll(propValueMap);

        String tableName = EntityHelper.getTableName(tableClass);
        String setValueStr = String.join(", ", setValueStrings);
        String updateExpression = String.format("UPDATE %s SET %s", tableName, setValueStr);

        String expression;
        if (filters == null || filters.length == 0) {
            expression = updateExpression;
        } else {
            ParamExpression whereParamExpression = queryHelper.toWhereExpression(tableClass, filters);
            expression = String.format("%s WHERE %s", updateExpression, whereParamExpression.getExpression());
            allParamMap.putAll(whereParamExpression.getParamMap());
        }

        ParamExpression result = new ParamExpression();
        result.setExpression(expression);
        result.getParamMap().putAll(allParamMap);
        return result;
    }


    // region dynamic query
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

    public ParamExpression getWhereExpression(final Class entityClass, final FilterDescriptorBase... filters)
            throws PropertyNotFoundException {
        validFilters(entityClass, filters);
        return queryHelper.toWhereExpression(entityClass, filters);
    }

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

    public String getSortExpression(final Class entityClass, final SortDescriptor... sorts)
            throws PropertyNotFoundException {
        validSorts(entityClass, sorts);
        return queryHelper.toSortExpression(entityClass, sorts);
    }

    // endregion

    String toPlaceholder(String propertyName) {
        return String.format("#{%s}", propertyName);
    }

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

    Object getFieldValue(final Object obj, final Field field) {
        try {
            return field.get(obj);
        } catch (IllegalAccessException ex) {
            throw new InternalRuntimeException(ex);
        }
    }
}
