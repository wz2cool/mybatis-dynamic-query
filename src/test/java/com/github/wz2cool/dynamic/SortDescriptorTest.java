package com.github.wz2cool.dynamic;

import com.github.wz2cool.dynamic.model.Student;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Frank
 * \* Date: 7/31/2017
 * \* Time: 2:11 PM
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class SortDescriptorTest {

    @Test
    public void lambdaNewInstanceTest() {
        SortDescriptor filterDescriptor =
                new SortDescriptor(Student::getAge, SortDirection.DESC);

        assertEquals("age", filterDescriptor.getPropertyName());
    }


    @Test
    public void lambdaSetPropertyTest() {
        SortDescriptor filterDescriptor = new SortDescriptor();
        filterDescriptor.setPropertyPath(Student::getAge);
        assertEquals("age", filterDescriptor.getPropertyName());
    }
}