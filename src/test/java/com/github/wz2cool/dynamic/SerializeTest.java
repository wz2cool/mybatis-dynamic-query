package com.github.wz2cool.dynamic;

import com.google.gson.Gson;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Frank
 * \* Date: 8/9/2017
 * \* Time: 2:54 PM
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class SerializeTest {

    private Gson gson = new Gson();

    @Test
    public void testSerializeFilterDescriptor() {
        FilterDescriptor filterDescriptor =
                new FilterDescriptor(FilterCondition.AND, "name", FilterOperator.EQUAL, "frank");

        String jsonStr = gson.toJson(filterDescriptor);
        FilterDescriptor filterDescriptorCopy = gson.fromJson(jsonStr, FilterDescriptor.class);
        assertEquals(filterDescriptor.getPropertyPath(), filterDescriptorCopy.getPropertyPath());
        assertEquals(filterDescriptor.getCondition(), filterDescriptorCopy.getCondition());
        assertEquals(filterDescriptor.getValue(), filterDescriptorCopy.getValue());
    }
}