package com.github.wz2cool.dynamic.mybatis.mapper;

import com.github.wz2cool.dynamic.DynamicQuery;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicDeleteProvider;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicQueryProvider;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Param;


/**
 * @author Frank
 */
public interface DeleteByDynamicQueryMapper<T> {
    /**
     * delete by dynamic query.
     *
     * @param dynamicQuery dynamic query
     * @return effect rows
     */
    @DeleteProvider(type = DynamicDeleteProvider.class, method = "deleteByDynamicQuery")
    int deleteByDynamicQuery(@Param(MapperConstants.DYNAMIC_QUERY) DynamicQuery<T> dynamicQuery);
}