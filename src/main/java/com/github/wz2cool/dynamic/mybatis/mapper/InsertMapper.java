package com.github.wz2cool.dynamic.mybatis.mapper;

import com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicInsertProvider;
import org.apache.ibatis.annotations.InsertProvider;

import java.util.List;

/**
 * 提供基本的增加
 *
 * @param <T>
 * @author wangjin
 */
public interface InsertMapper<T> {

    /**
     * 保存一个实体，null和空的属性不会保存，会使用数据库默认值
     *
     * @param entity 实体对象
     * @return 受影响的行数
     */
    @InsertProvider(type = DynamicInsertProvider.class, method = "insertSelective")
    int insertSelective(T entity);

    /**
     * 保存一个实体，null和空的属性也会保存，不会使用数据库默认值
     *
     * @param entity 实体对象
     * @return 受影响的行数
     */
    @InsertProvider(type = DynamicInsertProvider.class, method = "insert")
    int insert(T entity);

    @InsertProvider(type = DynamicInsertProvider.class, method = "insertList")
    int insertList(List<T> entity);
}