package com.github.wz2cool.dynamic.mybatis.mapper;

import com.github.wz2cool.dynamic.DynamicQuery;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicUpdateProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;


/**
 * @author Frank
 */
public interface UpdateByDynamicQueryMapper<T> {
    /**
     * update by dynamic query.
     * 忽略更新空和null
     *
     * @param entity       entity
     * @param dynamicQuery dynamic query
     * @return effect rows
     */
    @UpdateProvider(type = DynamicUpdateProvider.class, method = "dynamicUpdateForSelective")
    int updateByDynamicQuery(
            @Param("entity") T entity,
            @Param(MapperConstants.DYNAMIC_QUERY) DynamicQuery<T> dynamicQuery);
}
