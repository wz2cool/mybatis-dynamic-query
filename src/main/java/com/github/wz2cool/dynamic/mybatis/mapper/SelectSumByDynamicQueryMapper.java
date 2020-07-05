package com.github.wz2cool.dynamic.mybatis.mapper;

import com.github.wz2cool.dynamic.DynamicQuery;
import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.lambda.*;
import com.github.wz2cool.dynamic.mybatis.QueryHelper;
import com.github.wz2cool.dynamic.mybatis.TypeHelper;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicQueryProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author Frank
 **/
@RegisterMapper
public interface SelectSumByDynamicQueryMapper<T> {

    QueryHelper QUERY_HELPER = new QueryHelper();

    /**
     * Select sum value of column by dynamic query.
     *
     * @param column       the column need get sum value
     * @param dynamicQuery dynamic query
     * @return max value of column.
     */
    @SelectProvider(type = DynamicQueryProvider.class, method = "dynamicSQL")
    Object selectSumByDynamicQuery(
            @Param(MapperConstants.COLUMN) String column, @Param(MapperConstants.DYNAMIC_QUERY) DynamicQuery<T> dynamicQuery);

    /**
     * Select sum value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get sum value
     * @param dynamicQuery        dynamic query.
     * @return sum value of property.
     */
    default <R extends Comparable> Object selectSumByDynamicQueryInternal(
            GetPropertyFunction<T, R> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        String propertyName = CommonsHelper.getPropertyName(getPropertyFunction);
        Class entityClass = dynamicQuery.getEntityClass();
        String queryColumn = QUERY_HELPER.getQueryColumnByProperty(entityClass, propertyName);
        return selectSumByDynamicQuery(queryColumn, dynamicQuery);
    }

    /**
     * Select sum value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get sum value
     * @param dynamicQuery        dynamic query.
     * @return sum value of property.
     */
    default Optional<BigDecimal> selectSumByDynamicQuery(
            GetBigDecimalPropertyFunction<T> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        Object result = selectSumByDynamicQueryInternal(getPropertyFunction, dynamicQuery);
        return Optional.ofNullable(TypeHelper.getBigDecimal(result));
    }

    /**
     * Select sum value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get sum value
     * @param dynamicQuery        dynamic query.
     * @return sum value of property.
     */
    default Optional<Long> selectSumByDynamicQuery(
            GetBytePropertyFunction<T> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        Object result = selectSumByDynamicQueryInternal(getPropertyFunction, dynamicQuery);
        return Optional.ofNullable(TypeHelper.getLong(result));
    }

    /**
     * Select sum value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get sum value
     * @param dynamicQuery        dynamic query.
     * @return sum value of property.
     */
    default Optional<BigDecimal> selectSumByDynamicQuery(
            GetDoublePropertyFunction<T> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        Object result = selectSumByDynamicQueryInternal(getPropertyFunction, dynamicQuery);
        return Optional.ofNullable(TypeHelper.getBigDecimal(result));
    }

    /**
     * Select sum value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get sum value
     * @param dynamicQuery        dynamic query.
     * @return sum value of property.
     */
    default Optional<BigDecimal> selectSumByDynamicQuery(
            GetFloatPropertyFunction<T> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        Object result = selectSumByDynamicQueryInternal(getPropertyFunction, dynamicQuery);
        return Optional.ofNullable(TypeHelper.getBigDecimal(result));
    }

    /**
     * Select sum value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get sum value
     * @param dynamicQuery        dynamic query.
     * @return sum value of property.
     */
    default Optional<Long> selectSumByDynamicQuery(
            GetIntegerPropertyFunction<T> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        Object result = selectSumByDynamicQueryInternal(getPropertyFunction, dynamicQuery);
        return Optional.ofNullable(TypeHelper.getLong(result));
    }

    /**
     * Select sum value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get sum value
     * @param dynamicQuery        dynamic query.
     * @return sum value of property.
     */
    default Optional<Long> selectSumByDynamicQuery(
            GetLongPropertyFunction<T> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        Object result = selectSumByDynamicQueryInternal(getPropertyFunction, dynamicQuery);
        return Optional.ofNullable(TypeHelper.getLong(result));
    }

    /**
     * Select sum value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get sum value
     * @param dynamicQuery        dynamic query.
     * @return sum value of property.
     */
    default Optional<Long> selectSumByDynamicQuery(
            GetShortPropertyFunction<T> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        Object result = selectSumByDynamicQueryInternal(getPropertyFunction, dynamicQuery);
        return Optional.ofNullable(TypeHelper.getLong(result));
    }
}
