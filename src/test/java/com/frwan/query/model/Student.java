package com.frwan.query.model;

import com.frwan.query.dynamic.mybatis.annotation.DbColumn;
import com.frwan.query.dynamic.mybatis.annotation.DbTable;
import com.frwan.query.dynamic.mybatis.annotation.QueryColumn;

/**
 * Created by Frank on 7/10/2017.
 */
@DbTable(name = "student")
public class Student {
    private long serialId = 123456;
    private String name;
    private Integer age;
    @QueryColumn(name = "queryColumn.note")
    @DbColumn(name = "dbColumn.note", updateIfNull = true)
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
