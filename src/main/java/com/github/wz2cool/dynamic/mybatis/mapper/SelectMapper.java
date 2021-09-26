package com.github.wz2cool.dynamic.mybatis.mapper;

import com.github.wz2cool.dynamic.DynamicQuery;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicQueryProvider;
import org.apache.ibatis.annotations.SelectProvider;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface SelectMapper<T> {

    /**
     * 根据实体中的属性值进行查询，查询条件使用等号
     * 推荐使用{@link SelectByDynamicQueryMapper#selectByDynamicQuery(DynamicQuery)}
     *
     * @param record
     * @return
     */
    @SelectProvider(type = DynamicQueryProvider.class, method = "select")
    List<T> select(T record);

    /**
     * 根据实体中的属性进行查询，只能有一个返回值，有多个结果是抛出异常，查询条件使用等号
     * 推荐使用{@link SelectRowBoundsByDynamicQueryMapper#selectFirstByDynamicQuery(DynamicQuery)}
     *
     * @param record
     * @return
     */
    @SelectProvider(type = DynamicQueryProvider.class, method = "select")
    Optional<T> selectOne(T record);


    @SelectProvider(type = DynamicQueryProvider.class, method = "selectByPrimaryKey")
    Optional<T> selectByPrimaryKey(Serializable key);

    @SelectProvider(type = DynamicQueryProvider.class, method = "selectAll")
    List<T> selectAll();
}