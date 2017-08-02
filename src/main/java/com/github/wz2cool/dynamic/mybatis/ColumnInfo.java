package com.github.wz2cool.dynamic.mybatis;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

/**
 * Created by Frank on 2017/6/25.
 */
class ColumnInfo {
    private String columnName;
    private String tableOrAlias;
    private boolean updateIfNull;
    private boolean insertIfNull;
    private boolean insertIgnore;
    private JdbcType jdbcType;
    private Field field;

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public boolean isUpdateIfNull() {
        return updateIfNull;
    }

    public void setUpdateIfNull(boolean updateIfNull) {
        this.updateIfNull = updateIfNull;
    }

    public String getTableOrAlias() {
        return tableOrAlias;
    }

    public void setTableOrAlias(String tableOrAlias) {
        this.tableOrAlias = tableOrAlias;
    }

    public String getQueryColumn() {
        if (StringUtils.isNotBlank(this.tableOrAlias)) {
            return String.format("%s.%s", this.tableOrAlias, this.columnName);
        } else {
            return this.columnName;
        }
    }

    public boolean isInsertIfNull() {
        return insertIfNull;
    }

    public void setInsertIfNull(boolean insertIfNull) {
        this.insertIfNull = insertIfNull;
    }

    public JdbcType getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(JdbcType jdbcType) {
        this.jdbcType = jdbcType;
    }

    public boolean isInsertIgnore() {
        return insertIgnore;
    }

    public void setInsertIgnore(boolean insertIgnore) {
        this.insertIgnore = insertIgnore;
    }
}
