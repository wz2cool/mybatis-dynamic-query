package com.github.wz2cool.dynamic.mybatis.mapper;

import tk.mybatis.mapper.common.BaseMapper;


/**
 * @author Frank
 */
public interface DynamicQueryMapper<T> extends
        BaseMapper<T>,
        SelectCountByDynamicQueryMapper<T>,
        DeleteByDynamicQueryMapper<T>,
        SelectByDynamicQueryMapper<T>,
        SelectRowBoundsByDynamicQueryMapper<T>,
        UpdateSelectiveByDynamicQueryMapper<T>,
        UpdateByDynamicQueryMapper<T> {
}