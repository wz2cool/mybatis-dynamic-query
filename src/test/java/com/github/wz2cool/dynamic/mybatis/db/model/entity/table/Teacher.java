package com.github.wz2cool.dynamic.mybatis.db.model.entity.table;


import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author wangjin
 **/
@Table(name = "teacher")
public class Teacher {

    @Id
    @Column
    private Long id;
    private String name;


    public Integer getDeleted() {
        return deleted;
    }

    public Teacher setDeleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }

    private Integer deleted;

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
