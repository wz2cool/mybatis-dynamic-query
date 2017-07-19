package com.frwan.query.dynamic.mybatis.db.model.entity.view;

import com.frwan.query.dynamic.mybatis.annotation.QueryColumn;

import java.math.BigDecimal;

/**
 * Created by Frank on 2017/7/15.
 */
public class ProductView {
    @QueryColumn(name = "product.product_id")
    private Long productID;
    @QueryColumn(name = "product.product_name")
    private String productName;
    @QueryColumn(name = "product.price")
    private BigDecimal price;

    @QueryColumn(name = "category.category_id")
    private Long categoryID;
    @QueryColumn(name = "category.category_name")
    private String categoryName;
    @QueryColumn(name = "category.description")
    private String description;

    public Long getProductID() {
        return productID;
    }

    public void setProductID(Long productID) {
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

    public Long getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Long categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
