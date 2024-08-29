package com.github.wz2cool.dynamic.mybatis.db.model.entity.view;

import com.github.wz2cool.dynamic.mybatis.View;

import javax.persistence.Column;
import java.math.BigDecimal;

/**
 * Created by Frank on 2017/7/15.
 */
@View("${product_base_view}")
public class ProductBaseView {
    @Column(name = "product_id", insertable = false, updatable = false)
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer categoryId;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
