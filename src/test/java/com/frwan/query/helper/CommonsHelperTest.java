package com.frwan.query.helper;

import com.frwan.query.model.Student;
import com.sun.org.apache.xpath.internal.operations.String;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
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
}
