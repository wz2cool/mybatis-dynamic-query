package com.github.wz2cool.dynamic.mybatis.mapper;

import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * @author Frank
 * @date 2020/04/18
 **/
@RegisterMapper
public interface SelectViewByDynamicQueryMapper<T> extends
        SelectCountByDynamicQueryMapper<T>,
        SelectByDynamicQueryMapper<T>,
        SelectRowBoundsByDynamicQueryMapper<T> {
}
