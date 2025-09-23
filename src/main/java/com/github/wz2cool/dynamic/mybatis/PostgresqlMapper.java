package com.github.wz2cool.dynamic.mybatis;

import com.github.wz2cool.dynamic.mybatis.mapper.postgresql.InsertIgnoreMapper;
import com.github.wz2cool.dynamic.mybatis.mapper.postgresql.InsertIgnoreSelectiveMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;

@RegisterMapper
public interface PostgresqlMapper<T> extends
        InsertIgnoreMapper<T>,
        InsertIgnoreSelectiveMapper<T> {
}
