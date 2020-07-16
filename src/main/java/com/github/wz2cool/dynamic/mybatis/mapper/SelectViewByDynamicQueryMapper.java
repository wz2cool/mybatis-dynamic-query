package com.github.wz2cool.dynamic.mybatis.mapper;

import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * @author Frank
 **/
@RegisterMapper
public interface SelectViewByDynamicQueryMapper<T> extends
        SelectCountByDynamicQueryMapper<T>,
        SelectByDynamicQueryMapper<T>,
        SelectRowBoundsByDynamicQueryMapper<T>,
        SelectMaxByDynamicQueryMapper<T>,
        SelectMinByDynamicQueryMapper<T>,
        SelectSumByDynamicQueryMapper<T>,
        SelectAvgByDynamicQueryMapper<T> {
}
