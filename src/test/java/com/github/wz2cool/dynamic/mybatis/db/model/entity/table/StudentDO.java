package com.github.wz2cool.dynamic.mybatis.db.model.entity.table;


import javax.persistence.Table;

/**
 * @author Frank
 * @date 2020/05/02
 **/
@Table(name = "student")
public class StudentDO {
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
