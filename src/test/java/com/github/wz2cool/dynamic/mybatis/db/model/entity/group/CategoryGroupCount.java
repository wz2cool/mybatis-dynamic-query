package com.github.wz2cool.dynamic.mybatis.db.model.entity.group;

import javax.persistence.Column;

/**
 * @author Frank
 * @date 2020/11/28
 **/
public class CategoryGroupCount {
    public Integer categoryId;

    @Column(name = "COUNT(productId)")
    private Integer count;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
