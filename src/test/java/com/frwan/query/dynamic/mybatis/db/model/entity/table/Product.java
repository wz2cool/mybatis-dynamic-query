package com.frwan.query.dynamic.mybatis.db.model.entity.table;

import com.frwan.query.dynamic.mybatis.annotation.DbColumn;
import com.frwan.query.dynamic.mybatis.annotation.DbTable;

import java.math.BigDecimal;

/**
 * Created by Frank on 2017/7/15.
 */
@DbTable(name = "product")
public class Product {
    @DbColumn(name = "product_id")
    private Integer productID;
    private String productName;
    private BigDecimal price;
    private Integer categoryID;

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
