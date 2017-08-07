package com.github.wz2cool.model;

import javax.persistence.Column;

/**
 * Created by Frank on 7/11/2017.
 */
public class ParentClass {
    @Column(name = "parent_p1", table = "test")
    private String parentP1;

    public String getParentP1() {
        return parentP1;
    }

    public void setParentP1(String parentP1) {
        this.parentP1 = parentP1;
    }
}
