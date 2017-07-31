package com.github.wz2cool.dynamic.mybatis.db.model.entity.table;

import com.github.wz2cool.dynamic.mybatis.annotation.Column;
import com.github.wz2cool.dynamic.mybatis.annotation.Table;

import java.math.BigDecimal;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Frank
 * \* Date: 7/31/2017
 * \* Time: 3:42 PM
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Table(name = "product")
public class Product3 {
    // id 为null 的时候不插入
    @Column(name = "product_id")
    private Integer productID;
    private String productName;
    @Column(updateIfNull = true)
    private BigDecimal price;
    private Integer categoryID;

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

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }
}