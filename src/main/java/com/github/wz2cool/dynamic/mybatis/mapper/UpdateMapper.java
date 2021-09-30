
package com.github.wz2cool.dynamic.mybatis.mapper;

import com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicUpdateProvider;
import org.apache.ibatis.annotations.UpdateProvider;

/**
 * 提供基本的修改
 *
 * @param <T>
 * @author wangjin
 */
public interface UpdateMapper<T> {

    /**
     * 根据主键更新属性不为null且不为空的值
     *
     * @param entity 实体对象
     * @return 受影响的行数
     */
    @UpdateProvider(type = DynamicUpdateProvider.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(T entity);

    /**
     * 根据主键更新实体全部字段，null值空值会被更新
     *
     * @param entity 实体对象
     * @return 受影响的行数
     */
    @UpdateProvider(type = DynamicUpdateProvider.class, method = "updateByPrimaryKey")
    int updateByPrimaryKey(T entity);
}