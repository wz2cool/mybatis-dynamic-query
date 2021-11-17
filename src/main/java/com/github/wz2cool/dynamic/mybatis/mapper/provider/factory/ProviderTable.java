package com.github.wz2cool.dynamic.mybatis.mapper.provider.factory;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author wangjin
 */
public final class ProviderTable {
    private final Class<?> entityClass;
    private final Field[] fields;
    private final String tableName;
    private final ProviderColumn primaryKey;
    /**
     * 空间换时间. key为{@link ProviderColumn#getJavaColumn()}
     */
    private final Map<String, ProviderColumn> columnHash;
    /**
     * 是否为自增ID
     */
    private final boolean isAutoIncrement;
    private final ProviderColumn[] columns;
    private final ProviderColumn[] transientColumns;
    private String key;

    public ProviderTable(Class<?> entityClass, Field[] fields, String tableName, ProviderColumn primaryKey, Map<String, ProviderColumn> columnHash, boolean isAutoIncrement, ProviderColumn[] columns, ProviderColumn[] transientColumns) {
        this.entityClass = entityClass;
        this.fields = fields;
        this.tableName = tableName;
        this.primaryKey = primaryKey;
        this.columnHash = columnHash;
        this.isAutoIncrement = isAutoIncrement;
        this.columns = columns;
        this.transientColumns = transientColumns;
    }

    static ProviderTableBuilder builder() {
        return new ProviderTableBuilder();
    }

    public String getKey() {
        return key;
    }

    void setKey(String key) {
        this.key = key;
    }

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

    public boolean isAutoIncrement() {
        return isAutoIncrement;
    }

    public Map<String, ProviderColumn> getColumnHash() {
        return columnHash;
    }

    static final class ProviderTableBuilder {
        private Class<?> entityClass;
        private Field[] fields;
        private String tableName;
        private ProviderColumn primaryKey;
        private Map<String, ProviderColumn> columnHash;
        private boolean isAutoIncrement;
        private ProviderColumn[] columns;
        private ProviderColumn[] transientColumns;

        private ProviderTableBuilder() {
        }

        public ProviderTableBuilder withEntityClass(Class<?> entityClass) {
            this.entityClass = entityClass;
            return this;
        }

        public ProviderTableBuilder withFields(Field[] fields) {
            this.fields = fields;
            return this;
        }

        public ProviderTableBuilder withTableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public ProviderTableBuilder withPrimaryKey(ProviderColumn primaryKey) {
            this.primaryKey = primaryKey;
            return this;
        }

        public ProviderTableBuilder withColumnHash(Map<String, ProviderColumn> columnHash) {
            this.columnHash = columnHash;
            return this;
        }

        public ProviderTableBuilder withIsAutoIncrement(boolean isAutoIncrement) {
            this.isAutoIncrement = isAutoIncrement;
            return this;
        }

        public ProviderTableBuilder withColumns(ProviderColumn[] columns) {
            this.columns = columns;
            return this;
        }

        public ProviderTableBuilder withTransientColumns(ProviderColumn[] transientColumns) {
            this.transientColumns = transientColumns;
            return this;
        }

        public ProviderTable build() {
            return new ProviderTable(entityClass, fields, tableName, primaryKey, columnHash, isAutoIncrement, columns, transientColumns);
        }
    }
}
