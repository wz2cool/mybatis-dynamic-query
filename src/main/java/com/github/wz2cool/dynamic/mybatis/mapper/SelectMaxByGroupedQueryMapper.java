package com.github.wz2cool.dynamic.mybatis.mapper;

import com.github.wz2cool.dynamic.GroupedQuery;
import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.lambda.*;
import com.github.wz2cool.dynamic.mybatis.QueryHelper;
import com.github.wz2cool.dynamic.mybatis.TypeHelper;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.GroupedQueryProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Frank
 **/
@RegisterMapper
@SuppressWarnings("java:S119")
public interface SelectMaxByGroupedQueryMapper<T> {

    QueryHelper QUERY_HELPER = new QueryHelper();

    /**
     * Select max value of column by dynamic query.
     *
     * @param column       the column need get max value
     * @param groupedQuery grouped query
     * @return max value of column.
     */
    @SelectProvider(type = GroupedQueryProvider.class, method = "dynamicSQL")
    <TSelect extends Comparable> List<Object> selectMaxByGroupedQuery(
            @Param(MapperConstants.COLUMN) String column,
            @Param(MapperConstants.GROUPED_QUERY) GroupedQuery<T, TSelect> groupedQuery);


    default <TSelect extends Comparable> List<Object> selectMaxByGroupedQueryInternal(
            GetPropertyFunction<T, TSelect> getPropertyFunction, GroupedQuery<T, TSelect> groupedQuery) {
        String propertyName = CommonsHelper.getPropertyName(getPropertyFunction);
        Class<T> queryClass = groupedQuery.getQueryClass();
        String queryColumn = QUERY_HELPER.getQueryColumnByProperty(queryClass, propertyName);
        return selectMaxByGroupedQuery(queryColumn, groupedQuery);
    }

    /**
     * Select max value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get max value
     * @param groupedQuery        grouped query.
     * @return max value of property.
     */
    default List<BigDecimal> selectMaxByGroupedQuery(
            GetBigDecimalPropertyFunction<T> getPropertyFunction, GroupedQuery<T, BigDecimal> groupedQuery) {
        List<Object> objects = selectMaxByGroupedQueryInternal(getPropertyFunction, groupedQuery);
        List<BigDecimal> result = new ArrayList<>();
        if (objects.isEmpty()) {
            return result;
        }
        for (Object object : objects) {
            Optional.ofNullable(TypeHelper.getBigDecimal(object))
                    .ifPresent(result::add);
        }
        return result;
    }

    /**
     * Select max value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get max value
     * @param groupedQuery        grouped query.
     * @return max value of property.
     */
    default List<Byte> selectMaxByGroupedQuery(
            GetBytePropertyFunction<T> getPropertyFunction, GroupedQuery<T, Byte> groupedQuery) {
        List<Object> objects = selectMaxByGroupedQueryInternal(getPropertyFunction, groupedQuery);
        List<Byte> result = new ArrayList<>();
        if (objects.isEmpty()) {
            return result;
        }
        for (Object object : objects) {
            Optional.ofNullable(TypeHelper.getByte(object))
                    .ifPresent(result::add);
        }
        return result;
    }

    /**
     * Select max value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get max value
     * @param groupedQuery        grouped query.
     * @return max value of property.
     */
    default List<Date> selectMaxByGroupedQuery(
            GetDatePropertyFunction<T> getPropertyFunction, GroupedQuery<T, Date> groupedQuery) {
        List<Object> objects = selectMaxByGroupedQueryInternal(getPropertyFunction, groupedQuery);
        List<Date> result = new ArrayList<>();
        if (objects.isEmpty()) {
            return result;
        }
        for (Object object : objects) {
            Optional.ofNullable(TypeHelper.getDate(object))
                    .ifPresent(result::add);
        }
        return result;
    }

    /**
     * Select max value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get max value
     * @param groupedQuery        grouped query.
     * @return max value of property.
     */
    default List<Double> selectMaxByGroupedQuery(
            GetDoublePropertyFunction<T> getPropertyFunction, GroupedQuery<T, Double> groupedQuery) {
        List<Object> objects = selectMaxByGroupedQueryInternal(getPropertyFunction, groupedQuery);
        List<Double> result = new ArrayList<>();
        if (objects.isEmpty()) {
            return result;
        }
        for (Object object : objects) {
            Optional.ofNullable(TypeHelper.getDouble(object))
                    .ifPresent(result::add);
        }
        return result;
    }

    /**
     * Select max value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get max value
     * @param groupedQuery        grouped query.
     * @return max value of property.
     */
    default List<Float> selectMaxByGroupedQuery(
            GetFloatPropertyFunction<T> getPropertyFunction, GroupedQuery<T, Float> groupedQuery) {
        List<Object> objects = selectMaxByGroupedQueryInternal(getPropertyFunction, groupedQuery);
        List<Float> result = new ArrayList<>();
        if (objects.isEmpty()) {
            return result;
        }
        for (Object object : objects) {
            Optional.ofNullable(TypeHelper.getFloat(object))
                    .ifPresent(result::add);
        }
        return result;
    }

    /**
     * Select max value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get max value
     * @param groupedQuery        grouped query.
     * @return max value of property.
     */
    default List<Integer> selectMaxByGroupedQuery(
            GetIntegerPropertyFunction<T> getPropertyFunction, GroupedQuery<T, Integer> groupedQuery) {
        List<Object> objects = selectMaxByGroupedQueryInternal(getPropertyFunction, groupedQuery);
        List<Integer> result = new ArrayList<>();
        if (objects.isEmpty()) {
            return result;
        }
        for (Object object : objects) {
            Optional.ofNullable(TypeHelper.getInteger(object))
                    .ifPresent(result::add);
        }
        return result;
    }

    /**
     * Select max value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get max value
     * @param groupedQuery        grouped query.
     * @return max value of property.
     */
    default List<Long> selectMaxByGroupedQuery(
            GetLongPropertyFunction<T> getPropertyFunction, GroupedQuery<T, Long> groupedQuery) {
        List<Object> objects = selectMaxByGroupedQueryInternal(getPropertyFunction, groupedQuery);
        List<Long> result = new ArrayList<>();
        if (objects.isEmpty()) {
            return result;
        }
        for (Object object : objects) {
            Optional.ofNullable(TypeHelper.getLong(object))
                    .ifPresent(result::add);
        }
        return result;
    }

    /**
     * Select max value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get max value
     * @param groupedQuery        grouped query.
     * @return max value of property.
     */
    default List<Short> selectMaxByGroupedQuery(
            GetShortPropertyFunction<T> getPropertyFunction, GroupedQuery<T, Short> groupedQuery) {
        List<Object> objects = selectMaxByGroupedQueryInternal(getPropertyFunction, groupedQuery);
        List<Short> result = new ArrayList<>();
        if (objects.isEmpty()) {
            return result;
        }
        for (Object object : objects) {
            Optional.ofNullable(TypeHelper.getShort(object))
                    .ifPresent(result::add);
        }
        return result;
    }

    /**
     * Select max value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get max value
     * @param groupedQuery        grouped query.
     * @return max value of property.
     */
    default List<String> selectMaxByGroupedQuery(
            GetStringPropertyFunction<T> getPropertyFunction, GroupedQuery<T, String> groupedQuery) {
        List<Object> objects = selectMaxByGroupedQueryInternal(getPropertyFunction, groupedQuery);
        List<String> result = new ArrayList<>();
        if (objects.isEmpty()) {
            return result;
        }
        for (Object object : objects) {
            Optional.ofNullable(TypeHelper.getString(object))
                    .ifPresent(result::add);
        }
        return result;
    }
}
