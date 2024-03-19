package com.github.wz2cool.dynamic.mybatis.mapper;

import com.github.wz2cool.dynamic.mybatis.mapper.mysql.InsertIgnoreMapper;
import com.github.wz2cool.dynamic.mybatis.mapper.mysql.InsertIgnoreSelectiveMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;

@RegisterMapper
public interface MySqlMapper<T> extends
        InsertIgnoreMapper<T>,
        InsertIgnoreSelectiveMapper<T> {
}
