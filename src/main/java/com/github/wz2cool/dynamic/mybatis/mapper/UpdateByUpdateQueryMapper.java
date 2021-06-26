package com.github.wz2cool.dynamic.mybatis.mapper;

import com.github.wz2cool.dynamic.UpdateQuery;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicQueryProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * @author Frank
 * @date 2021/06/26
 **/
@RegisterMapper
public interface UpdateByUpdateQueryMapper<T> {

    /**
     * update selective by dynamic query.
     *
     * @param updateQuery update query
     * @return effect rows
     */
    @UpdateProvider(type = DynamicQueryProvider.class, method = "dynamicSQL")
    int updateByUpdateQuery(
            @Param(MapperConstants.DYNAMIC_QUERY) UpdateQuery<T> updateQuery);
}
