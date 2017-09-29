package com.github.wz2cool.dynamic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * \* Created with IntelliJ IDEA.
 * \* User: Frank
 * \* Date: 9/29/2017
 * \* Time: 1:55 PM
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class CustomSortDescriptorTest {

    @Test
    public void testDefaultConstructor() {
        CustomSortDescriptor customSortDescriptor =
                new CustomSortDescriptor("{0} DESC", "test");

        assertEquals("{0} DESC", customSortDescriptor.getExpression());
        assertEquals("test", customSortDescriptor.getParams()[0]);
    }
}