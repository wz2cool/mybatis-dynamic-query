package com.github.wz2cool.dynamic.mybatis.mapper;

import com.github.wz2cool.dynamic.DynamicQuery;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicQueryProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * @author Frank
 */
public interface SelectRowBoundsByDynamicQueryMapper<T> {

    /**
     * select row rounds by dynamic query.
     *
     * @param dynamicQuery dynamic query
     * @param rowBounds    row bounds
     * @return the list of items
     */
    @SelectProvider(type = DynamicQueryProvider.class, method = "dynamicSQL")
    List<T> selectRowBoundsByDynamicQuery(
            @Param(MapperConstants.DYNAMIC_QUERY) DynamicQuery<T> dynamicQuery,
            RowBounds rowBounds);
}