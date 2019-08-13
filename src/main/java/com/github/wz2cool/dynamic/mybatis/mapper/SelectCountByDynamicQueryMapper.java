package com.github.wz2cool.dynamic.mybatis.mapper;

import com.github.wz2cool.dynamic.DynamicQuery;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicQueryProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * @author Frank
 */
@RegisterMapper
public interface SelectCountByDynamicQueryMapper<T> {

    /**
     * select count by dynamic query.
     *
     * @param dynamicQuery dynamic query
     * @return the count of items
     */
    @SelectProvider(type = DynamicQueryProvider.class, method = "dynamicSQL")
    int selectCountByDynamicQuery(@Param(MapperConstants.DYNAMIC_QUERY) DynamicQuery<T> dynamicQuery);
}