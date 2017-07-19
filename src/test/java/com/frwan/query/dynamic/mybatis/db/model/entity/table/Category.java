package com.frwan.query.dynamic.mybatis.db.model.entity.table;

import com.frwan.query.dynamic.mybatis.annotation.DbColumn;
import com.frwan.query.dynamic.mybatis.annotation.DbTable;

/**
 * Created by Frank on 2017/7/15.
 */
@DbTable(name = "category")
public class Category {
    @DbColumn(name = "category_id")
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
