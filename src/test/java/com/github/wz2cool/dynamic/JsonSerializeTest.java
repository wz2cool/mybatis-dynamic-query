package com.github.wz2cool.dynamic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.wz2cool.model.Student;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class JsonSerializeTest {
    private static ObjectMapper mapper = new ObjectMapper();

    /* static {
         SimpleModule module = new SimpleModule();
         module.addAbstractTypeMapping(FilterDescriptorBase.class, FilterGroupDescriptor.class);
         module.addAbstractTypeMapping(FilterDescriptorBase.class, FilterDescriptor.class);

        module.addAbstractTypeMapping(FilterDescriptorBase.class, CustomFilterDescriptor.class);*//*
        mapper.registerModule(module);
    }
*/
    @Test
    public void testSerializeFilterDescriptor() throws Exception {
        FilterDescriptor ageFilter =
                new FilterDescriptor(Student.class, Student::getAge, FilterOperator.EQUAL, 2);
        String jsonStr = mapper.writeValueAsString(ageFilter);
        FilterDescriptor ageFilterCopy = mapper.readValue(jsonStr, FilterDescriptor.class);
        assertEquals(ageFilter.getPropertyPath(), ageFilterCopy.getPropertyPath());
        assertEquals(ageFilter.getValue(), ageFilterCopy.getValue());

        FilterDescriptorBase[] filters = new FilterDescriptorBase[]{ageFilter};
        String jsonArrayStr = mapper.writeValueAsString(filters);
        FilterDescriptorBase[] filtersCopy = mapper.readValue(jsonArrayStr, FilterDescriptorBase[].class);
        ageFilterCopy = (FilterDescriptor) filtersCopy[0];
        assertEquals(ageFilter.getPropertyPath(), ageFilterCopy.getPropertyPath());
        assertEquals(ageFilter.getValue(), ageFilterCopy.getValue());
    }

    @Test
    public void testSerializeFilterGroupDescriptor() throws Exception {
        FilterDescriptor ageFilter =
                new FilterDescriptor(Student.class, Student::getAge, FilterOperator.GREATER_THAN, 20);
        FilterDescriptor ageFilter2 =
                new FilterDescriptor(Student.class, Student::getAge, FilterOperator.LESS_THAN, 30);

        FilterGroupDescriptor ageGroupFilter = new FilterGroupDescriptor();
        ageGroupFilter.addFilters(ageFilter);
        ageGroupFilter.addFilters(ageFilter2);

        String jsonStr = mapper.writeValueAsString(ageGroupFilter);
        FilterGroupDescriptor ageGroupFilterCopy = mapper.readValue(jsonStr, FilterGroupDescriptor.class);
        FilterDescriptor ageFilterCopy = (FilterDescriptor) ageGroupFilterCopy.getFilters()[0];
        FilterDescriptor ageFilter2Copy = (FilterDescriptor) ageGroupFilterCopy.getFilters()[1];
        assertEquals(ageFilter.getPropertyPath(), ageFilterCopy.getPropertyPath());
        assertEquals(ageFilter2Copy.getValue(), ageFilter2Copy.getValue());

        FilterDescriptorBase[] filters = new FilterDescriptorBase[]{ageGroupFilter};
        String arrayFiltersJson = mapper.writeValueAsString(filters);
        FilterDescriptorBase[] filtersCopy = mapper.readValue(arrayFiltersJson, FilterDescriptorBase[].class);
        ageGroupFilterCopy = (FilterGroupDescriptor) filtersCopy[0];
        ageFilterCopy = (FilterDescriptor) ageGroupFilterCopy.getFilters()[0];
        ageFilter2Copy = (FilterDescriptor) ageGroupFilterCopy.getFilters()[1];
        assertEquals(ageFilter.getPropertyPath(), ageFilterCopy.getPropertyPath());
        assertEquals(ageFilter2Copy.getValue(), ageFilter2Copy.getValue());
    }


    @Test
    public void testSerializeCustomFilterDescriptor() throws Exception {
        CustomFilterDescriptor ageFilter = new CustomFilterDescriptor();
        ageFilter.setExpression("age > {1} and age < {2}");
        ageFilter.setParams(20, 30);

        String jsonStr = mapper.writeValueAsString(ageFilter);
        CustomFilterDescriptor ageFilterCopy = mapper.readValue(jsonStr, CustomFilterDescriptor.class);
        assertEquals(ageFilter.getExpression(), ageFilterCopy.getExpression());
        assertEquals(ageFilter.getParams()[0], ageFilterCopy.getParams()[0]);
        assertEquals(ageFilter.getParams()[1], ageFilterCopy.getParams()[1]);

        FilterDescriptorBase[] filters = new FilterDescriptorBase[]{ageFilter};
        String arrayFiltersJson = mapper.writeValueAsString(filters);
        FilterDescriptorBase[] filtersCopy = mapper.readValue(arrayFiltersJson, FilterDescriptorBase[].class);

        ageFilterCopy = (CustomFilterDescriptor) filtersCopy[0];
        assertEquals(ageFilter.getExpression(), ageFilterCopy.getExpression());
        assertEquals(ageFilter.getParams()[0], ageFilterCopy.getParams()[0]);
        assertEquals(ageFilter.getParams()[1], ageFilterCopy.getParams()[1]);
    }
}