package com.github.wz2cool.dynamic.mybatis.db.model.entity.table;

import com.github.wz2cool.dynamic.mybatis.annotation.Column;
import com.github.wz2cool.dynamic.mybatis.annotation.Table;

import java.math.BigDecimal;

/**
 * Created by Frank on 2017/7/15.
 */
@Table(name = "product")
public class Product {
    @Column(name = "product_id")
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
