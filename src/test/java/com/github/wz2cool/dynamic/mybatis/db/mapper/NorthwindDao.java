package com.github.wz2cool.dynamic.mybatis.db.mapper;

import com.github.wz2cool.dynamic.mybatis.db.model.entity.table.Category;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.table.Product;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.view.ProductView;
import org.apache.ibatis.annotations.Mapper;
import org.omg.CORBA.OBJ_ADAPTER;

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

    int insert(Map<String, Object> params);

    int update(Map<String, Object> params);

    int delete(Map<String, Object> params);
}
