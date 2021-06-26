package com.github.wz2cool.dynamic;

import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.lambda.*;
import com.github.wz2cool.dynamic.mybatis.ParamExpression;
import com.github.wz2cool.dynamic.mybatis.QueryHelper;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author Frank
 * @date 2021/06/26
 **/
public class UpdateQuery<T> extends BaseFilterGroup<T, UpdateQuery<T>> {

    private static final QueryHelper QUERY_HELPER = new QueryHelper();
    private final Map<String, Object> setColumnValueMap = new HashMap<>();
    private Class<T> entityClass;

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
        final String propertyName = CommonsHelper.getPropertyName(getPropertyFunc);
        setColumnValueMap.put(propertyName, value);
        return this;
    }

    public UpdateQuery<T> set(GetBytePropertyFunction<T> getPropertyFunc, Byte value) {
        final String propertyName = CommonsHelper.getPropertyName(getPropertyFunc);
        setColumnValueMap.put(propertyName, value);
        return this;
    }

    public UpdateQuery<T> set(GetDatePropertyFunction<T> getPropertyFunc, Date value) {
        final String propertyName = CommonsHelper.getPropertyName(getPropertyFunc);
        setColumnValueMap.put(propertyName, value);
        return this;
    }

    public UpdateQuery<T> set(GetDoublePropertyFunction<T> getPropertyFunc, Double value) {
        final String propertyName = CommonsHelper.getPropertyName(getPropertyFunc);
        setColumnValueMap.put(propertyName, value);
        return this;
    }

    public UpdateQuery<T> set(GetFloatPropertyFunction<T> getPropertyFunc, Float value) {
        final String propertyName = CommonsHelper.getPropertyName(getPropertyFunc);
        setColumnValueMap.put(propertyName, value);
        return this;
    }

    public UpdateQuery<T> set(GetIntegerPropertyFunction<T> getPropertyFunc, Integer value) {
        final String propertyName = CommonsHelper.getPropertyName(getPropertyFunc);
        setColumnValueMap.put(propertyName, value);
        return this;
    }

    public UpdateQuery<T> set(GetLongPropertyFunction<T> getPropertyFunc, Long value) {
        final String propertyName = CommonsHelper.getPropertyName(getPropertyFunc);
        setColumnValueMap.put(propertyName, value);
        return this;
    }

    public UpdateQuery<T> set(GetShortPropertyFunction<T> getPropertyFunc, Short value) {
        final String propertyName = CommonsHelper.getPropertyName(getPropertyFunc);
        setColumnValueMap.put(propertyName, value);
        return this;
    }

    public UpdateQuery<T> set(GetStringPropertyFunction<T> getPropertyFunc, String value) {
        final String propertyName = CommonsHelper.getPropertyName(getPropertyFunc);
        setColumnValueMap.put(propertyName, value);
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

        final Map<String, Object> updateParamMap = toUpdateParamMap(setColumnValueMap);
        paramMap.putAll(updateParamMap);
        return paramMap;
    }

    private Map<String, Object> toUpdateParamMap(Map<String, Object> setColumnValueMap) {
        Map<String, Object> result = new HashMap<>();
        List<String> setExpressionItems = new ArrayList<>();
        for (Map.Entry<String, Object> columnValueEntry : setColumnValueMap.entrySet()) {
            String valuePlaceHolder = String.format("param_%s", columnValueEntry.getKey());
            final String setExpressionItem = String.format("%s=#{%s.%s}",
                    columnValueEntry.getKey(), MapperConstants.DYNAMIC_QUERY_PARAMS, valuePlaceHolder);
            setExpressionItems.add(setExpressionItem);
            result.put(valuePlaceHolder, columnValueEntry.getValue());
        }
        String setExpression = String.join(",", setExpressionItems);
        result.put(MapperConstants.SET_EXPRESSION, setExpression);
        return result;
    }
}
