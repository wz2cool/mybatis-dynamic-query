package com.github.wz2cool.dynamic;

import com.github.wz2cool.model.Student;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Frank
 * \* Date: 8/10/2017
 * \* Time: 10:15 AM
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class DynamicQueryTest {

    @Test
    public void testAddFilter() {
        FilterDescriptor filterDescriptor =
                new FilterDescriptor(FilterCondition.AND, "name", FilterOperator.EQUAL, "frank");

        DynamicQuery<Student> dynamicQuery = new DynamicQuery<>(Student.class);
        dynamicQuery.addFilter(filterDescriptor);
        assertEquals(1, dynamicQuery.getFilters().length);
    }

    @Test
    public void testRemoveFilter() {
        FilterDescriptor filterDescriptor =
                new FilterDescriptor(FilterCondition.AND, "name", FilterOperator.EQUAL, "frank");

        DynamicQuery<Student> dynamicQuery = new DynamicQuery<>(Student.class);
        dynamicQuery.addFilter(filterDescriptor);
        assertEquals(1, dynamicQuery.getFilters().length);

        dynamicQuery.removeFilter(filterDescriptor);
        assertEquals(0, dynamicQuery.getFilters().length);
    }

    @Test
    public void testAddSort() {
        SortDescriptor sort = new SortDescriptor("name", SortDirection.DESC);

        DynamicQuery<Student> dynamicQuery = new DynamicQuery<>(Student.class);
        dynamicQuery.addSort(sort);

        assertEquals(1, dynamicQuery.getSorts().length);
    }

    @Test
    public void testRemoveSort() {
        SortDescriptor sort = new SortDescriptor("name", SortDirection.DESC);

        DynamicQuery<Student> dynamicQuery = new DynamicQuery<>(Student.class);
        dynamicQuery.addSort(sort);

        assertEquals(1, dynamicQuery.getSorts().length);

        dynamicQuery.removeSort(sort);
        assertEquals(0, dynamicQuery.getSorts().length);
    }
}