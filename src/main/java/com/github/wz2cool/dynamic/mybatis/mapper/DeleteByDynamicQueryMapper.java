package com.github.wz2cool.dynamic.mybatis.mapper;

import com.github.wz2cool.dynamic.DynamicQuery;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicQueryProvider;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * @author Frank
 */
@RegisterMapper
public interface DeleteByDynamicQueryMapper<T> {
    /**
     * delete by dynamic query.
     *
     * @param dynamicQuery dynamic query
     * @return effect rows
     */
    @DeleteProvider(type = DynamicQueryProvider.class, method = "dynamicSQL")
    int deleteByDynamicQuery(@Param(MapperConstants.DYNAMIC_QUERY) DynamicQuery<T> dynamicQuery);
}