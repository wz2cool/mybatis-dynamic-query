package com.github.wz2cool.dynamic.mybatis.mapper;

import com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicDeleteProvider;
import org.apache.ibatis.annotations.DeleteProvider;


public interface DeleteMapper<T> {
    /**
     * 根据实体属性作为条件进行删除，查询条件使用等号
     *
     * @param record
     * @return
     */
    @DeleteProvider(type = DynamicDeleteProvider.class, method = "dynamicSQL")
    int delete(T record);

    /**
     * 根据主键字段进行删除，方法参数必须包含完整的主键属性
     *
     * @param key
     * @return
     */
    @DeleteProvider(type = DynamicDeleteProvider.class, method = "dynamicSQL")
    int deleteByPrimaryKey(Object key);
}