package com.github.wz2cool.dynamic.mybatis.mapper;

import com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicInsertProvider;
import org.apache.ibatis.annotations.InsertProvider;

public interface InsertMapper<T> {

    /**
     * 保存一个实体，null的属性也会保存，不会使用数据库默认值
     *
     * @param record
     * @return
     */
    @InsertProvider(type = DynamicInsertProvider.class, method = "insert")
    int insert(T record);


    /**
     * 保存一个实体，null的属性不会保存，会使用数据库默认值
     *
     * @param record
     * @return
     */
    @InsertProvider(type = DynamicInsertProvider.class, method = "insertSelective")
    int insertSelective(T record);

}