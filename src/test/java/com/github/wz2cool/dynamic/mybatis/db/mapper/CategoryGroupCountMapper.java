package com.github.wz2cool.dynamic.mybatis.db.mapper;

import com.github.wz2cool.dynamic.mybatis.db.model.entity.group.CategoryGroupCount;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.table.Product;
import com.github.wz2cool.dynamic.mybatis.mapper.SelectByGroupedQueryMapper;
import com.github.wz2cool.dynamic.mybatis.mapper.SelectMaxByGroupedQueryMapper;

import java.math.BigDecimal;

/**
 * @author Frank
 * @date 2020/11/28
 **/
public interface CategoryGroupCountMapper extends
        SelectByGroupedQueryMapper<Product, CategoryGroupCount>,
        SelectMaxByGroupedQueryMapper<Product> {
}
