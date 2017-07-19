package com.frwan.query.dynamic.mybatis;

import java.lang.reflect.Field;

/**
 * Created by Frank on 7/18/2017.
 */
public class QueryColumnInfo {
    private String queryColumn;
    private Field field;

    public String getQueryColumn() {
        return queryColumn;
    }

    public void setQueryColumn(String queryColumn) {
        this.queryColumn = queryColumn;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }
}
