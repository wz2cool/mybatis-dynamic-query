package com.github.wz2cool.model;

import com.github.wz2cool.dynamic.mybatis.annotation.Column;
import com.github.wz2cool.dynamic.mybatis.annotation.Table;

/**
 * Created by Frank on 7/10/2017.
 */
@Table(name = "student")
public class Student {
    private long serialId = 123456;
    private String name;
    private Integer age;
    @Column(name = "note", tableOrAlias = "queryColumn", updateIfNull = true)
    private String note;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public long getSerialId() {
        return serialId;
    }
}
