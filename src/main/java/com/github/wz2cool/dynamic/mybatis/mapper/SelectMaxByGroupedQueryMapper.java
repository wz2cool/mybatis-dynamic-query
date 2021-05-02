package com.github.wz2cool.dynamic.mybatis.mapper;

import com.github.wz2cool.dynamic.GroupedQuery;
import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.lambda.GetPropertyFunction;
import com.github.wz2cool.dynamic.mybatis.QueryHelper;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.GroupedQueryProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

/**
 * @author Frank
 **/
@RegisterMapper
@SuppressWarnings("java:S119")
public interface SelectMaxByGroupedQueryMapper<TQuery, TSelect extends Comparable> {

    QueryHelper QUERY_HELPER = new QueryHelper();

    /**
     * Select max value of column by dynamic query.
     *
     * @param column       the column need get max value
     * @param groupedQuery grouped query
     * @return max value of column.
     */
    @SelectProvider(type = GroupedQueryProvider.class, method = "dynamicSQL")
    List<TSelect> selectMaxByGroupedQuery(
            @Param(MapperConstants.COLUMN) String column,
            @Param(MapperConstants.GROUPED_QUERY) GroupedQuery<TQuery, TSelect> groupedQuery);


    default List<TSelect> selectMaxByGroupedQuery(
            GetPropertyFunction<TQuery, TSelect> getPropertyFunction, GroupedQuery<TQuery, TSelect> groupedQuery) {
        String propertyName = CommonsHelper.getPropertyName(getPropertyFunction);
        Class<TQuery> queryClass = groupedQuery.getQueryClass();
        String queryColumn = QUERY_HELPER.getQueryColumnByProperty(queryClass, propertyName);
        return selectMaxByGroupedQuery(queryColumn, groupedQuery);
    }
}
