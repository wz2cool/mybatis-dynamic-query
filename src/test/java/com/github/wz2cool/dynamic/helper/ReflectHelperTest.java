package com.github.wz2cool.dynamic.helper;

import com.github.wz2cool.dynamic.model.ChildClass;
import com.github.wz2cool.dynamic.model.Student;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by Frank on 7/7/2017.
 */
public class ReflectHelperTest {
    @Test(expected = InvocationTargetException.class)
    public void TestReflectHelper() throws Exception {
        Constructor<ReflectHelper> c = ReflectHelper.class.getDeclaredConstructor();
        c.setAccessible(true);
        c.newInstance();
    }

    @Test
    public void TestHasParentInterface() {
        boolean result = ReflectHelper.hasParentInterface(List.class, Iterable.class);
        assertEquals(true, result);

        result = ReflectHelper.hasParentInterface(List.class, Iterable.class);
        assertEquals(true, result);

        result = ReflectHelper.hasParentInterface(ArrayList.class, Iterable.class);
        assertEquals(true, result);

        result = ReflectHelper.hasParentInterface(List.class, List.class);
        assertEquals(true, result);


        result = ReflectHelper.hasParentInterface(Iterable.class, Object.class);
        assertEquals(false, result);

        result = ReflectHelper.hasParentInterface(null, Iterable.class);
        assertEquals(false, result);

        result = ReflectHelper.hasParentInterface(List.class, null);
        assertEquals(false, result);
    }

    @Test
    public void TestHasParentClass() {
        boolean result = ReflectHelper.hasParentClass(ArrayList.class, AbstractList.class);
        assertEquals(true, result);

        result = ReflectHelper.hasParentClass(ArrayList.class, ArrayList.class);
        assertEquals(true, result);

        result = ReflectHelper.hasParentClass(ArrayList.class, Object.class);
        assertEquals(true, result);

        result = ReflectHelper.hasParentClass(Object.class, List.class);
        assertEquals(false, result);

        result = ReflectHelper.hasParentClass(null, AbstractList.class);
        assertEquals(false, result);

        result = ReflectHelper.hasParentClass(ArrayList.class, null);
        assertEquals(false, result);
    }

    @Test
    public void TestHasParent() {
        boolean result = ReflectHelper.hasParent(List.class, Iterable.class);
        assertEquals(true, result);

        result = ReflectHelper.hasParent(List.class, Iterable.class);
        assertEquals(true, result);

        result = ReflectHelper.hasParent(ArrayList.class, Iterable.class);
        assertEquals(true, result);

        result = ReflectHelper.hasParent(List.class, List.class);
        assertEquals(true, result);

        result = ReflectHelper.hasParent(Iterable.class, Object.class);
        assertEquals(false, result);

        result = ReflectHelper.hasParent(null, Iterable.class);
        assertEquals(false, result);

        result = ReflectHelper.hasParent(List.class, null);
        assertEquals(false, result);

        result = ReflectHelper.hasParent(ArrayList.class, AbstractList.class);
        assertEquals(true, result);

        result = ReflectHelper.hasParent(ArrayList.class, ArrayList.class);
        assertEquals(true, result);

        result = ReflectHelper.hasParent(ArrayList.class, Object.class);
        assertEquals(true, result);

        result = ReflectHelper.hasParent(Object.class, List.class);
        assertEquals(false, result);

        result = ReflectHelper.hasParent(null, AbstractList.class);
        assertEquals(false, result);

        result = ReflectHelper.hasParent(ArrayList.class, null);
        assertEquals(false, result);
    }

    @Test
    public void TestIsProperty() throws Exception {
        Method[] methods = Student.class.getMethods();
        Field field = Student.class.getDeclaredField("name");
        boolean result = ReflectHelper.isProperty(field, methods);
        assertEquals(true, result);

        field = Student.class.getDeclaredField("serialId");
        result = ReflectHelper.isProperty(field, methods);
        assertEquals(false, result);

        result = ReflectHelper.isProperty(null, methods);
        assertEquals(false, result);

        result = ReflectHelper.isProperty(field, null);
        assertEquals(false, result);
    }

    @Test
    public void TestGetPropertiesForCurrentClass() {
        Class testClass = ChildClass.class;
        Field[] fields = ReflectHelper.getPropertiesForCurrentClass(testClass);
        boolean result = Arrays.stream(fields).anyMatch(x -> "childP1".equalsIgnoreCase(x.getName()));
        assertEquals(true, result);

        result = Arrays.stream(fields).anyMatch(x -> "parentP1".equalsIgnoreCase(x.getName()));
        assertEquals(false, result);
    }

    @Test
    public void TestGetProperties() {
        Class testClass = ChildClass.class;
        Field[] fields = ReflectHelper.getProperties(testClass);
        boolean result = Arrays.stream(fields).anyMatch(x -> "childP1".equalsIgnoreCase(x.getName()));
        assertEquals(true, result);

        result = Arrays.stream(fields).anyMatch(x -> "parentP1".equalsIgnoreCase(x.getName()));
        assertEquals(true, result);
    }

    @Test
    public void TestHasContainsFieldName() {
        Class testClass = ChildClass.class;
        Field[] fields = ReflectHelper.getProperties(testClass);
        Collection<Field> fieldCollection = Arrays.asList(fields);

        boolean result = ReflectHelper.hasContainsFieldName("childP1", fieldCollection);
        assertEquals(true, result);

        result = ReflectHelper.hasContainsFieldName("", fieldCollection);
        assertEquals(false, result);
    }
}
