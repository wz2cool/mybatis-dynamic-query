package com.frwan.query.dynamic.mybatis.db.mapper;

import com.frwan.query.dynamic.mybatis.db.model.entity.table.Category;
import com.frwan.query.dynamic.mybatis.db.model.entity.table.Product;
import com.frwan.query.dynamic.mybatis.db.model.entity.view.ProductView;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Created by Frank on 2017/7/16.
 */
@Mapper
public interface NorthwindDao {
    List<Category> getCategories();

    List<Product> getProducts();

    List<Product> getProductByDynamic(Map<String, Object> params);

    List<ProductView> getProductViewsByDynamic(Map<String, Object> params);
}
