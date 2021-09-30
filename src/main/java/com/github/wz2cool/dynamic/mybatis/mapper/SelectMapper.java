package com.github.wz2cool.dynamic.mybatis.mapper;

import com.github.wz2cool.dynamic.DynamicQuery;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicQueryProvider;
import org.apache.ibatis.annotations.SelectProvider;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * 提供基本的查询
 *
 * @param <T>
 * @author wangjin
 */
public interface SelectMapper<T> {

    /**
     * 根据实体中的属性值进行查询，查询条件默认使用等号
     * 推荐使用{@link DynamicQueryMapper#selectByDynamicQuery(DynamicQuery)}
     *
     * @param entity 实体对象
     * @return List<T>
     */
    @SelectProvider(type = DynamicQueryProvider.class, method = "select")
    List<T> select(@Nullable T entity);

    /**
     * 根据实体中的属性进行查询，只能有一个返回值，有多个结果是抛出异常，查询条件使用等号
     * 推荐使用{@link DynamicQueryMapper#selectFirstByDynamicQuery(DynamicQuery)}
     *
     * @param entity 实体对象
     * @return T
     */
    @Nullable
    @SelectProvider(type = DynamicQueryProvider.class, method = "select")
    T selectOne(T entity);

    /**
     * 根据实体中的属性进行查询，只能有一个返回值，有多个结果是抛出异常，查询条件使用等号
     * 推荐使用{@link SelectRowBoundsByDynamicQueryMapper#selectFirstByDynamicQuery(DynamicQuery)}
     *
     * @param entity 实体对象
     * @return Optional<T>
     */
    @SelectProvider(type = DynamicQueryProvider.class, method = "select")
    Optional<T> selectOneForOptional(T entity);

    /**
     * 根据主键字段进行查询，方法参数必须包含完整的主键属性{@link javax.persistence.Id}
     *
     * @param key 主键
     * @return T
     */
    @Nullable
    @SelectProvider(type = DynamicQueryProvider.class, method = "selectByPrimaryKey")
    T selectByPrimaryKey(Serializable key);

    /**
     * 根据主键字段进行查询，方法参数必须包含完整的主键属性{@link javax.persistence.Id}
     *
     * @param key 主键
     * @return Optional<T>
     */
    @SelectProvider(type = DynamicQueryProvider.class, method = "selectByPrimaryKey")
    Optional<T> selectByPrimaryKeyForOptional(Serializable key);

    /**
     * 查询所有数据
     *
     * @return List<T>
     */
    @SelectProvider(type = DynamicQueryProvider.class, method = "selectAll")
    List<T> selectAll();
}