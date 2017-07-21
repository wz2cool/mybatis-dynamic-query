package com.github.wz2cool.dynamic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Frank
 * \* Date: 7/21/2017
 * \* Time: 3:44 PM
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class FilterGroupDescriptorTest {

    @Test
    public void addFiltersTest() {
        FilterDescriptor idFilter =
                new FilterDescriptor(FilterCondition.AND, "id", FilterOperator.EQUAL, 1);

        FilterGroupDescriptor groupDescriptor = new FilterGroupDescriptor();
        boolean result = groupDescriptor.addFilters(idFilter);
        assertEquals(true, result);
    }

    @Test
    public void removeFiltersTest() {
        FilterDescriptor idFilter =
                new FilterDescriptor(FilterCondition.AND, "id", FilterOperator.EQUAL, 1);

        FilterGroupDescriptor groupDescriptor = new FilterGroupDescriptor();
        boolean result = groupDescriptor.addFilters(idFilter);
        assertEquals(true, result);

        result = groupDescriptor.removeFilter(idFilter);
        assertEquals(true, result);
    }
}