package com.github.wz2cool.dynamic.mybatis.mapper;

import com.github.wz2cool.dynamic.DynamicQuery;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicQueryProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;

/**
 * @author Frank
 */
public interface UpdateSelectiveByDynamicQueryMapper<T> {

    /**
     * update selective by dynamic query.
     *
     * @param record       record of item
     * @param dynamicQuery dynamic query
     * @return effect rows
     */
    @UpdateProvider(type = DynamicQueryProvider.class, method = "dynamicSQL")
    int updateSelectiveByDynamicQuery(
            @Param("record") T record,
            @Param(MapperConstants.DYNAMIC_QUERY) DynamicQuery<T> dynamicQuery);
}