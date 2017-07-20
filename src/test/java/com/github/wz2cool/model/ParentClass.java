package com.github.wz2cool.model;

import com.github.wz2cool.dynamic.mybatis.annotation.QueryColumn;

/**
 * Created by Frank on 7/11/2017.
 */
public class ParentClass {
    @QueryColumn(name = "test.parent_p1")
    private String parentP1;

    public String getParentP1() {
        return parentP1;
    }

    public void setParentP1(String parentP1) {
        this.parentP1 = parentP1;
    }
}
