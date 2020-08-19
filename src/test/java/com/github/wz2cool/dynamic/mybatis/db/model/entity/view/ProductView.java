package com.github.wz2cool.dynamic.mybatis.db.model.entity.view;

import com.github.wz2cool.dynamic.mybatis.View;

import javax.persistence.Column;
import java.math.BigDecimal;

/**
 * Created by Frank on 2017/7/15.
 */
@View("product LEFT JOIN category ON product.category_id = category.category_id")
public class ProductView {
    @Column(name = "product_id", table = "product")
    private Long productID;
    @Column(name = "product_name", table = "product")
    private String productName;
    @Column(name = "price", table = "product")
    private BigDecimal price;

    @Column(name = "category_id", table = "category")
    private Long categoryID;
    @Column(name = "category_name", table = "category")
    private String categoryName;
    @Column(name = "description", table = "category")
    private String description;
    @Column(name = "description", table = "product")
    private String productDescription;

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

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
}
