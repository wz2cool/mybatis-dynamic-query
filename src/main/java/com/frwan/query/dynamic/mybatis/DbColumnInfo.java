package com.frwan.query.dynamic.mybatis;

import java.lang.reflect.Field;

/**
 * Created by Frank on 2017/6/25.
 */
class DbColumnInfo {
    private String dbColumnName;
    private boolean updateIfNull;
    private Field field;

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public String getDbColumnName() {
        return dbColumnName;
    }

    public void setDbColumnName(String dbColumnName) {
        this.dbColumnName = dbColumnName;
    }

    public boolean isUpdateIfNull() {
        return updateIfNull;
    }

    public void setUpdateIfNull(boolean updateIfNull) {
        this.updateIfNull = updateIfNull;
    }
}
