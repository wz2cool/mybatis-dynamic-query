package com.github.wz2cool.dynamic;

import com.github.wz2cool.dynamic.exception.InternalRuntimeException;
import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.helper.ParamResolverHelper;
import com.github.wz2cool.dynamic.lambda.*;
import com.github.wz2cool.dynamic.model.SelectPropertyConfig;
import com.github.wz2cool.dynamic.mybatis.ColumnInfo;
import com.github.wz2cool.dynamic.mybatis.ParamExpression;
import com.github.wz2cool.dynamic.mybatis.QueryHelper;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.UnaryOperator;

/**
 * @author Frank
 **/
public class UpdateQuery<T> extends BaseFilterGroup<T, UpdateQuery<T>> {

    private static final QueryHelper QUERY_HELPER = new QueryHelper();
    private final Map<String, Object> setColumnValueMap = new HashMap<>();
    private Class<T> entityClass;

    private static final String FIRST_SQL_KEY = "mdq_first_sql";
    private static final String LAST_SQL_KEY = "mdq_last_sql";
    private static final String HINT_SQL_KEY = "mdq_hint_sql";

    protected Map<String, Object> customDynamicQueryParams = new HashMap<>();

    public UpdateQuery() {
        // for json
    }

    public UpdateQuery(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public static <T> UpdateQuery<T> createQuery(Class<T> entityClass) {
        return new UpdateQuery<>(entityClass);
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    public UpdateQuery<T> set(GetBigDecimalPropertyFunction<T> getPropertyFunc, BigDecimal value) {
        return set(true, getPropertyFunc, value);
    }

    public UpdateQuery<T> set(GetBytePropertyFunction<T> getPropertyFunc, Byte value) {
        return set(true, getPropertyFunc, value);
    }

    public UpdateQuery<T> set(GetDatePropertyFunction<T> getPropertyFunc, Date value) {
        return set(true, getPropertyFunc, value);
    }

    public UpdateQuery<T> set(GetDoublePropertyFunction<T> getPropertyFunc, Double value) {
        return set(true, getPropertyFunc, value);
    }

    public UpdateQuery<T> set(GetFloatPropertyFunction<T> getPropertyFunc, Float value) {
        return set(true, getPropertyFunc, value);
    }

    public UpdateQuery<T> set(GetIntegerPropertyFunction<T> getPropertyFunc, Integer value) {
        return set(true, getPropertyFunc, value);
    }

    public UpdateQuery<T> set(GetLongPropertyFunction<T> getPropertyFunc, Long value) {
        return set(true, getPropertyFunc, value);
    }

    public UpdateQuery<T> set(GetShortPropertyFunction<T> getPropertyFunc, Short value) {
        return set(true, getPropertyFunc, value);
    }

    public UpdateQuery<T> set(GetStringPropertyFunction<T> getPropertyFunc, String value) {
        return set(true, getPropertyFunc, value);
    }

    public UpdateQuery<T> set(T record) {
        return set(true, record);
    }

    public UpdateQuery<T> set(boolean enable, T record) {
        return set(enable, record, null);
    }

    public UpdateQuery<T> set(T record, UnaryOperator<SelectPropertyConfig<T>> getConfigFunc) {
        return set(true, record, getConfigFunc);
    }

    public UpdateQuery<T> set(boolean enable, T record, UnaryOperator<SelectPropertyConfig<T>> getConfigFunc) {
        if (enable) {
            SelectPropertyConfig<T> config = null;
            if (Objects.nonNull(getConfigFunc)) {
                config = getConfigFunc.apply(new SelectPropertyConfig<>());
            }
            Map<String, ColumnInfo> propertyColumnInfoMap = QUERY_HELPER.getPropertyColumnInfoMap(record.getClass());
            final Set<String> needUpdatePropertyNames = getNeedUpdatePropertyNames(propertyColumnInfoMap.keySet(), config);
            for (Map.Entry<String, ColumnInfo> propertyColumnInfoEntry : propertyColumnInfoMap.entrySet()) {
                String propertyName = propertyColumnInfoEntry.getKey();
                ColumnInfo columnInfo = propertyColumnInfoEntry.getValue();
                try {
                    if (needUpdatePropertyNames.contains(propertyName)) {
                        Object value = columnInfo.getField().get(record);
                        setColumnValueMap.put(propertyName, value);
                    }
                } catch (IllegalAccessException e) {
                    throw new InternalRuntimeException(e);
                }
            }
        }
        return this;
    }

    public UpdateQuery<T> set(boolean enable, GetBigDecimalPropertyFunction<T> getPropertyFunc, BigDecimal value) {
        if (enable) {
            final String propertyName = CommonsHelper.getPropertyName(getPropertyFunc);
            setColumnValueMap.put(propertyName, value);
        }
        return this;
    }

    public UpdateQuery<T> set(boolean enable, GetBytePropertyFunction<T> getPropertyFunc, Byte value) {
        if (enable) {
            final String propertyName = CommonsHelper.getPropertyName(getPropertyFunc);
            setColumnValueMap.put(propertyName, value);
        }
        return this;
    }

    public UpdateQuery<T> set(boolean enable, GetDatePropertyFunction<T> getPropertyFunc, Date value) {
        if (enable) {
            final String propertyName = CommonsHelper.getPropertyName(getPropertyFunc);
            setColumnValueMap.put(propertyName, value);
        }
        return this;
    }

    public UpdateQuery<T> set(boolean enable, GetDoublePropertyFunction<T> getPropertyFunc, Double value) {
        if (enable) {
            final String propertyName = CommonsHelper.getPropertyName(getPropertyFunc);
            setColumnValueMap.put(propertyName, value);
        }
        return this;
    }

    public UpdateQuery<T> set(boolean enable, GetFloatPropertyFunction<T> getPropertyFunc, Float value) {
        if (enable) {
            final String propertyName = CommonsHelper.getPropertyName(getPropertyFunc);
            setColumnValueMap.put(propertyName, value);
        }
        return this;
    }

    public UpdateQuery<T> set(boolean enable, GetIntegerPropertyFunction<T> getPropertyFunc, Integer value) {
        if (enable) {
            final String propertyName = CommonsHelper.getPropertyName(getPropertyFunc);
            setColumnValueMap.put(propertyName, value);
        }
        return this;
    }

    public UpdateQuery<T> set(boolean enable, GetLongPropertyFunction<T> getPropertyFunc, Long value) {
        if (enable) {
            final String propertyName = CommonsHelper.getPropertyName(getPropertyFunc);
            setColumnValueMap.put(propertyName, value);
        }
        return this;
    }

    public UpdateQuery<T> set(boolean enable, GetShortPropertyFunction<T> getPropertyFunc, Short value) {
        if (enable) {
            final String propertyName = CommonsHelper.getPropertyName(getPropertyFunc);
            setColumnValueMap.put(propertyName, value);
        }
        return this;
    }

    public UpdateQuery<T> set(boolean enable, GetStringPropertyFunction<T> getPropertyFunc, String value) {
        if (enable) {
            final String propertyName = CommonsHelper.getPropertyName(getPropertyFunc);
            setColumnValueMap.put(propertyName, value);
        }
        return this;
    }

    public Map<String, Object> toQueryParamMap(boolean isMapUnderscoreToCamelCase) {
        BaseFilterDescriptor[] filters = this.getFilters();
        ParamExpression whereParamExpression = QUERY_HELPER.toWhereExpression(entityClass, filters);
        String whereExpression = whereParamExpression.getExpression();
        Map<String, Object> paramMap = whereParamExpression.getParamMap();
        for (Map.Entry<String, Object> param : paramMap.entrySet()) {
            String key = param.getKey();
            String newKey = String.format("%s.%s", MapperConstants.DYNAMIC_QUERY_PARAMS, key);
            whereExpression = whereExpression.replace(key, newKey);
        }
        paramMap.put(MapperConstants.WHERE_EXPRESSION, whereExpression);

        final Map<String, Object> updateParamMap = toUpdateParamMap(isMapUnderscoreToCamelCase, setColumnValueMap);
        paramMap.putAll(updateParamMap);
        initDefaultQueryParams();
        paramMap.putAll(this.customDynamicQueryParams);
        return paramMap;
    }

    private Map<String, Object> toUpdateParamMap(boolean isMapUnderscoreToCamelCase, Map<String, Object> setColumnValueMap) {
        Map<String, Object> result = new HashMap<>();
        List<String> setExpressionItems = new ArrayList<>();
        for (Map.Entry<String, Object> columnValueEntry : setColumnValueMap.entrySet()) {
            String propertyName = columnValueEntry.getKey();
            String valuePlaceHolder = String.format("param_%s", columnValueEntry.getKey());
            final String columnName = QUERY_HELPER.getQueryColumnByProperty(this.getEntityClass(), propertyName);
            final String setExpressionItem = String.format("%s=#{%s.%s}",
                    columnName, MapperConstants.DYNAMIC_QUERY_PARAMS, valuePlaceHolder);
            setExpressionItems.add(setExpressionItem);
            result.put(valuePlaceHolder, columnValueEntry.getValue());
        }
        String setExpression = String.join(",", setExpressionItems);
        result.put(MapperConstants.SET_EXPRESSION, setExpression);
        return result;
    }

    private Set<String> getNeedUpdatePropertyNames(Set<String> allPropertyNames, SelectPropertyConfig<T> config) {
        if (Objects.isNull(config)) {
            return allPropertyNames;
        }

        if (!config.getSelectPropertyNames().isEmpty()) {
            return config.getSelectPropertyNames();
        }

        Set<String> result = new HashSet<>();
        for (String propertyName : allPropertyNames) {
            if (config.getIgnorePropertyNames().contains(propertyName)) {
                continue;
            }
            result.add(propertyName);
        }
        return result;
    }

    public final UpdateQuery<T> last(String lastSql) {
        return last(true, lastSql);
    }

    public final UpdateQuery<T> last(boolean enable, String lastSql) {
        if (enable) {
            String useLastSql = ParamResolverHelper.resolveExpression(lastSql);
            this.customDynamicQueryParams.put(LAST_SQL_KEY, useLastSql);
        }
        return this;
    }

    public final UpdateQuery<T> first(String firstSql) {
        return first(true, firstSql);
    }

    public final UpdateQuery<T> first(boolean enable, String firstSql) {
        if (enable) {
            String useFirstSql = ParamResolverHelper.resolveExpression(firstSql);
            this.customDynamicQueryParams.put(FIRST_SQL_KEY, useFirstSql);
        }
        return this;
    }

    public final UpdateQuery<T> hint(String hintSql) {
        return hint(true, hintSql);
    }

    /**
     * https://docs.oracle.com/cd/B13789_01/server.101/b10759/sql_elements006.htm#i35922
     *
     * @return
     */
    public final UpdateQuery<T> hint(boolean enable, String hintSql) {
        if (enable) {
            String useHintSql = ParamResolverHelper.resolveExpression(hintSql);
            this.customDynamicQueryParams.put(HINT_SQL_KEY, useHintSql);
        }
        return this;
    }

    public final UpdateQuery<T> queryParam(String key, Object value) {
        return queryParam(true, key, value);
    }

    public final UpdateQuery<T> queryParam(boolean enable, String key, Object value) {
        if (enable) {
            this.customDynamicQueryParams.put(key, value);
        }
        return this;
    }

    private void initDefaultQueryParams() {
        this.customDynamicQueryParams.putIfAbsent(LAST_SQL_KEY, "");
        this.customDynamicQueryParams.putIfAbsent(FIRST_SQL_KEY, "");
        this.customDynamicQueryParams.putIfAbsent(HINT_SQL_KEY, "");
    }
}
