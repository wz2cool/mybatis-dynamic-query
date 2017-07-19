package com.frwan.query.dynamic.mybatis;

import com.frwan.query.exception.PropertyNotFoundInternalException;
import com.frwan.query.model.Student;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Frank on 7/11/2017.
 */
public class EntityCacheTest {

    @Test
    public void TestGetQueryColumn() {
        QueryColumnInfo result = EntityCache.getInstance().getQueryColumnInfo(Student.class, "name");
        assertEquals("name", result.getQueryColumn());

        result = EntityCache.getInstance().getQueryColumnInfo(Student.class, "note");
        assertEquals("queryColumn.note", result.getQueryColumn());
    }

    @Test(expected = NullPointerException.class)
    public void TestGetQueryColumnEntityClassNull() {
        EntityCache.getInstance().getQueryColumnInfo(null, "name");
    }

    @Test(expected = NullPointerException.class)
    public void TestGetQueryColumnPropertyNull() {
        EntityCache.getInstance().getQueryColumnInfo(Student.class, null);
    }

    @Test(expected = PropertyNotFoundInternalException.class)
    public void TestGetQueryColumnPropertyNotFound() {
        EntityCache.getInstance().getQueryColumnInfo(Student.class, "notFoundProperty");
    }

    @Test
    public void TestGetDbColumnInfo() {
        DbColumnInfo result = EntityCache.getInstance().getDbColumnInfo(Student.class, "name");
        assertEquals("name", result.getDbColumnName());
        assertEquals(false, result.isUpdateIfNull());

        result = EntityCache.getInstance().getDbColumnInfo(Student.class, "note");
        assertEquals("dbColumn.note", result.getDbColumnName());
        assertEquals(true, result.isUpdateIfNull());
    }

    @Test(expected = NullPointerException.class)
    public void TestGetDbColumnInfoEntityClassNull() {
        EntityCache.getInstance().getDbColumnInfo(null, "name");
    }

    @Test(expected = NullPointerException.class)
    public void TestGetDbColumnInfoPropertyNull() {
        EntityCache.getInstance().getDbColumnInfo(Student.class, null);
    }

    @Test(expected = PropertyNotFoundInternalException.class)
    public void TestGetDbColumnPropertyNotFound() {
        EntityCache.getInstance().getDbColumnInfo(Student.class, "notFoundProperty");
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
