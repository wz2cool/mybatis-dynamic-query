package com.github.wz2cool.dynamic.mybatis.mapper;

import com.github.wz2cool.dynamic.DynamicQuery;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicQueryProvider;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Param;

public interface DeleteByDynamicQueryMapper<T> {
    @DeleteProvider(type = DynamicQueryProvider.class, method = "dynamicSQL")
    int deleteByDynamicQuery(@Param(MapperConstants.DYNAMIC_QUERY) DynamicQuery dynamicQuery);
}