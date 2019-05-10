package com.github.wz2cool.dynamic;

import com.fasterxml.jackson.databind.ObjectMapper;
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
                new FilterDescriptor(Student::getAge, FilterOperator.EQUAL, 2);
        String jsonStr = mapper.writeValueAsString(ageFilter);
        FilterDescriptor ageFilterCopy = mapper.readValue(jsonStr, FilterDescriptor.class);
        assertEquals(ageFilter.getPropertyPath(), ageFilterCopy.getPropertyPath());
        assertEquals(ageFilter.getValue(), ageFilterCopy.getValue());

        BaseFilterDescriptor[] filters = new BaseFilterDescriptor[]{ageFilter};
        String jsonArrayStr = mapper.writeValueAsString(filters);
        BaseFilterDescriptor[] filtersCopy = mapper.readValue(jsonArrayStr, BaseFilterDescriptor[].class);
        ageFilterCopy = (FilterDescriptor) filtersCopy[0];
        assertEquals(ageFilter.getPropertyPath(), ageFilterCopy.getPropertyPath());
        assertEquals(ageFilter.getValue(), ageFilterCopy.getValue());
    }

    @Test
    public void testSerializeFilterGroupDescriptor() throws Exception {
        FilterDescriptor ageFilter =
                new FilterDescriptor(Student::getAge, FilterOperator.GREATER_THAN, 20);
        FilterDescriptor ageFilter2 =
                new FilterDescriptor(Student::getAge, FilterOperator.LESS_THAN, 30);

        FilterGroupDescriptor ageGroupFilter = new FilterGroupDescriptor();
        ageGroupFilter.addFilters(ageFilter);
        ageGroupFilter.addFilters(ageFilter2);

        String jsonStr = mapper.writeValueAsString(ageGroupFilter);
        FilterGroupDescriptor ageGroupFilterCopy = mapper.readValue(jsonStr, FilterGroupDescriptor.class);
        FilterDescriptor ageFilterCopy = (FilterDescriptor) ageGroupFilterCopy.getFilters()[0];
        FilterDescriptor ageFilter2Copy = (FilterDescriptor) ageGroupFilterCopy.getFilters()[1];
        assertEquals(ageFilter.getPropertyPath(), ageFilterCopy.getPropertyPath());
        assertEquals(ageFilter2Copy.getValue(), ageFilter2Copy.getValue());

        BaseFilterDescriptor[] filters = new BaseFilterDescriptor[]{ageGroupFilter};
        String arrayFiltersJson = mapper.writeValueAsString(filters);
        BaseFilterDescriptor[] filtersCopy = mapper.readValue(arrayFiltersJson, BaseFilterDescriptor[].class);
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

        BaseFilterDescriptor[] filters = new BaseFilterDescriptor[]{ageFilter};
        String arrayFiltersJson = mapper.writeValueAsString(filters);
        BaseFilterDescriptor[] filtersCopy = mapper.readValue(arrayFiltersJson, BaseFilterDescriptor[].class);

        ageFilterCopy = (CustomFilterDescriptor) filtersCopy[0];
        assertEquals(ageFilter.getExpression(), ageFilterCopy.getExpression());
        assertEquals(ageFilter.getParams()[0], ageFilterCopy.getParams()[0]);
        assertEquals(ageFilter.getParams()[1], ageFilterCopy.getParams()[1]);
    }

    @Test
    public void testSerializeDynamicQuery() throws Exception {
        DynamicQuery<Student> dynamicQuery = new DynamicQuery<>(Student.class);
        FilterDescriptor nameFilter =
                new FilterDescriptor(Student::getName, FilterOperator.EQUAL, "frank");
        dynamicQuery.addFilters(nameFilter);

        SortDescriptor ageSort =
                new SortDescriptor(Student::getAge, SortDirection.ASC);
        dynamicQuery.addSorts(ageSort);

        String jsonStr = mapper.writeValueAsString(dynamicQuery);
        DynamicQuery dynamicQueryCopy = mapper.readValue(jsonStr, DynamicQuery.class);

        assertEquals(dynamicQuery.getEntityClass(), dynamicQueryCopy.getEntityClass());
        assertEquals(dynamicQuery.getFilters().length, dynamicQueryCopy.getFilters().length);
        assertEquals(dynamicQuery.getSorts().length, dynamicQueryCopy.getSorts().length);
    }

    @Test
    public void testSerializeCustomSortDescriptor() throws Exception {
        CustomSortDescriptor ageSort = new CustomSortDescriptor();
        ageSort.setExpression("{0} DESC, {1} ASC");
        ageSort.setParams("age", "name");

        String jsonStr = mapper.writeValueAsString(ageSort);
        CustomSortDescriptor ageSortCopy = mapper.readValue(jsonStr, CustomSortDescriptor.class);
        assertEquals(ageSort.getExpression(), ageSortCopy.getExpression());
        assertEquals(ageSort.getParams()[0], ageSortCopy.getParams()[0]);
        assertEquals(ageSort.getParams()[1], ageSortCopy.getParams()[1]);

        BaseSortDescriptor[] sorts = new BaseSortDescriptor[]{ageSort};
        String arrayFiltersJson = mapper.writeValueAsString(sorts);
        BaseSortDescriptor[] sortCopy = mapper.readValue(arrayFiltersJson, BaseSortDescriptor[].class);

        ageSortCopy = (CustomSortDescriptor) sortCopy[0];
        assertEquals(ageSort.getExpression(), ageSortCopy.getExpression());
        assertEquals(ageSort.getParams()[0], ageSortCopy.getParams()[0]);
        assertEquals(ageSort.getParams()[1], ageSortCopy.getParams()[1]);
    }
}