package com.github.wz2cool.dynamic.mybatis.mapper;

import com.github.wz2cool.dynamic.DynamicQuery;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicQueryProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;


import java.util.List;

/**
 * @author Frank
 */
public interface SelectByDynamicQueryMapper<T> {

    /**
     * select by dynamic query
     *
     * @param dynamicQuery dynamic query
     * @return list of item
     */
    @SelectProvider(type = DynamicQueryProvider.class, method = "dynamicSQL")
    List<T> selectByDynamicQuery(@Param(MapperConstants.DYNAMIC_QUERY) DynamicQuery<T> dynamicQuery);
}