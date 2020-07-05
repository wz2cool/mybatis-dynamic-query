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
public interface SelectMinByDynamicQueryMapper<T> {

    QueryHelper QUERY_HELPER = new QueryHelper();

    /**
     * Select min value of column by dynamic query.
     *
     * @param column       the column need get min value
     * @param dynamicQuery dynamic query
     * @return min value of column.
     */
    @SelectProvider(type = DynamicQueryProvider.class, method = "dynamicSQL")
    Object selectMinByDynamicQuery(
            @Param(MapperConstants.COLUMN) String column, @Param(MapperConstants.DYNAMIC_QUERY) DynamicQuery<T> dynamicQuery);

    /**
     * Select min value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get min value
     * @param dynamicQuery        dynamic query.
     * @return min value of property.
     */
    default <R extends Comparable> Object selectMinByDynamicQueryInternal(
            GetPropertyFunction<T, R> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        String propertyName = CommonsHelper.getPropertyName(getPropertyFunction);
        Class entityClass = dynamicQuery.getEntityClass();
        String queryColumn = QUERY_HELPER.getQueryColumnByProperty(entityClass, propertyName);
        return selectMinByDynamicQuery(queryColumn, dynamicQuery);
    }

    /**
     * Select min value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get min value
     * @param dynamicQuery        dynamic query.
     * @return min value of property.
     */
    default Optional<BigDecimal> selectMinByDynamicQuery(
            GetBigDecimalPropertyFunction<T> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        Object result = selectMinByDynamicQueryInternal(getPropertyFunction, dynamicQuery);
        return Optional.ofNullable(TypeHelper.getBigDecimal(result));
    }

    /**
     * Select min value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get min value
     * @param dynamicQuery        dynamic query.
     * @return min value of property.
     */
    default Optional<Byte> selectMinByDynamicQuery(
            GetBytePropertyFunction<T> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        Object result = selectMinByDynamicQueryInternal(getPropertyFunction, dynamicQuery);
        return Optional.ofNullable(TypeHelper.getByte(result));
    }

    /**
     * Select min value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get min value
     * @param dynamicQuery        dynamic query.
     * @return min value of property.
     */
    default Optional<Date> selectMinByDynamicQuery(
            GetDatePropertyFunction<T> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        Object result = selectMinByDynamicQueryInternal(getPropertyFunction, dynamicQuery);
        return Optional.ofNullable(TypeHelper.getDate(result));
    }

    /**
     * Select min value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get min value
     * @param dynamicQuery        dynamic query.
     * @return min value of property.
     */
    default Optional<Double> selectMinByDynamicQuery(
            GetDoublePropertyFunction<T> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        Object result = selectMinByDynamicQueryInternal(getPropertyFunction, dynamicQuery);
        return Optional.ofNullable(TypeHelper.getDouble(result));
    }

    /**
     * Select min value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get min value
     * @param dynamicQuery        dynamic query.
     * @return min value of property.
     */
    default Optional<Float> selectMinByDynamicQuery(
            GetFloatPropertyFunction<T> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        Object result = selectMinByDynamicQueryInternal(getPropertyFunction, dynamicQuery);
        return Optional.ofNullable(TypeHelper.getFloat(result));
    }

    /**
     * Select min value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get min value
     * @param dynamicQuery        dynamic query.
     * @return min value of property.
     */
    default Optional<Integer> selectMinByDynamicQuery(
            GetIntegerPropertyFunction<T> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        Object result = selectMinByDynamicQueryInternal(getPropertyFunction, dynamicQuery);
        return Optional.ofNullable(TypeHelper.getInteger(result));
    }

    /**
     * Select min value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get min value
     * @param dynamicQuery        dynamic query.
     * @return min value of property.
     */
    default Optional<Long> selectMinByDynamicQuery(
            GetLongPropertyFunction<T> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        Object result = selectMinByDynamicQueryInternal(getPropertyFunction, dynamicQuery);
        return Optional.ofNullable(TypeHelper.getLong(result));
    }

    /**
     * Select min value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get min value
     * @param dynamicQuery        dynamic query.
     * @return min value of property.
     */
    default Optional<Short> selectMinByDynamicQuery(
            GetShortPropertyFunction<T> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        Object result = selectMinByDynamicQueryInternal(getPropertyFunction, dynamicQuery);
        return Optional.ofNullable(TypeHelper.getShort(result));
    }

    /**
     * Select min value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get min value
     * @param dynamicQuery        dynamic query.
     * @return min value of property.
     */
    default Optional<String> selectMinByDynamicQuery(
            GetStringPropertyFunction<T> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        Object result = selectMinByDynamicQueryInternal(getPropertyFunction, dynamicQuery);
        return Optional.ofNullable(TypeHelper.getString(result));
    }
}
