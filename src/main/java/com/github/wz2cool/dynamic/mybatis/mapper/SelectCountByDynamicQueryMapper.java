package com.github.wz2cool.dynamic.mybatis.mapper;

import com.github.wz2cool.dynamic.FilterDescriptorBase;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperContant;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicQueryProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Frank
 * \* Date: 8/8/2017
 * \* Time: 5:39 PM
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public interface SelectCountByDynamicQueryMapper<T> {

    @SelectProvider(type = DynamicQueryProvider.class, method = "dynamicSQL")
    long selectCountByDynamicQuery(@Param(MapperContant.ENTITY_CLASS) Class<T> entityClass,
                                   @Param(MapperContant.FILTERS) FilterDescriptorBase... filters);
}