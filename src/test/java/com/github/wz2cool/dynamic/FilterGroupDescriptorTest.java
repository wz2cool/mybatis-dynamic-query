package com.github.wz2cool.dynamic;

import com.github.wz2cool.dynamic.model.ExampleModel;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;

import static com.github.wz2cool.dynamic.builder.DynamicQueryBuilderHelper.isEqual;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        groupDescriptor.addFilters(idFilter);
        assertTrue(groupDescriptor.getFilters().length > 0);
    }

    @Test
    public void removeFiltersTest() {
        FilterDescriptor idFilter =
                new FilterDescriptor(FilterCondition.AND, "id", FilterOperator.EQUAL, 1);

        FilterGroupDescriptor groupDescriptor = new FilterGroupDescriptor();
        groupDescriptor.addFilters(idFilter);
        assertTrue(groupDescriptor.getFilters().length > 0);

        groupDescriptor.removeFilters(idFilter);
        assertTrue(groupDescriptor.getFilters().length == 0);
    }

    @Test
    public void testAndBigDecimal() {
        FilterGroupDescriptor<ExampleModel> filterGroupDescriptor = new FilterGroupDescriptor<>();
        filterGroupDescriptor.and(ExampleModel::getP1, isEqual(BigDecimal.ONE));

        FilterDescriptor filterDescriptor = (FilterDescriptor) filterGroupDescriptor.getFilters()[0];
        assertEquals(FilterCondition.AND, filterDescriptor.getCondition());
        assertEquals("p1", filterDescriptor.getPropertyName());
        assertEquals(FilterOperator.EQUAL, filterDescriptor.getOperator());
        assertEquals(BigDecimal.ONE, filterDescriptor.getValue());
    }

    @Test
    public void testOrBigDecimal() {
        FilterGroupDescriptor<ExampleModel> filterGroupDescriptor = new FilterGroupDescriptor<>();
        filterGroupDescriptor.or(ExampleModel::getP1, isEqual(BigDecimal.ONE));

        FilterDescriptor filterDescriptor = (FilterDescriptor) filterGroupDescriptor.getFilters()[0];
        assertEquals(FilterCondition.OR, filterDescriptor.getCondition());
        assertEquals("p1", filterDescriptor.getPropertyName());
        assertEquals(FilterOperator.EQUAL, filterDescriptor.getOperator());
        assertEquals(BigDecimal.ONE, filterDescriptor.getValue());
    }

    @Test
    public void testAndByte() {
        byte filterValue = 1;
        FilterGroupDescriptor<ExampleModel> filterGroupDescriptor = new FilterGroupDescriptor<>();
        filterGroupDescriptor.and(ExampleModel::getP2, isEqual(filterValue));

        FilterDescriptor filterDescriptor = (FilterDescriptor) filterGroupDescriptor.getFilters()[0];
        assertEquals(FilterCondition.AND, filterDescriptor.getCondition());
        assertEquals("p2", filterDescriptor.getPropertyName());
        assertEquals(FilterOperator.EQUAL, filterDescriptor.getOperator());
        assertEquals(filterValue, filterDescriptor.getValue());
    }

    @Test
    public void testOrByte() {
        byte filterValue = 1;
        FilterGroupDescriptor<ExampleModel> filterGroupDescriptor = new FilterGroupDescriptor<>();
        filterGroupDescriptor.or(ExampleModel::getP2, isEqual(filterValue));

        FilterDescriptor filterDescriptor = (FilterDescriptor) filterGroupDescriptor.getFilters()[0];
        assertEquals(FilterCondition.OR, filterDescriptor.getCondition());
        assertEquals("p2", filterDescriptor.getPropertyName());
        assertEquals(FilterOperator.EQUAL, filterDescriptor.getOperator());
        assertEquals(filterValue, filterDescriptor.getValue());
    }

    @Test
    public void testAndDate() {
        Timestamp filterValue = new Timestamp(System.currentTimeMillis());
        FilterGroupDescriptor<ExampleModel> filterGroupDescriptor = new FilterGroupDescriptor<>();
        filterGroupDescriptor.and(ExampleModel::getP3, isEqual(filterValue));

        FilterDescriptor filterDescriptor = (FilterDescriptor) filterGroupDescriptor.getFilters()[0];
        assertEquals(FilterCondition.AND, filterDescriptor.getCondition());
        assertEquals("p3", filterDescriptor.getPropertyName());
        assertEquals(FilterOperator.EQUAL, filterDescriptor.getOperator());
        assertEquals(filterValue, filterDescriptor.getValue());
    }

    @Test
    public void testOrDate() {
        Timestamp filterValue = new Timestamp(System.currentTimeMillis());
        FilterGroupDescriptor<ExampleModel> filterGroupDescriptor = new FilterGroupDescriptor<>();
        filterGroupDescriptor.or(ExampleModel::getP3, isEqual(filterValue));

        FilterDescriptor filterDescriptor = (FilterDescriptor) filterGroupDescriptor.getFilters()[0];
        assertEquals(FilterCondition.OR, filterDescriptor.getCondition());
        assertEquals("p3", filterDescriptor.getPropertyName());
        assertEquals(FilterOperator.EQUAL, filterDescriptor.getOperator());
        assertEquals(filterValue, filterDescriptor.getValue());
    }

    @Test
    public void testAndDouble() {
        Double filterValue = 1.2;
        FilterGroupDescriptor<ExampleModel> filterGroupDescriptor = new FilterGroupDescriptor<>();
        filterGroupDescriptor.and(ExampleModel::getP4, isEqual(filterValue));

        FilterDescriptor filterDescriptor = (FilterDescriptor) filterGroupDescriptor.getFilters()[0];
        assertEquals(FilterCondition.AND, filterDescriptor.getCondition());
        assertEquals("p4", filterDescriptor.getPropertyName());
        assertEquals(FilterOperator.EQUAL, filterDescriptor.getOperator());
        assertEquals(filterValue, filterDescriptor.getValue());
    }

    @Test
    public void testOrDouble() {
        Double filterValue = 1.2;
        FilterGroupDescriptor<ExampleModel> filterGroupDescriptor = new FilterGroupDescriptor<>();
        filterGroupDescriptor.or(ExampleModel::getP4, isEqual(filterValue));

        FilterDescriptor filterDescriptor = (FilterDescriptor) filterGroupDescriptor.getFilters()[0];
        assertEquals(FilterCondition.OR, filterDescriptor.getCondition());
        assertEquals("p4", filterDescriptor.getPropertyName());
        assertEquals(FilterOperator.EQUAL, filterDescriptor.getOperator());
        assertEquals(filterValue, filterDescriptor.getValue());
    }

    @Test
    public void testAndFloat() {
        Float filterValue = 1.2f;
        FilterGroupDescriptor<ExampleModel> filterGroupDescriptor = new FilterGroupDescriptor<>();
        filterGroupDescriptor.and(ExampleModel::getP5, isEqual(filterValue));

        FilterDescriptor filterDescriptor = (FilterDescriptor) filterGroupDescriptor.getFilters()[0];
        assertEquals(FilterCondition.AND, filterDescriptor.getCondition());
        assertEquals("p5", filterDescriptor.getPropertyName());
        assertEquals(FilterOperator.EQUAL, filterDescriptor.getOperator());
        assertEquals(filterValue, filterDescriptor.getValue());
    }

    @Test
    public void testOrFloat() {
        Float filterValue = 1.2f;
        FilterGroupDescriptor<ExampleModel> filterGroupDescriptor = new FilterGroupDescriptor<>();
        filterGroupDescriptor.or(ExampleModel::getP5, isEqual(filterValue));

        FilterDescriptor filterDescriptor = (FilterDescriptor) filterGroupDescriptor.getFilters()[0];
        assertEquals(FilterCondition.OR, filterDescriptor.getCondition());
        assertEquals("p5", filterDescriptor.getPropertyName());
        assertEquals(FilterOperator.EQUAL, filterDescriptor.getOperator());
        assertEquals(filterValue, filterDescriptor.getValue());
    }

    @Test
    public void testAndInteger() {
        Integer filterValue = 1;
        FilterGroupDescriptor<ExampleModel> filterGroupDescriptor = new FilterGroupDescriptor<>();
        filterGroupDescriptor.and(ExampleModel::getP6, isEqual(filterValue));

        FilterDescriptor filterDescriptor = (FilterDescriptor) filterGroupDescriptor.getFilters()[0];
        assertEquals(FilterCondition.AND, filterDescriptor.getCondition());
        assertEquals("p6", filterDescriptor.getPropertyName());
        assertEquals(FilterOperator.EQUAL, filterDescriptor.getOperator());
        assertEquals(filterValue, filterDescriptor.getValue());
    }

    @Test
    public void testOrInteger() {
        Integer filterValue = 1;
        FilterGroupDescriptor<ExampleModel> filterGroupDescriptor = new FilterGroupDescriptor<>();
        filterGroupDescriptor.or(ExampleModel::getP6, isEqual(filterValue));

        FilterDescriptor filterDescriptor = (FilterDescriptor) filterGroupDescriptor.getFilters()[0];
        assertEquals(FilterCondition.OR, filterDescriptor.getCondition());
        assertEquals("p6", filterDescriptor.getPropertyName());
        assertEquals(FilterOperator.EQUAL, filterDescriptor.getOperator());
        assertEquals(filterValue, filterDescriptor.getValue());
    }

    @Test
    public void testAndLong() {
        Long filterValue = 1L;
        FilterGroupDescriptor<ExampleModel> filterGroupDescriptor = new FilterGroupDescriptor<>();
        filterGroupDescriptor.and(ExampleModel::getP7, isEqual(filterValue));

        FilterDescriptor filterDescriptor = (FilterDescriptor) filterGroupDescriptor.getFilters()[0];
        assertEquals(FilterCondition.AND, filterDescriptor.getCondition());
        assertEquals("p7", filterDescriptor.getPropertyName());
        assertEquals(FilterOperator.EQUAL, filterDescriptor.getOperator());
        assertEquals(filterValue, filterDescriptor.getValue());
    }

    @Test
    public void testOrLong() {
        Long filterValue = 1L;
        FilterGroupDescriptor<ExampleModel> filterGroupDescriptor = new FilterGroupDescriptor<>();
        filterGroupDescriptor.or(ExampleModel::getP7, isEqual(filterValue));

        FilterDescriptor filterDescriptor = (FilterDescriptor) filterGroupDescriptor.getFilters()[0];
        assertEquals(FilterCondition.OR, filterDescriptor.getCondition());
        assertEquals("p7", filterDescriptor.getPropertyName());
        assertEquals(FilterOperator.EQUAL, filterDescriptor.getOperator());
        assertEquals(filterValue, filterDescriptor.getValue());
    }

    @Test
    public void testAndShort() {
        Short filterValue = 1;
        FilterGroupDescriptor<ExampleModel> filterGroupDescriptor = new FilterGroupDescriptor<>();
        filterGroupDescriptor.and(ExampleModel::getP8, isEqual(filterValue));

        FilterDescriptor filterDescriptor = (FilterDescriptor) filterGroupDescriptor.getFilters()[0];
        assertEquals(FilterCondition.AND, filterDescriptor.getCondition());
        assertEquals("p8", filterDescriptor.getPropertyName());
        assertEquals(FilterOperator.EQUAL, filterDescriptor.getOperator());
        assertEquals(filterValue, filterDescriptor.getValue());
    }

    @Test
    public void testOrShort() {
        Short filterValue = 1;
        FilterGroupDescriptor<ExampleModel> filterGroupDescriptor = new FilterGroupDescriptor<>();
        filterGroupDescriptor.or(ExampleModel::getP8, isEqual(filterValue));

        FilterDescriptor filterDescriptor = (FilterDescriptor) filterGroupDescriptor.getFilters()[0];
        assertEquals(FilterCondition.OR, filterDescriptor.getCondition());
        assertEquals("p8", filterDescriptor.getPropertyName());
        assertEquals(FilterOperator.EQUAL, filterDescriptor.getOperator());
        assertEquals(filterValue, filterDescriptor.getValue());
    }

    @Test
    public void testAndString() {
        String filterValue = "frank";
        FilterGroupDescriptor<ExampleModel> filterGroupDescriptor = new FilterGroupDescriptor<>();
        filterGroupDescriptor.and(ExampleModel::getP9, isEqual(filterValue));

        FilterDescriptor filterDescriptor = (FilterDescriptor) filterGroupDescriptor.getFilters()[0];
        assertEquals(FilterCondition.AND, filterDescriptor.getCondition());
        assertEquals("p9", filterDescriptor.getPropertyName());
        assertEquals(FilterOperator.EQUAL, filterDescriptor.getOperator());
        assertEquals(filterValue, filterDescriptor.getValue());
    }

    @Test
    public void testOrString() {
        String filterValue = "frank";
        FilterGroupDescriptor<ExampleModel> filterGroupDescriptor = new FilterGroupDescriptor<>();
        filterGroupDescriptor.or(ExampleModel::getP9, isEqual(filterValue));

        FilterDescriptor filterDescriptor = (FilterDescriptor) filterGroupDescriptor.getFilters()[0];
        assertEquals(FilterCondition.OR, filterDescriptor.getCondition());
        assertEquals("p9", filterDescriptor.getPropertyName());
        assertEquals(FilterOperator.EQUAL, filterDescriptor.getOperator());
        assertEquals(filterValue, filterDescriptor.getValue());
    }

    @Test
    public void testAndGroupBegin() {
        BigDecimal filterValue = BigDecimal.ONE;
        FilterGroupDescriptor<ExampleModel> filterGroupDescriptor = new FilterGroupDescriptor<>();
        filterGroupDescriptor.andGroupBegin()
                .and(ExampleModel::getP1, isEqual(filterValue))
                .groupEnd();

        FilterGroupDescriptor<ExampleModel> internalFilterGroup = (FilterGroupDescriptor<ExampleModel>) filterGroupDescriptor.getFilters()[0];
        FilterDescriptor filterDescriptor = (FilterDescriptor) internalFilterGroup.getFilters()[0];
        assertEquals(FilterCondition.AND, filterDescriptor.getCondition());
        assertEquals("p1", filterDescriptor.getPropertyName());
        assertEquals(FilterOperator.EQUAL, filterDescriptor.getOperator());
        assertEquals(filterValue, filterDescriptor.getValue());
    }

    @Test
    public void testOrGroupBegin() {
        BigDecimal filterValue = BigDecimal.ONE;
        FilterGroupDescriptor<ExampleModel> filterGroupDescriptor = new FilterGroupDescriptor<>();
        filterGroupDescriptor.orGroupBegin()
                .and(ExampleModel::getP1, isEqual(filterValue))
                .groupEnd();

        FilterGroupDescriptor<ExampleModel> internalFilterGroup = (FilterGroupDescriptor<ExampleModel>) filterGroupDescriptor.getFilters()[0];
        FilterDescriptor filterDescriptor = (FilterDescriptor) internalFilterGroup.getFilters()[0];
        assertEquals(FilterCondition.OR, internalFilterGroup.getCondition());
        assertEquals(FilterCondition.AND, filterDescriptor.getCondition());
        assertEquals("p1", filterDescriptor.getPropertyName());
        assertEquals(FilterOperator.EQUAL, filterDescriptor.getOperator());
        assertEquals(filterValue, filterDescriptor.getValue());
    }
}