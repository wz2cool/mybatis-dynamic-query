package com.github.wz2cool.dynamic.helper;

import com.github.wz2cool.dynamic.model.PropertyInfo;
import com.github.wz2cool.dynamic.model.Student;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;


/**
 * Created by Frank on 7/7/2017.
 */

public class CommonsHelperTest {

    @Test(expected = InvocationTargetException.class)
    public void TestCommonsHelper() throws Exception {
        Constructor<CommonsHelper> c = CommonsHelper.class.getDeclaredConstructor();
        c.setAccessible(true);
        c.newInstance();
    }

    @Test
    public void TestIsArray() {
        Object value = new String[0];
        boolean result = CommonsHelper.isArray(value);
        assertEquals(true, result);

        value = "123";
        result = CommonsHelper.isArray(value);
        assertEquals(false, result);

        value = null;
        result = CommonsHelper.isArray(value);
        assertEquals(false, result);
    }

    @Test
    public void TestIsCollection() {
        Object value = new ArrayList<String>();
        boolean result = CommonsHelper.isCollection(value);
        assertEquals(true, result);

        value = "123";
        result = CommonsHelper.isCollection(value);
        assertEquals(false, result);

        value = null;
        result = CommonsHelper.isCollection(value);
        assertEquals(false, result);
    }

    @Test
    public void TestIsArrayOrCollection() {
        Object value = new ArrayList<String>();
        boolean result = CommonsHelper.isArrayOrCollection(value);
        assertEquals(true, result);

        value = new String[0];
        result = CommonsHelper.isArray(value);
        assertEquals(true, result);

        value = "123";
        result = CommonsHelper.isArrayOrCollection(value);
        assertEquals(false, result);

        value = null;
        result = CommonsHelper.isArrayOrCollection(value);
        assertEquals(false, result);
    }

    @Test
    public void TestGetCollectionValues() {
        List<Integer> testCollections = new ArrayList<Integer>();
        testCollections.add(10);
        testCollections.add(3);
        testCollections.add(100);

        Object[] result = CommonsHelper.getCollectionValues(testCollections);

        assertEquals(testCollections.get(0), result[0]);
        assertEquals(testCollections.get(1), result[1]);
        assertEquals(testCollections.get(2), result[2]);

        int[] testArray = new int[]{1, 3, 5};
        result = CommonsHelper.getCollectionValues(testArray);

        assertEquals(testArray[0], result[0]);
        assertEquals(testArray[1], result[1]);
        assertEquals(testArray[2], result[2]);
    }

    @Test(expected = NullPointerException.class)
    public void TestGetCollectionValuesThrowNullException() {
        CommonsHelper.getCollectionValues(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestGetCollectionValuesThrowIllegalArgException() {
        CommonsHelper.getCollectionValues("test");
    }

    @Test
    public void testIsNumeric() {
        boolean result = CommonsHelper.isNumeric(Integer.class);
        assertEquals(true, result);

        result = CommonsHelper.isNumeric(BigDecimal.class);
        assertEquals(true, result);

        result = CommonsHelper.isNumeric(Long.class);
        assertEquals(true, result);

        result = CommonsHelper.isNumeric(null);
        assertEquals(false, result);

        result = CommonsHelper.isNumeric(Student.class);
        assertEquals(false, result);
    }

    @Test
    public void testGetPropertyInfo() {
        int[] servers = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Arrays.stream(servers).parallel().forEach((a) -> {
            PropertyInfo result = CommonsHelper.getPropertyInfo(Student::getAge);
            assertEquals("age", result.getPropertyName());
            assertEquals(Student.class, result.getOwnerClass());

            result = CommonsHelper.getPropertyInfo(Student::getName);
            assertEquals("name", result.getPropertyName());
            assertEquals(Student.class, result.getOwnerClass());

            result = CommonsHelper.getPropertyInfo(Student::getNote);
            assertEquals("note", result.getPropertyName());
            assertEquals(Student.class, result.getOwnerClass());

            result = CommonsHelper.getPropertyInfo(Student::isDeleted);
            assertEquals("deleted", result.getPropertyName());
            assertEquals(Student.class, result.getOwnerClass());
            // this will call cache to get class
            result = CommonsHelper.getPropertyInfo(Student::isDeleted);
            assertEquals("deleted", result.getPropertyName());
            assertEquals(Student.class, result.getOwnerClass());

            result = CommonsHelper.getPropertyInfo(Student::toString);
            assertEquals("toString", result.getPropertyName());
        });
    }


    @Test
    public void testToStringSafe() {
        String result = CommonsHelper.toStringSafe(null);
        assertEquals("", result);

        result = CommonsHelper.toStringSafe(1);
        assertEquals("1", result);
    }


    @Test
    public void format() {
        int count = 10_000_000;
        System.out.println("循环次数: " + count + " 统计耗时为毫秒");


        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            String format = MessageFormat.format("{0},{1},{2},{3},{4}", "V1", "V2", "1", "2", "3");
        }
        long end = System.currentTimeMillis();
        System.out.println("MessageFormat.format with {} 不同的占位符:" + (end - start));


        start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            String format = CommonsHelper.format("%s,%s,%s,%s,%s", "V1", "V2", "1", "2", "3");
        }
        end = System.currentTimeMillis();
        System.out.println("CommonsHelper.format {} :" + (end - start));


        start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            String format = MessageFormat.format("{0},{0},{0},{0},{0}", "V1");
        }
        end = System.currentTimeMillis();
        System.out.println("MessageFormat.format with 同一个占位符 {0} :" + (end - start));


        start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            String format = "V1" + "V2" + "1" + "2" + "3";
        }
        end = System.currentTimeMillis();
        System.out.println("String plus: " + (end - start));


        start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            String format = String.format("%s,%s,%s,%s,%s", "V1", "V2", "1", "2", "3");
        }
        end = System.currentTimeMillis();
        System.out.println("String.format: " + (end - start));


        start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            String format = new StringBuilder().append("V1").append("V2")
                    .append("1").append("2").append("3").toString();
        }
        end = System.currentTimeMillis();
        System.out.println("StringBuilder: " + (end - start));


        String format = CommonsHelper.format("%s,%s,%s,%s,%s", "V1", "V2", "1", "2", "3");
        //V1,V2,1,2,3
        System.out.println(format);

        format = CommonsHelper.format("'%s'''':::,%s',%'s,%s',%s", "V1", "V2", "1", "2", "3");
        System.out.println(format);
        //'V1'''':::,V2',%'s,1',2
    }

}
