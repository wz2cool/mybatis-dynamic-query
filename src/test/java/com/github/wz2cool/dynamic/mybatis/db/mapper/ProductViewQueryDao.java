package com.github.wz2cool.dynamic.mybatis.db.mapper;

import com.github.wz2cool.dynamic.mybatis.db.model.entity.view.ProductView;
import com.github.wz2cool.dynamic.mybatis.mapper.DynamicQueryMapper;

/**
 * Test-only mapper for {@link ProductView} distinct count coverage
 * (openspec fix-multi-column-distinct-count, tasks 3.4).
 * <p>
 * Registered automatically by {@code @MapperScan} of {@code TestApplication}.
 *
 * @author Frank
 */
public interface ProductViewQueryDao extends DynamicQueryMapper<ProductView> {
}
