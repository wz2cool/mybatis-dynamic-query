package com.github.wz2cool.dynamic.mybatis.db.model.entity.table;

import com.github.wz2cool.dynamic.mybatis.annotation.Column;
import com.github.wz2cool.dynamic.mybatis.annotation.Table;

/**
 * Created by Frank on 2017/7/15.
 */
@Table(name = "category")
public class Category {
    @Column(name = "category_id")
    private Integer categoryID;
    private String categoryName;
    private String description;

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
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
