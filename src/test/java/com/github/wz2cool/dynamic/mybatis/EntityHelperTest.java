package com.github.wz2cool.dynamic.mybatis;

import com.github.wz2cool.dynamic.mybatis.annotation.Column;
import com.github.wz2cool.exception.PropertyNotFoundInternalException;
import com.github.wz2cool.helper.ReflectHelper;
import com.github.wz2cool.model.ChildClass;
import com.github.wz2cool.model.HelloWorld;
import com.github.wz2cool.model.Student;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by Frank on 7/10/2017.
 */
public class EntityHelperTest {
    @Test(expected = InvocationTargetException.class)
    public void TestReflectHelper() throws Exception {
        Constructor<EntityHelper> c = EntityHelper.class.getDeclaredConstructor();
        c.setAccessible(true);
        c.newInstance();
    }

    @Test
    public void TestGetTableName() {
        String result = EntityHelper.getTableName(Student.class);
        assertEquals("student", result);

        result = EntityHelper.getTableName(HelloWorld.class);
        assertEquals("hello_world", result);
    }

    @Test(expected = NullPointerException.class)
    public void TestGetTableNameArgNullPointer() {
        EntityHelper.getTableName(null);
    }

    @Test
    public void TestGetPropertyField() {
        Field[] fields = Student.class.getDeclaredFields();
        Method[] methods = Student.class.getMethods();
        Field[] properties = Arrays.stream(fields)
                .filter(x -> ReflectHelper.isProperty(x, methods))
                .toArray(Field[]::new);

        Field result = EntityHelper.getPropertyField("name", properties);
        assertEquals(true, result != null);
    }

    @Test(expected = PropertyNotFoundInternalException.class)
    public void TestGetPropertyFieldNotFound() {
        Field[] fields = Student.class.getDeclaredFields();
        Method[] methods = Student.class.getMethods();
        Field[] properties = Arrays.stream(fields)
                .filter(x -> ReflectHelper.isProperty(x, methods))
                .toArray(Field[]::new);

        EntityHelper.getPropertyField("", properties);
    }

    @Test
    public void TestGetDbColumnByProperty() {
        Field[] fields = Student.class.getDeclaredFields();
        Method[] methods = Student.class.getMethods();
        Field[] properties = Arrays.stream(fields)
                .filter(x -> ReflectHelper.isProperty(x, methods))
                .toArray(Field[]::new);

        Column result = EntityHelper.getColumnByProperty("note", properties);
        assertEquals(true, result != null);

        result = EntityHelper.getColumnByProperty("name", properties);
        assertEquals(true, result != null);


    }

    @Test(expected = PropertyNotFoundInternalException.class)
    public void TestGetDbColumnByPropertyNotFound() {
        Field[] fields = Student.class.getDeclaredFields();
        Method[] methods = Student.class.getMethods();
        Field[] properties = Arrays.stream(fields)
                .filter(x -> ReflectHelper.isProperty(x, methods))
                .toArray(Field[]::new);
        Column result = EntityHelper.getColumnByProperty("noPropertyTest", properties);
        assertEquals(false, result != null);
    }


    @Test
    public void TestIsPropertyUpdateIfNull() {
        Field[] fields = Student.class.getDeclaredFields();
        Method[] methods = Student.class.getMethods();
        Field[] properties = Arrays.stream(fields)
                .filter(x -> ReflectHelper.isProperty(x, methods))
                .toArray(Field[]::new);

        boolean result = EntityHelper.isPropertyUpdateIfNull("note", properties);
        assertEquals(true, result);

        result = EntityHelper.isPropertyUpdateIfNull("name", properties);
        assertEquals(false, result);
    }

    @Test(expected = PropertyNotFoundInternalException.class)
    public void TestIsPropertyUpdateIfNullNotFound() {
        Field[] fields = Student.class.getDeclaredFields();
        Method[] methods = Student.class.getMethods();
        Field[] properties = Arrays.stream(fields)
                .filter(x -> ReflectHelper.isProperty(x, methods))
                .toArray(Field[]::new);

        boolean result = EntityHelper.isPropertyUpdateIfNull("noPropertyTest", properties);
        assertEquals(false, result);
    }

    @Test
    public void TestGetDBColumnNameByProperty() {
        Field[] fields = Student.class.getDeclaredFields();
        Method[] methods = Student.class.getMethods();
        Field[] properties = Arrays.stream(fields)
                .filter(x -> ReflectHelper.isProperty(x, methods))
                .toArray(Field[]::new);

        String result = EntityHelper.getColumnNameByProperty("note", properties);
        assertEquals("note", result);

        result = EntityHelper.getColumnNameByProperty("name", properties);
        assertEquals("name", result);
    }

    @Test(expected = PropertyNotFoundInternalException.class)
    public void TestGetDBColumnNameByPropertyNotFound() {
        Field[] fields = Student.class.getDeclaredFields();
        Method[] methods = Student.class.getMethods();
        Field[] properties = Arrays.stream(fields)
                .filter(x -> ReflectHelper.isProperty(x, methods))
                .toArray(Field[]::new);

        String result = EntityHelper.getColumnNameByProperty("noPropertyTest", properties);
        assertEquals("", result);
    }

    @Test
    public void TestGetQueryColumnByProperty() {
        Field[] properties = ReflectHelper.getProperties(ChildClass.class);

        String result = EntityHelper.getColumnNameByProperty("childP1", properties);
        assertEquals("child_p1", result);
    }

    @Test(expected = PropertyNotFoundInternalException.class)
    public void TestGetQueryColumnByPropertyNotFound() {
        Field[] properties = ReflectHelper.getProperties(ChildClass.class);
        EntityHelper.getColumnNameByProperty("", properties);
    }
}
