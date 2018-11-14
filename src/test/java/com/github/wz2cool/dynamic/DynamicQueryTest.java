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
        dynamicQuery.addFilters(filterDescriptor);
        assertEquals(1, dynamicQuery.getFilters().length);
    }

    @Test
    public void testRemoveFilter() {
        FilterDescriptor filterDescriptor =
                new FilterDescriptor(FilterCondition.AND, "name", FilterOperator.EQUAL, "frank");

        DynamicQuery<Student> dynamicQuery = new DynamicQuery<>(Student.class);
        dynamicQuery.addFilters(filterDescriptor);
        assertEquals(1, dynamicQuery.getFilters().length);

        dynamicQuery.removeFilter(filterDescriptor);
        assertEquals(0, dynamicQuery.getFilters().length);
    }

    @Test
    public void testAddSort() {
        SortDescriptor sort = new SortDescriptor("name", SortDirection.DESC);

        DynamicQuery<Student> dynamicQuery = new DynamicQuery<>(Student.class);
        dynamicQuery.addSorts(sort);

        assertEquals(1, dynamicQuery.getSorts().length);
    }

    @Test
    public void testRemoveSort() {
        SortDescriptor sort = new SortDescriptor("name", SortDirection.DESC);

        DynamicQuery<Student> dynamicQuery = new DynamicQuery<>(Student.class);
        dynamicQuery.addSorts(sort);

        assertEquals(1, dynamicQuery.getSorts().length);

        dynamicQuery.removeSort(sort);
        assertEquals(0, dynamicQuery.getSorts().length);
    }

    @Test
    public void testCreateQuery() {
        DynamicQuery<Student> query = DynamicQuery.createQuery(Student.class);
        assertEquals(true, query != null);
    }

    @Test
    public void testAddFilterDescriptor() {
        DynamicQuery<Student> query = DynamicQuery.createQuery(Student.class)
                .addFilterDescriptor(FilterCondition.OR, Student::getName, FilterOperator.EQUAL, "frank");
        FilterDescriptor filterDescriptor = (FilterDescriptor) query.getFilters()[0];
        assertEquals(FilterCondition.OR, filterDescriptor.getCondition());
        assertEquals("name", filterDescriptor.getPropertyPath());
        assertEquals(FilterOperator.EQUAL, filterDescriptor.getOperator());
        assertEquals("frank", filterDescriptor.getValue());
    }

    @Test
    public void testLinkOperation() {
        DynamicQuery<Student> query = DynamicQuery.createQuery(Student.class)
                .addFilterDescriptor(Student::getName, FilterOperator.EQUAL, "frank")
                .addSortDescriptor(Student::getAge, SortDirection.DESC);

        FilterDescriptor filterDescriptor = (FilterDescriptor) query.getFilters()[0];
        assertEquals(FilterCondition.AND, filterDescriptor.getCondition());
        assertEquals("name", filterDescriptor.getPropertyPath());
        assertEquals(FilterOperator.EQUAL, filterDescriptor.getOperator());
        assertEquals("frank", filterDescriptor.getValue());

        SortDescriptor sortDescriptor = (SortDescriptor) query.getSorts()[0];
        assertEquals("age", sortDescriptor.getPropertyPath());
        assertEquals(SortDirection.DESC, sortDescriptor.getSortDirection());
    }
}