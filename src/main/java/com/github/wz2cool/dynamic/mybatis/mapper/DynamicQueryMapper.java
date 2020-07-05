package com.github.wz2cool.dynamic.mybatis.mapper;

import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.special.InsertListMapper;


/**
 * @author Frank
 */
@RegisterMapper
public interface DynamicQueryMapper<T> extends
        BaseMapper<T>,
        InsertListMapper<T>,
        SelectCountByDynamicQueryMapper<T>,
        DeleteByDynamicQueryMapper<T>,
        SelectByDynamicQueryMapper<T>,
        SelectRowBoundsByDynamicQueryMapper<T>,
        UpdateSelectiveByDynamicQueryMapper<T>,
        UpdateByDynamicQueryMapper<T>,
        SelectMaxByDynamicQueryMapper<T>,
        SelectMinByDynamicQueryMapper<T> {
}