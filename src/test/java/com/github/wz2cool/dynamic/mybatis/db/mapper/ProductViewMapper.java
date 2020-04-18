package com.github.wz2cool.dynamic.mybatis.db.mapper;

import com.github.wz2cool.dynamic.mybatis.db.model.entity.view.ProductView;
import com.github.wz2cool.dynamic.mybatis.mapper.DynamicQueryMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Frank
 * @date 2020/04/18
 **/
@Mapper
public interface ProductViewMapper extends DynamicQueryMapper<ProductView> {
}
