package com.github.wz2cool.dynamic.mybatis.db.model.entity.table;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by Frank on 2017/7/15.
 */
@Table(name = "product")
public class Product {
    @Column(name = "product_id", insertable = false, updatable = false)
    private Integer productID;
    private String productName;
    private BigDecimal price;
    private Integer categoryID;
    private Timestamp createTime;

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

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
