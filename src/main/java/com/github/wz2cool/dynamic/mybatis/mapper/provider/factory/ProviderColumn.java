package com.github.wz2cool.dynamic.mybatis.mapper.provider.factory;


/**
 * @author wangjin
 */
public class ProviderColumn {
    protected String javaColumn;
    protected String dbColumn;
    protected boolean isPrimaryKey;
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
}
