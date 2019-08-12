package com.github.wz2cool.dynamic.builder;

import com.github.wz2cool.dynamic.DynamicQuery;
import com.github.wz2cool.dynamic.FilterCondition;
import com.github.wz2cool.dynamic.FilterDescriptor;
import com.github.wz2cool.dynamic.model.Bug;
import com.github.wz2cool.dynamic.model.ExampleModel;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;

import static com.github.wz2cool.dynamic.builder.DynamicQueryBuilderHelper.*;

public class DynamicQueryBuilderTest {

    @Test
    public void testBigDecimalAnd() {
        DynamicQuery<ExampleModel> dynamicQuery = new DynamicQueryBuilder<ExampleModel>()
                .selectAll()
                .where(ExampleModel::getP1, notEqual(BigDecimal.ONE))
                .and(ExampleModel::getP1, isEqual(BigDecimal.TEN))
                .build();

        FilterDescriptor filter = (FilterDescriptor) dynamicQuery.getFilters()[1];
        Assert.assertEquals(FilterCondition.AND, filter.getCondition());
        Assert.assertEquals("p1", filter.getPropertyName());
        Assert.assertEquals(BigDecimal.TEN, filter.getValue());
    }

    @Test
    public void testByteAnd() {
        Byte filterValue = 1;
        DynamicQuery<ExampleModel> dynamicQuery = new DynamicQueryBuilder<ExampleModel>()
                .selectAll()
                .where(ExampleModel::getP1, notEqual(BigDecimal.ONE))
                .and(ExampleModel::getP2, isEqual(filterValue))
                .build();

        FilterDescriptor filter = (FilterDescriptor) dynamicQuery.getFilters()[1];
        Assert.assertEquals(FilterCondition.AND, filter.getCondition());
        Assert.assertEquals("p2", filter.getPropertyName());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void testDateAnd() {
        Date filterValue = new Date();
        DynamicQuery<ExampleModel> dynamicQuery = new DynamicQueryBuilder<ExampleModel>()
                .selectAll()
                .where(ExampleModel::getP1, notEqual(BigDecimal.ONE))
                .and(ExampleModel::getP3, isEqual(filterValue))
                .build();

        FilterDescriptor filter = (FilterDescriptor) dynamicQuery.getFilters()[1];
        Assert.assertEquals(FilterCondition.AND, filter.getCondition());
        Assert.assertEquals("p3", filter.getPropertyName());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void testDoubleAnd() {
        Double filterValue = 1.1d;
        DynamicQuery<ExampleModel> dynamicQuery = new DynamicQueryBuilder<ExampleModel>()
                .selectAll()
                .where(ExampleModel::getP1, notEqual(BigDecimal.ONE))
                .and(ExampleModel::getP4, isEqual(filterValue))
                .build();

        FilterDescriptor filter = (FilterDescriptor) dynamicQuery.getFilters()[1];
        Assert.assertEquals(FilterCondition.AND, filter.getCondition());
        Assert.assertEquals("p4", filter.getPropertyName());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void testFloatAnd() {
        Float filterValue = 1.1f;
        DynamicQuery<ExampleModel> dynamicQuery = new DynamicQueryBuilder<ExampleModel>()
                .selectAll()
                .where(ExampleModel::getP1, notEqual(BigDecimal.ONE))
                .and(ExampleModel::getP5, isEqual(filterValue))
                .build();

        FilterDescriptor filter = (FilterDescriptor) dynamicQuery.getFilters()[1];
        Assert.assertEquals(FilterCondition.AND, filter.getCondition());
        Assert.assertEquals("p5", filter.getPropertyName());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void testIntegerAnd() {
        Integer filterValue = 1;
        DynamicQuery<ExampleModel> dynamicQuery = new DynamicQueryBuilder<ExampleModel>()
                .selectAll()
                .where(ExampleModel::getP1, notEqual(BigDecimal.ONE))
                .and(ExampleModel::getP6, isEqual(filterValue))
                .build();

        FilterDescriptor filter = (FilterDescriptor) dynamicQuery.getFilters()[1];
        Assert.assertEquals(FilterCondition.AND, filter.getCondition());
        Assert.assertEquals("p6", filter.getPropertyName());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void testLongAnd() {
        Long filterValue = 1L;
        DynamicQuery<ExampleModel> dynamicQuery = new DynamicQueryBuilder<ExampleModel>()
                .selectAll()
                .where(ExampleModel::getP1, notEqual(BigDecimal.ONE))
                .and(ExampleModel::getP7, isEqual(filterValue))
                .build();

        FilterDescriptor filter = (FilterDescriptor) dynamicQuery.getFilters()[1];
        Assert.assertEquals(FilterCondition.AND, filter.getCondition());
        Assert.assertEquals("p7", filter.getPropertyName());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void testShortAnd() {
        Short filterValue = 1;
        DynamicQuery<ExampleModel> dynamicQuery = new DynamicQueryBuilder<ExampleModel>()
                .selectAll()
                .where(ExampleModel::getP1, notEqual(BigDecimal.ONE))
                .and(ExampleModel::getP8, isEqual(filterValue))
                .build();

        FilterDescriptor filter = (FilterDescriptor) dynamicQuery.getFilters()[1];
        Assert.assertEquals(FilterCondition.AND, filter.getCondition());
        Assert.assertEquals("p8", filter.getPropertyName());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void testStringAnd() {
        String filterValue = "1";
        DynamicQuery<ExampleModel> dynamicQuery = new DynamicQueryBuilder<ExampleModel>()
                .selectAll()
                .where(ExampleModel::getP1, notEqual(BigDecimal.ONE))
                .and(ExampleModel::getP9, isEqual(filterValue))
                .build();

        FilterDescriptor filter = (FilterDescriptor) dynamicQuery.getFilters()[1];
        Assert.assertEquals(FilterCondition.AND, filter.getCondition());
        Assert.assertEquals("p9", filter.getPropertyName());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void testBigDecimalOr() {
        DynamicQuery<ExampleModel> dynamicQuery = new DynamicQueryBuilder<ExampleModel>()
                .selectAll()
                .where(ExampleModel::getP1, notEqual(BigDecimal.ONE))
                .or(ExampleModel::getP1, isEqual(BigDecimal.TEN))
                .build();

        FilterDescriptor filter = (FilterDescriptor) dynamicQuery.getFilters()[1];
        Assert.assertEquals(FilterCondition.OR, filter.getCondition());
        Assert.assertEquals("p1", filter.getPropertyName());
        Assert.assertEquals(BigDecimal.TEN, filter.getValue());
    }

    @Test
    public void testByteOr() {
        Byte filterValue = 1;
        DynamicQuery<ExampleModel> dynamicQuery = new DynamicQueryBuilder<ExampleModel>()
                .selectAll()
                .where(ExampleModel::getP1, notEqual(BigDecimal.ONE))
                .or(ExampleModel::getP2, isEqual(filterValue))
                .build();

        FilterDescriptor filter = (FilterDescriptor) dynamicQuery.getFilters()[1];
        Assert.assertEquals(FilterCondition.OR, filter.getCondition());
        Assert.assertEquals("p2", filter.getPropertyName());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void testDateOr() {
        Date filterValue = new Date();
        DynamicQuery<ExampleModel> dynamicQuery = new DynamicQueryBuilder<ExampleModel>()
                .selectAll()
                .where(ExampleModel::getP1, notEqual(BigDecimal.ONE))
                .or(ExampleModel::getP3, isEqual(filterValue))
                .build();

        FilterDescriptor filter = (FilterDescriptor) dynamicQuery.getFilters()[1];
        Assert.assertEquals(FilterCondition.OR, filter.getCondition());
        Assert.assertEquals("p3", filter.getPropertyName());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void testDoubleOr() {
        Double filterValue = 1.1d;
        DynamicQuery<ExampleModel> dynamicQuery = new DynamicQueryBuilder<ExampleModel>()
                .selectAll()
                .where(ExampleModel::getP1, notEqual(BigDecimal.ONE))
                .or(ExampleModel::getP4, isEqual(filterValue))
                .build();

        FilterDescriptor filter = (FilterDescriptor) dynamicQuery.getFilters()[1];
        Assert.assertEquals(FilterCondition.OR, filter.getCondition());
        Assert.assertEquals("p4", filter.getPropertyName());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void testFloatOr() {
        Float filterValue = 1.1f;
        DynamicQuery<ExampleModel> dynamicQuery = new DynamicQueryBuilder<ExampleModel>()
                .selectAll()
                .where(ExampleModel::getP1, notEqual(BigDecimal.ONE))
                .or(ExampleModel::getP5, isEqual(filterValue))
                .build();

        FilterDescriptor filter = (FilterDescriptor) dynamicQuery.getFilters()[1];
        Assert.assertEquals(FilterCondition.OR, filter.getCondition());
        Assert.assertEquals("p5", filter.getPropertyName());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void testIntegerOr() {
        Integer filterValue = 1;
        DynamicQuery<ExampleModel> dynamicQuery = new DynamicQueryBuilder<ExampleModel>()
                .selectAll()
                .where(ExampleModel::getP1, notEqual(BigDecimal.ONE))
                .or(ExampleModel::getP6, isEqual(filterValue))
                .build();

        FilterDescriptor filter = (FilterDescriptor) dynamicQuery.getFilters()[1];
        Assert.assertEquals(FilterCondition.OR, filter.getCondition());
        Assert.assertEquals("p6", filter.getPropertyName());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void testLongOr() {
        Long filterValue = 1L;
        DynamicQuery<ExampleModel> dynamicQuery = new DynamicQueryBuilder<ExampleModel>()
                .selectAll()
                .where(ExampleModel::getP1, notEqual(BigDecimal.ONE))
                .or(ExampleModel::getP7, isEqual(filterValue))
                .build();

        FilterDescriptor filter = (FilterDescriptor) dynamicQuery.getFilters()[1];
        Assert.assertEquals(FilterCondition.OR, filter.getCondition());
        Assert.assertEquals("p7", filter.getPropertyName());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void testShortOr() {
        Short filterValue = 1;
        DynamicQuery<ExampleModel> dynamicQuery = new DynamicQueryBuilder<ExampleModel>()
                .selectAll()
                .where(ExampleModel::getP1, notEqual(BigDecimal.ONE))
                .or(ExampleModel::getP8, isEqual(filterValue))
                .build();

        FilterDescriptor filter = (FilterDescriptor) dynamicQuery.getFilters()[1];
        Assert.assertEquals(FilterCondition.OR, filter.getCondition());
        Assert.assertEquals("p8", filter.getPropertyName());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void testStringOr() {
        String filterValue = "1";
        DynamicQuery<ExampleModel> dynamicQuery = new DynamicQueryBuilder<ExampleModel>()
                .selectAll()
                .where(ExampleModel::getP1, notEqual(BigDecimal.ONE))
                .or(ExampleModel::getP9, isEqual(filterValue))
                .build();

        FilterDescriptor filter = (FilterDescriptor) dynamicQuery.getFilters()[1];
        Assert.assertEquals(FilterCondition.OR, filter.getCondition());
        Assert.assertEquals("p9", filter.getPropertyName());
        Assert.assertEquals(filterValue, filter.getValue());
    }
}
