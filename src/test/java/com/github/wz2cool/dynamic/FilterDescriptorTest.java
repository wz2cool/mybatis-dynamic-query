package com.github.wz2cool.dynamic;

import com.github.wz2cool.dynamic.model.Student;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Frank
 * \* Date: 7/31/2017
 * \* Time: 1:57 PM
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class FilterDescriptorTest {
    @Test
    public void lambdaNewInstanceTest() {
        FilterDescriptor filterDescriptor =
                new FilterDescriptor(FilterCondition.AND, Student::getAge,
                        FilterOperator.EQUAL, "3");

        assertEquals("age", filterDescriptor.getPropertyName());
    }

    @Test
    public void lambdaSetPropertyTest() {
        FilterDescriptor filterDescriptor = new FilterDescriptor();
        filterDescriptor.setPropertyPath(Student::getAge);
        assertEquals("age", filterDescriptor.getPropertyName());
    }
}