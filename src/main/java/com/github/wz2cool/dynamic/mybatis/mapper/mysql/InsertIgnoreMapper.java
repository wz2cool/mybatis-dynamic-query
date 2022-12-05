package com.github.wz2cool.dynamic.mybatis.mapper.mysql;

import com.github.wz2cool.dynamic.mybatis.mapper.provider.InsertIgnoreProvider;
import org.apache.ibatis.annotations.InsertProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * insert ignore mapper (mysql)
 *
 * @author frank
 */
@RegisterMapper
public interface InsertIgnoreMapper<T> {

    /**
     * 保存一个实体，null的属性也会保存，不会使用数据库默认值
     *
     * @param record
     * @return
     */
    @InsertProvider(type = InsertIgnoreProvider.class, method = "dynamicSQL")
    int insert(T record);
}
