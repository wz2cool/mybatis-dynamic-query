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
}
