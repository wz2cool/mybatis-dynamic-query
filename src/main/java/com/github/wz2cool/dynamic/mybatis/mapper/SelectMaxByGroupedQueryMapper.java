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
import org.apache.ibatis.session.RowBounds;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Frank
 **/
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


    /**
     * Select max value of column by dynamic query.
     *
     * @param column       the column need get max value
     * @param groupedQuery grouped query
     * @return max value of column.
     */
    @SelectProvider(type = GroupedQueryProvider.class, method = "dynamicSQL")
    <TSelect extends Comparable> List<Object> selectMaxRowBoundsByGroupedQuery(
            @Param(MapperConstants.COLUMN) String column,
            @Param(MapperConstants.GROUPED_QUERY) GroupedQuery<T, TSelect> groupedQuery,
            RowBounds rowBounds);


    default <TSelect extends Comparable> List<Object> selectMaxByGroupedQueryInternal(
            GetPropertyFunction<T, TSelect> getPropertyFunction, GroupedQuery<T, TSelect> groupedQuery) {
        String propertyName = CommonsHelper.getPropertyName(getPropertyFunction);
        Class<T> queryClass = groupedQuery.getQueryClass();
        String queryColumn = QUERY_HELPER.getQueryColumnByProperty(queryClass, propertyName);
        return selectMaxByGroupedQuery(queryColumn, groupedQuery);
    }

    default <TSelect extends Comparable> List<Object> selectMaxByGroupedQueryInternal(
            GetPropertyFunction<T, TSelect> getPropertyFunction, GroupedQuery<T, TSelect> groupedQuery,
            RowBounds rowBounds) {
        String propertyName = CommonsHelper.getPropertyName(getPropertyFunction);
        Class<T> queryClass = groupedQuery.getQueryClass();
        String queryColumn = QUERY_HELPER.getQueryColumnByProperty(queryClass, propertyName);
        return selectMaxRowBoundsByGroupedQuery(queryColumn, groupedQuery, rowBounds);
    }

    /**
     * Select max value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get max value
     * @param groupedQuery        grouped query.
     * @param rowBounds           row bounds
     * @return max value of property.
     */
    default List<BigDecimal> selectMaxByGroupedQuery(
            GetBigDecimalPropertyFunction<T> getPropertyFunction,
            GroupedQuery<T, BigDecimal> groupedQuery,
            RowBounds rowBounds) {
        List<Object> objects = rowBounds == null ?
                selectMaxByGroupedQueryInternal(getPropertyFunction, groupedQuery) :
                selectMaxByGroupedQueryInternal(getPropertyFunction, groupedQuery, rowBounds);
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
            GetBytePropertyFunction<T> getPropertyFunction, GroupedQuery<T, Byte> groupedQuery,
            RowBounds rowBounds) {
        List<Object> objects = rowBounds == null ?
                selectMaxByGroupedQueryInternal(getPropertyFunction, groupedQuery) :
                selectMaxByGroupedQueryInternal(getPropertyFunction, groupedQuery, rowBounds);
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
    default List<Byte> selectMaxByGroupedQuery(
            GetBytePropertyFunction<T> getPropertyFunction, GroupedQuery<T, Byte> groupedQuery) {
        return selectMaxByGroupedQuery(getPropertyFunction, groupedQuery, null);
    }

    /**
     * Select max value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get max value
     * @param groupedQuery        grouped query.
     * @param rowBounds           row bounds
     * @return max value of property.
     */
    default List<Date> selectMaxByGroupedQuery(
            GetDatePropertyFunction<T> getPropertyFunction, GroupedQuery<T, Date> groupedQuery,
            RowBounds rowBounds) {
        List<Object> objects = rowBounds == null ?
                selectMaxByGroupedQueryInternal(getPropertyFunction, groupedQuery) :
                selectMaxByGroupedQueryInternal(getPropertyFunction, groupedQuery, rowBounds);
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
    default List<Date> selectMaxByGroupedQuery(
            GetDatePropertyFunction<T> getPropertyFunction, GroupedQuery<T, Date> groupedQuery) {
        return selectMaxByGroupedQuery(getPropertyFunction, groupedQuery, null);
    }

    /**
     * Select max value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get max value
     * @param groupedQuery        grouped query.
     * @param rowBounds           row bounds
     * @return max value of property.
     */
    default List<Double> selectMaxByGroupedQuery(
            GetDoublePropertyFunction<T> getPropertyFunction, GroupedQuery<T, Double> groupedQuery,
            RowBounds rowBounds) {
        List<Object> objects = rowBounds == null ?
                selectMaxByGroupedQueryInternal(getPropertyFunction, groupedQuery) :
                selectMaxByGroupedQueryInternal(getPropertyFunction, groupedQuery, rowBounds);
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
    default List<Double> selectMaxByGroupedQuery(
            GetDoublePropertyFunction<T> getPropertyFunction, GroupedQuery<T, Double> groupedQuery) {
        return selectMaxByGroupedQuery(getPropertyFunction, groupedQuery);
    }

    /**
     * Select max value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get max value
     * @param groupedQuery        grouped query.
     * @param rowBounds           row bounds
     * @return max value of property.
     */
    default List<Float> selectMaxByGroupedQuery(
            GetFloatPropertyFunction<T> getPropertyFunction, GroupedQuery<T, Float> groupedQuery,
            RowBounds rowBounds) {
        List<Object> objects = rowBounds == null ?
                selectMaxByGroupedQueryInternal(getPropertyFunction, groupedQuery) :
                selectMaxByGroupedQueryInternal(getPropertyFunction, groupedQuery, rowBounds);
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
    default List<Float> selectMaxByGroupedQuery(
            GetFloatPropertyFunction<T> getPropertyFunction, GroupedQuery<T, Float> groupedQuery) {
        return selectMaxByGroupedQuery(getPropertyFunction, groupedQuery, null);
    }

    /**
     * Select max value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get max value
     * @param groupedQuery        grouped query.
     * @return max value of property.
     */
    default List<Integer> selectMaxByGroupedQuery(
            GetIntegerPropertyFunction<T> getPropertyFunction, GroupedQuery<T, Integer> groupedQuery,
            RowBounds rowBounds) {
        List<Object> objects = rowBounds == null ?
                selectMaxByGroupedQueryInternal(getPropertyFunction, groupedQuery) :
                selectMaxByGroupedQueryInternal(getPropertyFunction, groupedQuery, rowBounds);
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
    default List<Integer> selectMaxByGroupedQuery(
            GetIntegerPropertyFunction<T> getPropertyFunction, GroupedQuery<T, Integer> groupedQuery) {
        return selectMaxByGroupedQuery(getPropertyFunction, groupedQuery, null);
    }

    /**
     * Select max value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get max value
     * @param groupedQuery        grouped query.
     * @return max value of property.
     */
    default List<Long> selectMaxByGroupedQuery(
            GetLongPropertyFunction<T> getPropertyFunction, GroupedQuery<T, Long> groupedQuery,
            RowBounds rowBounds) {
        List<Object> objects = rowBounds == null ?
                selectMaxByGroupedQueryInternal(getPropertyFunction, groupedQuery) :
                selectMaxByGroupedQueryInternal(getPropertyFunction, groupedQuery, rowBounds);
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
    default List<Long> selectMaxByGroupedQuery(
            GetLongPropertyFunction<T> getPropertyFunction, GroupedQuery<T, Long> groupedQuery) {
        return selectMaxByGroupedQuery(getPropertyFunction, groupedQuery, null);
    }

    /**
     * Select max value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get max value
     * @param groupedQuery        grouped query.
     * @param rowBounds           row bonds
     * @return max value of property.
     */
    default List<Short> selectMaxByGroupedQuery(
            GetShortPropertyFunction<T> getPropertyFunction, GroupedQuery<T, Short> groupedQuery,
            RowBounds rowBounds) {
        List<Object> objects = rowBounds == null ?
                selectMaxByGroupedQueryInternal(getPropertyFunction, groupedQuery) :
                selectMaxByGroupedQueryInternal(getPropertyFunction, groupedQuery, rowBounds);
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
    default List<Short> selectMaxByGroupedQuery(
            GetShortPropertyFunction<T> getPropertyFunction, GroupedQuery<T, Short> groupedQuery) {
        return selectMaxByGroupedQuery(getPropertyFunction, groupedQuery, null);
    }

    /**
     * Select max value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get max value
     * @param groupedQuery        grouped query.
     * @param rowBounds           row bounds
     * @return max value of property.
     */
    default List<String> selectMaxByGroupedQuery(
            GetStringPropertyFunction<T> getPropertyFunction, GroupedQuery<T, String> groupedQuery,
            RowBounds rowBounds) {
        List<Object> objects = rowBounds == null ?
                selectMaxByGroupedQueryInternal(getPropertyFunction, groupedQuery) :
                selectMaxByGroupedQueryInternal(getPropertyFunction, groupedQuery, rowBounds);
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

    /**
     * Select max value of property by dynamic query.
     *
     * @param getPropertyFunction the property need get max value
     * @param groupedQuery        grouped query.
     * @return max value of property.
     */
    default List<String> selectMaxByGroupedQuery(
            GetStringPropertyFunction<T> getPropertyFunction, GroupedQuery<T, String> groupedQuery) {
        return selectMaxByGroupedQuery(getPropertyFunction, groupedQuery, null);
    }
}
