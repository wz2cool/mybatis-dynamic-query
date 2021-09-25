package com.github.wz2cool.dynamic.mybatis.mapper;

import com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicQueryProvider;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface SelectMapper<T> {

    /**
     * 根据实体中的属性值进行查询，查询条件使用等号
     *
     * @param record
     * @return
     */
    @SelectProvider(type = DynamicQueryProvider.class, method = "select")
    List<T> select(T record);

    /**
     * 根据实体中的属性进行查询，只能有一个返回值，有多个结果是抛出异常，查询条件使用等号
     *
     * @param record
     * @return
     */
    @SelectProvider(type = DynamicQueryProvider.class, method = "select")
    T selectOne(T record);

    /**
     * 根据主键字段进行查询，方法参数必须包含完整的主键属性，查询条件使用等号
     *
     * @param key
     * @return
     */
    @SelectProvider(type = DynamicQueryProvider.class, method = "select")
    T selectByPrimaryKey(Object key);


    /**
     * 查询全部结果
     *
     * @return
     */
    @SelectProvider(type = DynamicQueryProvider.class, method = "select")
    List<T> selectAll();


    /**
     * 根据实体中的属性查询总数，查询条件使用等号
     *
     * @param record
     * @return
     */
    @SelectProvider(type = DynamicQueryProvider.class, method = "dynamicSQL")
    int selectCount(T record);
}