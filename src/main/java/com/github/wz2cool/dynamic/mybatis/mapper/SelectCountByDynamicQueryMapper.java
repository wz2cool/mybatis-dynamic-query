package com.github.wz2cool.dynamic.mybatis.mapper;

import com.github.wz2cool.dynamic.DynamicQuery;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicQueryProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;


/**
 * @author Frank
 */
public interface SelectCountByDynamicQueryMapper<T> {

    /**
     * select count by dynamic query.
     *
     * @param dynamicQuery dynamic query
     * @return the count of items
     */
    @SelectProvider(type = DynamicQueryProvider.class, method = "selectCountByDynamicQuery")
    int selectCountByDynamicQuery(@Param(MapperConstants.DYNAMIC_QUERY) DynamicQuery<T> dynamicQuery);
}