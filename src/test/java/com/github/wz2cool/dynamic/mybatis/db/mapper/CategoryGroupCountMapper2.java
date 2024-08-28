package com.github.wz2cool.dynamic.mybatis.db.mapper;

import com.github.wz2cool.dynamic.mybatis.db.model.entity.group.CategoryGroupCount;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.view.ProductBaseView;
import com.github.wz2cool.dynamic.mybatis.mapper.SelectByGroupedQueryMapper;
import com.github.wz2cool.dynamic.mybatis.mapper.SelectMaxByGroupedQueryMapper;
import com.github.wz2cool.dynamic.mybatis.mapper.SelectMinByGroupedQueryMapper;

/**
 * @author Frank
 **/
public interface CategoryGroupCountMapper2 extends
        SelectByGroupedQueryMapper<ProductBaseView, CategoryGroupCount>,
        SelectMaxByGroupedQueryMapper<ProductBaseView>,
        SelectMinByGroupedQueryMapper<ProductBaseView> {
}
