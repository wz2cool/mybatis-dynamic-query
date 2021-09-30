package com.github.wz2cool.dynamic.mybatis.mapper.provider.factory;


import javax.persistence.Column;

/**
 * @author wangjin
 */
public class ProviderColumn {
    /**
     * java字段
     */
    protected String javaColumn;
    /**
     * 数据库字段
     * {@link Column#name()}
     */
    protected String dbColumn;

    /**
     * 数据库字段对应哪个表
     * {@link Column#table()}
     */
    protected String dbColumnTable;
    /**
     * 是否为主键
     */
    protected boolean isPrimaryKey;
    /**
     * java字段的类型
     */
    protected Class<?> columnType;


    public String getJavaColumn() {
        return javaColumn;
    }

    public String getDbColumn() {
        return dbColumn;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public Class<?> getColumnType() {
        return columnType;
    }

    public String getDbColumnTable() {
        return dbColumnTable;
    }
}
