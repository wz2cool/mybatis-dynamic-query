package com.github.wz2cool.dynamic.mybatis.mapper;

import com.github.wz2cool.dynamic.GroupedQuery;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.GroupedQueryProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

/**
 * @author Frank
 * @date 2020/11/28
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
}
