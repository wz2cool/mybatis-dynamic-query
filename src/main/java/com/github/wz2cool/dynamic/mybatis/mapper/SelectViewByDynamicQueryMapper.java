package com.github.wz2cool.dynamic.mybatis.mapper;



/**
 * @author Frank
 **/
public interface SelectViewByDynamicQueryMapper<T> extends
        SelectCountByDynamicQueryMapper<T>,
        SelectByDynamicQueryMapper<T>,
        SelectRowBoundsByDynamicQueryMapper<T>,
        SelectMaxByDynamicQueryMapper<T>,
        SelectMinByDynamicQueryMapper<T>,
        SelectSumByDynamicQueryMapper<T>,
        SelectAvgByDynamicQueryMapper<T> {
}
