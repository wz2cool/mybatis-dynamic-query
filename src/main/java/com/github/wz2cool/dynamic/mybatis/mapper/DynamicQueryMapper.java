package com.github.wz2cool.dynamic.mybatis.mapper;

import tk.mybatis.mapper.common.BaseMapper;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Frank
 * \* Date: 8/8/2017
 * \* Time: 3:10 PM
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
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