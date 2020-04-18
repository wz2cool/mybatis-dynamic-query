package com.github.wz2cool.dynamic.mybatis.db.mapper;

import com.github.wz2cool.dynamic.mybatis.db.model.entity.view.ProductView;
import com.github.wz2cool.dynamic.mybatis.mapper.SelectViewByDynamicQueryMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Frank
 **/
@Mapper
public interface ProductViewMapper extends SelectViewByDynamicQueryMapper<ProductView> {
}
