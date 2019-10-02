package com.github.wz2cool.dynamic;

import static com.github.wz2cool.dynamic.builder.DynamicQueryBuilderHelper.*;

import com.github.wz2cool.dynamic.model.Student;
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

        dynamicQuery.removeFilters(filterDescriptor);
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

        dynamicQuery.removeSorts(sort);
        assertEquals(0, dynamicQuery.getSorts().length);
    }

    @Test
    public void testCreateQuery() {
        DynamicQuery<Student> query = DynamicQuery.createQuery(Student.class);
        assertEquals(0, query.getFilters().length);
    }

    @Test
    public void testAddFilterDescriptor() {
        DynamicQuery<Student> query = DynamicQuery.createQuery(Student.class)
                .or(Student::getName, isEqual("frank"));
        FilterDescriptor filterDescriptor = (FilterDescriptor) query.getFilters()[0];
        assertEquals(FilterCondition.OR, filterDescriptor.getCondition());
        assertEquals("name", filterDescriptor.getPropertyName());
        assertEquals(FilterOperator.EQUAL, filterDescriptor.getOperator());
        assertEquals("frank", filterDescriptor.getValue());
    }

    @Test
    public void testLinkOperation() {
        DynamicQuery<Student> query = DynamicQuery.createQuery(Student.class)
                .and(Student::getName, isEqual("frank"))
                .sort(Student::getAge, desc());

        FilterDescriptor filterDescriptor = (FilterDescriptor) query.getFilters()[0];
        assertEquals(FilterCondition.AND, filterDescriptor.getCondition());
        assertEquals("name", filterDescriptor.getPropertyName());
        assertEquals(FilterOperator.EQUAL, filterDescriptor.getOperator());
        assertEquals("frank", filterDescriptor.getValue());

        SortDescriptor sortDescriptor = (SortDescriptor) query.getSorts()[0];
        assertEquals("age", sortDescriptor.getPropertyName());
        assertEquals(SortDirection.DESC, sortDescriptor.getDirection());

    }

    @Test
    public void testAddSelectProperty() {
        DynamicQuery<Student> query = DynamicQuery.createQuery(Student.class)
                .select(Student::getAge, Student::getName);
        String[] selectFields = query.getSelectedProperties();
        assertEquals("age", selectFields[0]);
        assertEquals("name", selectFields[1]);
    }

    @Test
    public void testAddIgnoredProperty() {
        DynamicQuery<Student> query = DynamicQuery.createQuery(Student.class)
                .ignore(Student::getAge, Student::getName);
        String[] ignoredProperties = query.getIgnoredProperties();
        assertEquals("age", ignoredProperties[0]);
        assertEquals("name", ignoredProperties[1]);
    }
}