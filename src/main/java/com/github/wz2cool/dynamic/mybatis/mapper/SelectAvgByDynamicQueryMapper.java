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


import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author Frank
 **/
public interface SelectAvgByDynamicQueryMapper<T> {

    QueryHelper QUERY_HELPER = new QueryHelper();

    /**
     * Select avg value of column by dynamic query.
     *
     * @param column       the column need get avg value
     * @param dynamicQuery dynamic query
     * @return max value of column.
     */
    @SelectProvider(type = DynamicQueryProvider.class, method = "dynamicSQL")
    Object selectAvgByDynamicQuery(
            @Param(MapperConstants.COLUMN) String column, @Param(MapperConstants.DYNAMIC_QUERY) DynamicQuery<T> dynamicQuery);

    /**
     * Select sum value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get sum value
     * @param dynamicQuery        dynamic query.
     * @return sum value of property.
     */
    default <R extends Comparable> Optional<BigDecimal> selectAvgByDynamicQueryInternal(
            GetPropertyFunction<T, R> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        String propertyName = CommonsHelper.getPropertyName(getPropertyFunction);
        Class entityClass = dynamicQuery.getEntityClass();
        String queryColumn = QUERY_HELPER.getQueryColumnByProperty(entityClass, propertyName);
        Object result = selectAvgByDynamicQuery(queryColumn, dynamicQuery);
        return Optional.ofNullable(TypeHelper.getBigDecimal(result));
    }

    /**
     * Select sum value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get sum value
     * @param dynamicQuery        dynamic query.
     * @return sum value of property.
     */
    default Optional<BigDecimal> selectAvgByDynamicQuery(
            GetBigDecimalPropertyFunction<T> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        return selectAvgByDynamicQueryInternal(getPropertyFunction, dynamicQuery);
    }

    /**
     * Select sum value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get sum value
     * @param dynamicQuery        dynamic query.
     * @return sum value of property.
     */
    default Optional<BigDecimal> selectAvgByDynamicQuery(
            GetBytePropertyFunction<T> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        return selectAvgByDynamicQueryInternal(getPropertyFunction, dynamicQuery);
    }

    /**
     * Select sum value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get sum value
     * @param dynamicQuery        dynamic query.
     * @return sum value of property.
     */
    default Optional<BigDecimal> selectAvgByDynamicQuery(
            GetDoublePropertyFunction<T> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        return selectAvgByDynamicQueryInternal(getPropertyFunction, dynamicQuery);
    }

    /**
     * Select sum value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get sum value
     * @param dynamicQuery        dynamic query.
     * @return sum value of property.
     */
    default Optional<BigDecimal> selectAvgByDynamicQuery(
            GetFloatPropertyFunction<T> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        return selectAvgByDynamicQueryInternal(getPropertyFunction, dynamicQuery);
    }

    /**
     * Select sum value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get sum value
     * @param dynamicQuery        dynamic query.
     * @return sum value of property.
     */
    default Optional<BigDecimal> selectAvgByDynamicQuery(
            GetIntegerPropertyFunction<T> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        return selectAvgByDynamicQueryInternal(getPropertyFunction, dynamicQuery);
    }

    /**
     * Select avg value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get avg value
     * @param dynamicQuery        dynamic query.
     * @return avg value of property.
     */
    default Optional<BigDecimal> selectAvgByDynamicQuery(
            GetLongPropertyFunction<T> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        return selectAvgByDynamicQueryInternal(getPropertyFunction, dynamicQuery);
    }

    /**
     * Select avg value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get avg value
     * @param dynamicQuery        dynamic query.
     * @return avg value of property.
     */
    default Optional<BigDecimal> selectAvgByDynamicQuery(
            GetShortPropertyFunction<T> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        return selectAvgByDynamicQueryInternal(getPropertyFunction, dynamicQuery);
    }
}
