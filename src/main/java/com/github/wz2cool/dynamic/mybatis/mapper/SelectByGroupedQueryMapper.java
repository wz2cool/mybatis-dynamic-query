package com.github.wz2cool.dynamic.mybatis.mapper;

import com.github.wz2cool.dynamic.DynamicQuery;
import com.github.wz2cool.dynamic.GroupedQuery;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicQueryProvider;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.GroupedQueryProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;
import java.util.Optional;

/**
 * @author Frank
 **/
@RegisterMapper
public interface SelectByGroupedQueryMapper<TQuery, TSelect> {

    /**
     * select by grouped query
     *
     * @param groupedQuery grouped query
     * @return list of item
     */
    @SelectProvider(type = GroupedQueryProvider.class, method = "dynamicSQL")
    List<TSelect> selectByGroupedQuery(@Param(MapperConstants.GROUPED_QUERY) GroupedQuery<TQuery, TSelect> groupedQuery);

    /**
     * select row rounds by grouped query.
     *
     * @param groupedQuery grouped query
     * @param rowBounds    row bounds
     * @return the list of items
     */
    @SelectProvider(type = GroupedQueryProvider.class, method = "dynamicSQL")
    List<TSelect> selectRowBoundsByGroupedQuery(
            @Param(MapperConstants.GROUPED_QUERY) GroupedQuery<TQuery, TSelect> groupedQuery,
            RowBounds rowBounds);

    /**
     * select first record by grouped query
     *
     * @param groupedQuery grouped query
     * @return matched first record
     */
    default Optional<TSelect> selectFirstByGroupedQuery(GroupedQuery<TQuery, TSelect> groupedQuery) {
        RowBounds rowBounds = new RowBounds(0, 1);
        List<TSelect> result = selectRowBoundsByGroupedQuery(groupedQuery, rowBounds);
        if (result == null || result.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(result.get(0));
        }
    }
}
