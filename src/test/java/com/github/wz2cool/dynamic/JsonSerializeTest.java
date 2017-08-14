package com.github.wz2cool.dynamic;

import com.google.gson.Gson;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class JsonSerializeTest {

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