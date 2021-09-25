package com.github.wz2cool.dynamic.provider;


/**
 * @author wangjin
 */
public class ProviderColumn {
    protected String column;
    protected boolean isPrimaryKey;
    protected Class<?> columnType;

    public String getColumn() {
        return column;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public Class<?> getColumnType() {
        return columnType;
    }
}
