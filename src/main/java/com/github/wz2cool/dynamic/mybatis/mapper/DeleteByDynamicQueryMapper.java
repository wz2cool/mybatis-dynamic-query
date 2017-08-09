package com.github.wz2cool.dynamic.mybatis.mapper;

import com.github.wz2cool.dynamic.FilterDescriptorBase;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperContant;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicQueryProvider;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Param;

public interface DeleteByDynamicQueryMapper<T> {
    @DeleteProvider(type = DynamicQueryProvider.class, method = "dynamicSQL")
    int deleteByDynamicQuery(@Param(MapperContant.ENTITY_CLASS) Class<T> entityClass,
                             @Param(MapperContant.FILTERS) FilterDescriptorBase... filters);
}