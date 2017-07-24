package com.github.wz2cool.dynamic.mybatis;

import com.github.wz2cool.exception.PropertyNotFoundInternalException;
import com.github.wz2cool.model.Student;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Frank on 7/11/2017.
 */
public class EntityCacheTest {

    @Test
    public void TestGetQueryColumn() {
        ColumnInfo result = EntityCache.getInstance().getColumnInfo(Student.class, "name");
        assertEquals("name", result.getQueryColumn());

        result = EntityCache.getInstance().getColumnInfo(Student.class, "note");
        assertEquals("queryColumn.note", result.getQueryColumn());
        assertEquals("queryColumn", result.getTableOrAlias());
    }

    @Test(expected = NullPointerException.class)
    public void TestGetQueryColumnEntityClassNull() {
        EntityCache.getInstance().getColumnInfo(null, "name");
    }

    @Test(expected = NullPointerException.class)
    public void TestGetQueryColumnPropertyNull() {
        EntityCache.getInstance().getColumnInfo(Student.class, null);
    }

    @Test(expected = PropertyNotFoundInternalException.class)
    public void TestGetQueryColumnPropertyNotFound() {
        EntityCache.getInstance().getColumnInfo(Student.class, "notFoundProperty");
    }

    @Test
    public void TestGetDbColumnInfo() {
        ColumnInfo result = EntityCache.getInstance().getColumnInfo(Student.class, "name");
        assertEquals("name", result.getColumnName());
        assertEquals(false, result.isUpdateIfNull());

        result = EntityCache.getInstance().getColumnInfo(Student.class, "note");
        assertEquals("note", result.getColumnName());
        assertEquals(true, result.isUpdateIfNull());
    }

    @Test(expected = NullPointerException.class)
    public void TestGetDbColumnInfoEntityClassNull() {
        EntityCache.getInstance().getColumnInfo(null, "name");
    }

    @Test(expected = NullPointerException.class)
    public void TestGetDbColumnInfoPropertyNull() {
        EntityCache.getInstance().getColumnInfo(Student.class, null);
    }

    @Test(expected = PropertyNotFoundInternalException.class)
    public void TestGetDbColumnPropertyNotFound() {
        EntityCache.getInstance().getColumnInfo(Student.class, "notFoundProperty");
    }

    @Test
    public void TestHasProperty() {
        boolean result = EntityCache.getInstance().hasProperty(Student.class, "name");
        assertEquals(true, result);

        result = EntityCache.getInstance().hasProperty(Student.class, "notProperty");
        assertEquals(false, result);

        result = EntityCache.getInstance().hasProperty(Student.class, "");
        assertEquals(false, result);
    }

    @Test(expected = NullPointerException.class)
    public void TestHasPropertyEntityNull() {
        EntityCache.getInstance().hasProperty(null, "name");
    }
}
