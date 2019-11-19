package com.github.wz2cool.dynamic.mybatis.mapper;

import com.github.wz2cool.dynamic.DynamicQuery;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicQueryProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;
import java.util.Optional;

/**
 * @author Frank
 */
@RegisterMapper
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

    /**
     * select first record by dynamic query
     *
     * @param dynamicQuery dynamic query
     * @return matched first record
     */
    default Optional<T> selectFirstByDynamicQuery(DynamicQuery<T> dynamicQuery) {
        RowBounds rowBounds = new RowBounds(0, 1);
        List<T> result = selectRowBoundsByDynamicQuery(dynamicQuery, rowBounds);
        if (result == null || result.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(result.get(0));
        }
    }
}