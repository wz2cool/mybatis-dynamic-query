package com.github.wz2cool.dynamic.model;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Created by Frank on 7/10/2017.
 */
@Table(name = "student")
public class Student {
    private long serialId = 123456;
    private String name;
    private Integer age;
    @Column(name = "note", table = "queryColumn")
    private String note;

    private boolean deleted;

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

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
