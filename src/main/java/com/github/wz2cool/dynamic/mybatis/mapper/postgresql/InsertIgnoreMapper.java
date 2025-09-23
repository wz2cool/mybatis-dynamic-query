package com.github.wz2cool.dynamic.mybatis.mapper.postgresql;

import com.github.wz2cool.dynamic.mybatis.mapper.provider.InsertIgnorePostgresqlProvider;
import org.apache.ibatis.annotations.InsertProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * insert ignore mapper ([postgresql])
 *
 * @author zfx
 */
@RegisterMapper
public interface InsertIgnoreMapper<T> {

    /**
     * 保存一个实体，null的属性也会保存，不会使用数据库默认值
     *
     * @param record
     * @return
     */
    @InsertProvider(type = InsertIgnorePostgresqlProvider.class, method = "dynamicSQL")
    int insertIgnore(T record);
}
