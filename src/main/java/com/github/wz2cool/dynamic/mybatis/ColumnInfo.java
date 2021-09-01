package com.github.wz2cool.dynamic.mybatis;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

/**
 * Created by Frank on 2017/6/25.
 */
public class ColumnInfo {
    private String columnName;
    private String tableOrAlias;
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

    public String getTableOrAlias() {
        return tableOrAlias;
    }

    public void setTableOrAlias(String tableOrAlias) {
        this.tableOrAlias = tableOrAlias;
    }

    public String getQueryColumn() {
        if (StringUtils.isNotBlank(getTableOrAlias())) {
            return String.format("%s.%s", getTableOrAlias(), getColumnName());
        } else {
            return this.columnName;
        }
    }
}
