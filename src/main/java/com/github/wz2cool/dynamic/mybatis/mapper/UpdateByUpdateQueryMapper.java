package com.github.wz2cool.dynamic.mybatis.mapper;

import com.github.wz2cool.dynamic.UpdateQuery;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicQueryProvider;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicUpdateProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;


/**
 * @author Frank
 **/
public interface UpdateByUpdateQueryMapper<T> {

    /**
     * update selective by dynamic query.
     *
     * @param updateQuery update query
     * @return effect rows
     */
    @UpdateProvider(type = DynamicUpdateProvider.class, method = "dynamicUpdate")
    int updateByUpdateQuery(
            @Param(MapperConstants.DYNAMIC_QUERY) UpdateQuery<T> updateQuery);
}
