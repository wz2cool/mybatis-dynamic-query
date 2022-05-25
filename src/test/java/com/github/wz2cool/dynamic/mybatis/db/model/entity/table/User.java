package com.github.wz2cool.dynamic.mybatis.db.model.entity.table;

import javax.persistence.*;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Frank
 * \* Date: 8/7/2017
 * \* Time: 5:37 PM
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Table(name = "users")
public class User {

    @Id
    @Column
    @JoinTable(name = "")
    private Integer id;

    @Column
    private String userName;

    @Column
    private String password;

    @Transient
    private String uselessProperty;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUselessProperty() {
        return uselessProperty;
    }

    public void setUselessProperty(String uselessProperty) {
        this.uselessProperty = uselessProperty;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", uselessProperty='" + uselessProperty + '\'' +
                '}';
    }
}