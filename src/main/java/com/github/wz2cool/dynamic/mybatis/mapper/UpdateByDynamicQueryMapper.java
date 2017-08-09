package com.github.wz2cool.dynamic.mybatis.mapper;

import com.github.wz2cool.dynamic.DynamicQuery;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicQueryProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;

public interface UpdateByDynamicQueryMapper<T> {
    @UpdateProvider(type = DynamicQueryProvider.class, method = "dynamicSQL")
    int updateByDynamicQuery(
            @Param("record") T record,
            @Param(MapperConstants.DYNAMIC_QUERY) DynamicQuery<T> dynamicQuery);
}
