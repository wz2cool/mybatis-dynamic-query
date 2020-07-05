package com.github.wz2cool.dynamic.mybatis.mapper;

import com.github.wz2cool.dynamic.DynamicQuery;
import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.lambda.*;
import com.github.wz2cool.dynamic.mybatis.TypeHelper;
import com.github.wz2cool.dynamic.mybatis.QueryHelper;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicQueryProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

/**
 * @author Frank
 */
@RegisterMapper
@SuppressWarnings("squid:S1214")
public interface SelectMaxByDynamicQueryMapper<T> {

    QueryHelper QUERY_HELPER = new QueryHelper();

    /**
     * Select max value of column by dynamic query.
     *
     * @param column       the column need get max value
     * @param dynamicQuery dynamic query
     * @return max value of column.
     */
    @SelectProvider(type = DynamicQueryProvider.class, method = "dynamicSQL")
    Object selectMaxByDynamicQuery(
            @Param(MapperConstants.COLUMN) String column, @Param(MapperConstants.DYNAMIC_QUERY) DynamicQuery<T> dynamicQuery);

    /**
     * Select max value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get max value
     * @param dynamicQuery        dynamic query.
     * @return max value of property.
     */
    default <R extends Comparable> Object selectMaxByDynamicQueryInternal(
            GetPropertyFunction<T, R> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        String propertyName = CommonsHelper.getPropertyName(getPropertyFunction);
        Class entityClass = dynamicQuery.getEntityClass();
        String queryColumn = QUERY_HELPER.getQueryColumnByProperty(entityClass, propertyName);
        return selectMaxByDynamicQuery(queryColumn, dynamicQuery);
    }

    /**
     * Select max value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get max value
     * @param dynamicQuery        dynamic query.
     * @return max value of property.
     */
    default Optional<BigDecimal> selectMaxByDynamicQuery(
            GetBigDecimalPropertyFunction<T> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        Object result = selectMaxByDynamicQueryInternal(getPropertyFunction, dynamicQuery);
        return Optional.ofNullable(TypeHelper.getBigDecimal(result));
    }

    /**
     * Select max value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get max value
     * @param dynamicQuery        dynamic query.
     * @return max value of property.
     */
    default Optional<Byte> selectMaxByDynamicQuery(
            GetBytePropertyFunction<T> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        Object result = selectMaxByDynamicQueryInternal(getPropertyFunction, dynamicQuery);
        return Optional.ofNullable(TypeHelper.getByte(result));
    }

    /**
     * Select max value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get max value
     * @param dynamicQuery        dynamic query.
     * @return max value of property.
     */
    default Optional<Date> selectMaxByDynamicQuery(
            GetDatePropertyFunction<T> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        Object result = selectMaxByDynamicQueryInternal(getPropertyFunction, dynamicQuery);
        return Optional.ofNullable(TypeHelper.getDate(result));
    }

    /**
     * Select max value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get max value
     * @param dynamicQuery        dynamic query.
     * @return max value of property.
     */
    default Optional<Double> selectMaxByDynamicQuery(
            GetDoublePropertyFunction<T> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        Object result = selectMaxByDynamicQueryInternal(getPropertyFunction, dynamicQuery);
        return Optional.ofNullable(TypeHelper.getDouble(result));
    }

    /**
     * Select max value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get max value
     * @param dynamicQuery        dynamic query.
     * @return max value of property.
     */
    default Optional<Float> selectMaxByDynamicQuery(
            GetFloatPropertyFunction<T> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        Object result = selectMaxByDynamicQueryInternal(getPropertyFunction, dynamicQuery);
        return Optional.ofNullable(TypeHelper.getFloat(result));
    }

    /**
     * Select max value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get max value
     * @param dynamicQuery        dynamic query.
     * @return max value of property.
     */
    default Optional<Integer> selectMaxByDynamicQuery(
            GetIntegerPropertyFunction<T> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        Object result = selectMaxByDynamicQueryInternal(getPropertyFunction, dynamicQuery);
        return Optional.ofNullable(TypeHelper.getInteger(result));
    }

    /**
     * Select max value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get max value
     * @param dynamicQuery        dynamic query.
     * @return max value of property.
     */
    default Optional<Long> selectMaxByDynamicQuery(
            GetLongPropertyFunction<T> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        Object result = selectMaxByDynamicQueryInternal(getPropertyFunction, dynamicQuery);
        return Optional.ofNullable(TypeHelper.getLong(result));
    }

    /**
     * Select max value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get max value
     * @param dynamicQuery        dynamic query.
     * @return max value of property.
     */
    default Optional<Short> selectMaxByDynamicQuery(
            GetShortPropertyFunction<T> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        Object result = selectMaxByDynamicQueryInternal(getPropertyFunction, dynamicQuery);
        return Optional.ofNullable(TypeHelper.getShort(result));
    }

    /**
     * Select max value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get max value
     * @param dynamicQuery        dynamic query.
     * @return max value of property.
     */
    default Optional<String> selectMaxByDynamicQuery(
            GetStringPropertyFunction<T> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        Object result = selectMaxByDynamicQueryInternal(getPropertyFunction, dynamicQuery);
        return Optional.ofNullable(TypeHelper.getString(result));
    }
}
