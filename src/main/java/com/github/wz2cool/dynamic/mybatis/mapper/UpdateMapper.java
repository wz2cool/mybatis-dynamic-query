
package com.github.wz2cool.dynamic.mybatis.mapper;

import com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicUpdateProvider;
import org.apache.ibatis.annotations.UpdateProvider;


public interface UpdateMapper<T> {

    /**
     * 根据主键更新属性不为null且不为空的值
     *
     * @param record
     * @return
     */
    @UpdateProvider(type = DynamicUpdateProvider.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(T record);


    /**
     * 根据主键更新实体全部字段，null值空值会被更新
     *
     * @param record
     * @return
     */
    @UpdateProvider(type = DynamicUpdateProvider.class, method = "updateByPrimaryKey")
    int updateByPrimaryKey(T record);
}