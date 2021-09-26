package com.github.wz2cool.dynamic.mybatis.mapper.provider.factory;


import java.lang.reflect.Field;

/**
 * @author wangjin
 */
public class ProviderTable {

    protected String key;

    protected Class<?> entityClass;

    protected Field[] fields;

    protected String tableName;

    protected ProviderColumn primaryKey;

    /**
     * 是否为自增ID
     */
    protected boolean isAutoIncrement;

    protected ProviderColumn[] columns;
    protected ProviderColumn[] transientColumns;

    public ProviderColumn[] getColumns() {
        return columns;
    }

    public ProviderColumn[] getTransientColumns() {
        return transientColumns;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public Field[] getFields() {
        return fields;
    }

    public String getTableName() {
        return tableName;
    }

    public ProviderColumn getPrimaryKey() {
        return primaryKey;
    }

    public String getKey() {
        return key;
    }

    public boolean isAutoIncrement() {
        return isAutoIncrement;
    }
}
