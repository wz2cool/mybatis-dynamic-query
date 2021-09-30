package com.github.wz2cool.dynamic.mybatis.mapper.provider.factory;


import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Map;

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
     * 空间换时间. key为{@link ProviderColumn#javaColumn}
     */
    protected Map<String, ProviderColumn> columnHash;
    /**
     * 是否为自增ID
     */
    protected boolean isAutoIncrement;
    protected ProviderColumn[] columns;
    protected ProviderColumn[] transientColumns;


    public ProviderColumn getProviderColumn(final String javaColumn) {
        return columnHash.get(javaColumn);
    }

    public boolean containsProviderColumn(@Nullable final String javaColumn) {
        if (javaColumn == null || "".equals(javaColumn)) {
            return false;
        }
        return columnHash.containsKey(javaColumn);
    }


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

    public Map<String, ProviderColumn> getColumnHash() {
        return columnHash;
    }
}
