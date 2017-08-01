package com.github.wz2cool.dynamic.mybatis;

import com.github.wz2cool.dynamic.FilterDescriptor;
import com.github.wz2cool.dynamic.FilterDescriptorBase;
import com.github.wz2cool.dynamic.FilterGroupDescriptor;
import com.github.wz2cool.dynamic.SortDescriptor;
import com.github.wz2cool.exception.InternalRuntimeException;
import com.github.wz2cool.exception.PropertyNotFoundException;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * The type Mybatis query provider.
 */
public class MybatisQueryProvider {
    private final EntityCache entityCache = EntityCache.getInstance();
    private final QueryHelper queryHelper;

    /**
     * Instantiates a new Mybatis query provider.
     *
     * @param databaseType the database type
     */
    public MybatisQueryProvider(DatabaseType databaseType) {
        queryHelper = new QueryHelper(databaseType);
    }

    /**
     * Gets insert expression.
     *
     * @param tableEntity the table entity
     * @return the insert expression
     */
    public ParamExpression getInsertExpression(final Object tableEntity) {
        if (tableEntity == null) {
            throw new NullPointerException("tableEntity");
        }

        Class tableClass = tableEntity.getClass();
        ColumnInfo[] columnInfos = entityCache.getColumnInfos(tableClass);
        Map<String, Object> propValueMap = new LinkedHashMap<>();
        Collection<String> columns = new ArrayList<>();
        Collection<String> placeholders = new ArrayList<>();

        for (ColumnInfo columnInfo : columnInfos) {
            Field propertyField = columnInfo.getField();
            Object value = getFieldValue(tableEntity, propertyField);
            if (columnInfo.isInsertIfNull() || value != null) {
                propValueMap.put(propertyField.getName(), value);
                columns.add(columnInfo.getColumnName());
                placeholders.add(toPlaceholder(propertyField.getName(), columnInfo.getJdbcType()));
            }
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

    /**
     * Gets update expression.
     *
     * @param tableEntity the table entity
     * @param filters     the filters
     * @return the update expression
     * @throws PropertyNotFoundException the property not found exception
     */
    public ParamExpression getUpdateExpression(final Object tableEntity, final FilterDescriptorBase... filters)
            throws PropertyNotFoundException {
        if (tableEntity == null) {
            throw new NullPointerException("tableEntity");
        }
        Class tableClass = tableEntity.getClass();
        validFilters(tableClass, filters);

        Map<String, Object> allParamMap = new LinkedHashMap<>();
        ColumnInfo[] columnInfos = entityCache.getColumnInfos(tableClass);
        Map<String, Object> propValueMap = new LinkedHashMap<>();
        Collection<String> setValueStrings = new ArrayList<>();

        for (ColumnInfo columnInfo : columnInfos) {
            Field field = columnInfo.getField();
            Object value = getFieldValue(tableEntity, field);
            if (columnInfo.isUpdateIfNull() || value != null) {
                propValueMap.put(field.getName(), value);
                String setValueString =
                        String.format("`%s`=%s", columnInfo.getColumnName(),
                                toPlaceholder(field.getName(), columnInfo.getJdbcType()));
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

    public ParamExpression getDeleteExpression(final Class tableClass, final FilterDescriptorBase... filters)
            throws PropertyNotFoundException {
        if (tableClass == null) {
            throw new NullPointerException("tableClass");
        }

        String tableName = EntityHelper.getTableName(tableClass);
        String deleteExpression = String.format("DELETE FROM %s", tableName);

        Map<String, Object> allParamMap = new LinkedHashMap<>();
        String expression;
        if (filters == null || filters.length == 0) {
            expression = deleteExpression;
        } else {
            ParamExpression whereParamExpression = queryHelper.toWhereExpression(tableClass, filters);
            expression = String.format("%s WHERE %s", deleteExpression, whereParamExpression.getExpression());
            allParamMap.putAll(whereParamExpression.getParamMap());
        }

        ParamExpression result = new ParamExpression();
        result.setExpression(expression);
        result.getParamMap().putAll(allParamMap);
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
     * To placeholder string.
     *
     * @param propertyName the property name
     * @return the string
     */
    String toPlaceholder(String propertyName, JdbcType jdbcType) {
        if (jdbcType == null || JdbcType.NONE.equals(jdbcType)) {
            return String.format("#{%s}", propertyName);
        } else {
            return String.format("#{%s,jdbcType=%s}", propertyName, jdbcType);
        }
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
