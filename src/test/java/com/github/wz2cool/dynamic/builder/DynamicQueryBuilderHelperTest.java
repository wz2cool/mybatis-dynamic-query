package com.github.wz2cool.dynamic.builder;

import com.github.wz2cool.dynamic.FilterCondition;
import com.github.wz2cool.dynamic.FilterDescriptor;
import com.github.wz2cool.dynamic.FilterOperator;
import com.github.wz2cool.dynamic.builder.opeartor.*;
import com.github.wz2cool.dynamic.model.ExampleModel;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;

public class DynamicQueryBuilderHelperTest {

    @Test
    public void lessThanTest() {
        LessThan<Integer> result = DynamicQueryBuilderHelper.lessThan(1);
        Assert.assertEquals(FilterOperator.LESS_THAN, result.getOperator());
        Assert.assertEquals(Integer.valueOf(1), result.getValue());
    }

    @Test
    public void lessThanOrEqualTest() {
        LessThanOrEqual<Integer> result = DynamicQueryBuilderHelper.lessThanOrEqual(1);
        Assert.assertEquals(FilterOperator.LESS_THAN_OR_EQUAL, result.getOperator());
        Assert.assertEquals(Integer.valueOf(1), result.getValue());
    }

    @Test
    public void equalTest() {
        Equal<Integer> result = DynamicQueryBuilderHelper.isEqual(1);
        Assert.assertEquals(FilterOperator.EQUAL, result.getOperator());
        Assert.assertEquals(Integer.valueOf(1), result.getValue());
    }

    @Test
    public void notEqualTest() {
        NotEqual<Integer> result = DynamicQueryBuilderHelper.notEqual(1);
        Assert.assertEquals(FilterOperator.NOT_EQUAL, result.getOperator());
        Assert.assertEquals(Integer.valueOf(1), result.getValue());
    }

    @Test
    public void greaterThanEqualTest() {
        GreaterThanOrEqual<Integer> result = DynamicQueryBuilderHelper.greaterThanOrEqual(1);
        Assert.assertEquals(FilterOperator.GREATER_THAN_OR_EQUAL, result.getOperator());
        Assert.assertEquals(Integer.valueOf(1), result.getValue());
    }

    @Test
    public void greaterThanTest() {
        GreaterThan<Integer> result = DynamicQueryBuilderHelper.greaterThan(1);
        Assert.assertEquals(FilterOperator.GREATER_THAN, result.getOperator());
        Assert.assertEquals(Integer.valueOf(1), result.getValue());
    }

    @Test
    public void startWithTest() {
        StartWith<String> result = DynamicQueryBuilderHelper.startWith("a");
        Assert.assertEquals(FilterOperator.START_WITH, result.getOperator());
        Assert.assertEquals("a", result.getValue());
    }

    @Test
    public void endWithTest() {
        EndWith<String> result = DynamicQueryBuilderHelper.endWith("a");
        Assert.assertEquals(FilterOperator.END_WITH, result.getOperator());
        Assert.assertEquals("a", result.getValue());
    }

    @Test
    public void containsTest() {
        Contains<String> result = DynamicQueryBuilderHelper.contains("a");
        Assert.assertEquals(FilterOperator.CONTAINS, result.getOperator());
        Assert.assertEquals("a", result.getValue());
    }

    @Test
    public void inTest() {
        In<String> result = DynamicQueryBuilderHelper.in("a", "b", "c");
        Assert.assertEquals(FilterOperator.IN, result.getOperator());
        Assert.assertArrayEquals(new String[]{"a", "b", "c"}, result.getValue().toArray());
    }

    @Test
    public void notInTest() {
        NotIn<String> result = DynamicQueryBuilderHelper.notIn("a", "b", "c");
        Assert.assertEquals(FilterOperator.NOT_IN, result.getOperator());
        Assert.assertArrayEquals(new String[]{"a", "b", "c"}, result.getValue().toArray());
    }

    @Test
    public void betweenTest() {
        Between<Integer> result = DynamicQueryBuilderHelper.between(1, 10);
        Assert.assertEquals(FilterOperator.BETWEEN, result.getOperator());
        Assert.assertArrayEquals(new Integer[]{1, 10}, result.getValue().toArray());
    }

    @Test
    public void bigDecimalAndTest() {
        BigDecimal filterValue = BigDecimal.ONE;
        ConditionClauseBuilder<ExampleModel> conditionClauseBuilder =
                DynamicQueryBuilderHelper.and(ExampleModel::getP1, DynamicQueryBuilderHelper.isEqual(filterValue));
        FilterDescriptor filter = (FilterDescriptor) conditionClauseBuilder.toFilter();
        Assert.assertEquals(FilterCondition.AND, filter.getCondition());
        Assert.assertEquals("p1", filter.getPropertyName());
        Assert.assertEquals(FilterOperator.EQUAL, filter.getOperator());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void byteAndTest() {
        Byte filterValue = 1;
        ConditionClauseBuilder<ExampleModel> conditionClauseBuilder =
                DynamicQueryBuilderHelper.and(ExampleModel::getP2, DynamicQueryBuilderHelper.isEqual(filterValue));
        FilterDescriptor filter = (FilterDescriptor) conditionClauseBuilder.toFilter();
        Assert.assertEquals(FilterCondition.AND, filter.getCondition());
        Assert.assertEquals("p2", filter.getPropertyName());
        Assert.assertEquals(FilterOperator.EQUAL, filter.getOperator());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void dateAndTest() {
        Date filterValue = new Date();
        ConditionClauseBuilder<ExampleModel> conditionClauseBuilder =
                DynamicQueryBuilderHelper.and(ExampleModel::getP3, DynamicQueryBuilderHelper.isEqual(filterValue));
        FilterDescriptor filter = (FilterDescriptor) conditionClauseBuilder.toFilter();
        Assert.assertEquals(FilterCondition.AND, filter.getCondition());
        Assert.assertEquals("p3", filter.getPropertyName());
        Assert.assertEquals(FilterOperator.EQUAL, filter.getOperator());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void doubleAndTest() {
        Double filterValue = 1.1d;
        ConditionClauseBuilder<ExampleModel> conditionClauseBuilder =
                DynamicQueryBuilderHelper.and(ExampleModel::getP4, DynamicQueryBuilderHelper.isEqual(filterValue));
        FilterDescriptor filter = (FilterDescriptor) conditionClauseBuilder.toFilter();
        Assert.assertEquals(FilterCondition.AND, filter.getCondition());
        Assert.assertEquals("p4", filter.getPropertyName());
        Assert.assertEquals(FilterOperator.EQUAL, filter.getOperator());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void floatAndTest() {
        Float filterValue = 1.1f;
        ConditionClauseBuilder<ExampleModel> conditionClauseBuilder =
                DynamicQueryBuilderHelper.and(ExampleModel::getP5, DynamicQueryBuilderHelper.isEqual(filterValue));
        FilterDescriptor filter = (FilterDescriptor) conditionClauseBuilder.toFilter();
        Assert.assertEquals(FilterCondition.AND, filter.getCondition());
        Assert.assertEquals("p5", filter.getPropertyName());
        Assert.assertEquals(FilterOperator.EQUAL, filter.getOperator());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void integerAndTest() {
        Integer filterValue = 1;
        ConditionClauseBuilder<ExampleModel> conditionClauseBuilder =
                DynamicQueryBuilderHelper.and(ExampleModel::getP6, DynamicQueryBuilderHelper.isEqual(filterValue));
        FilterDescriptor filter = (FilterDescriptor) conditionClauseBuilder.toFilter();
        Assert.assertEquals(FilterCondition.AND, filter.getCondition());
        Assert.assertEquals("p6", filter.getPropertyName());
        Assert.assertEquals(FilterOperator.EQUAL, filter.getOperator());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void longAndTest() {
        Long filterValue = 1L;
        ConditionClauseBuilder<ExampleModel> conditionClauseBuilder =
                DynamicQueryBuilderHelper.and(ExampleModel::getP7, DynamicQueryBuilderHelper.isEqual(filterValue));
        FilterDescriptor filter = (FilterDescriptor) conditionClauseBuilder.toFilter();
        Assert.assertEquals(FilterCondition.AND, filter.getCondition());
        Assert.assertEquals("p7", filter.getPropertyName());
        Assert.assertEquals(FilterOperator.EQUAL, filter.getOperator());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void shortAndTest() {
        short filterValue = 1;
        ConditionClauseBuilder<ExampleModel> conditionClauseBuilder =
                DynamicQueryBuilderHelper.and(ExampleModel::getP8, DynamicQueryBuilderHelper.isEqual(filterValue));
        FilterDescriptor filter = (FilterDescriptor) conditionClauseBuilder.toFilter();
        Assert.assertEquals(FilterCondition.AND, filter.getCondition());
        Assert.assertEquals("p8", filter.getPropertyName());
        Assert.assertEquals(FilterOperator.EQUAL, filter.getOperator());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void stringAndTest() {
        String filterValue = "1";
        ConditionClauseBuilder<ExampleModel> conditionClauseBuilder =
                DynamicQueryBuilderHelper.and(ExampleModel::getP9, DynamicQueryBuilderHelper.isEqual(filterValue));
        FilterDescriptor filter = (FilterDescriptor) conditionClauseBuilder.toFilter();
        Assert.assertEquals(FilterCondition.AND, filter.getCondition());
        Assert.assertEquals("p9", filter.getPropertyName());
        Assert.assertEquals(FilterOperator.EQUAL, filter.getOperator());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void bigDecimalOrTest() {
        BigDecimal filterValue = BigDecimal.ONE;
        ConditionClauseBuilder<ExampleModel> conditionClauseBuilder =
                DynamicQueryBuilderHelper.or(ExampleModel::getP1, DynamicQueryBuilderHelper.isEqual(filterValue));
        FilterDescriptor filter = (FilterDescriptor) conditionClauseBuilder.toFilter();
        Assert.assertEquals(FilterCondition.OR, filter.getCondition());
        Assert.assertEquals("p1", filter.getPropertyName());
        Assert.assertEquals(FilterOperator.EQUAL, filter.getOperator());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void byteOrTest() {
        Byte filterValue = 1;
        ConditionClauseBuilder<ExampleModel> conditionClauseBuilder =
                DynamicQueryBuilderHelper.or(ExampleModel::getP2, DynamicQueryBuilderHelper.isEqual(filterValue));
        FilterDescriptor filter = (FilterDescriptor) conditionClauseBuilder.toFilter();
        Assert.assertEquals(FilterCondition.OR, filter.getCondition());
        Assert.assertEquals("p2", filter.getPropertyName());
        Assert.assertEquals(FilterOperator.EQUAL, filter.getOperator());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void dateOrTest() {
        Date filterValue = new Date();
        ConditionClauseBuilder<ExampleModel> conditionClauseBuilder =
                DynamicQueryBuilderHelper.or(ExampleModel::getP3, DynamicQueryBuilderHelper.isEqual(filterValue));
        FilterDescriptor filter = (FilterDescriptor) conditionClauseBuilder.toFilter();
        Assert.assertEquals(FilterCondition.OR, filter.getCondition());
        Assert.assertEquals("p3", filter.getPropertyName());
        Assert.assertEquals(FilterOperator.EQUAL, filter.getOperator());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void doubleOrTest() {
        Double filterValue = 1.1d;
        ConditionClauseBuilder<ExampleModel> conditionClauseBuilder =
                DynamicQueryBuilderHelper.or(ExampleModel::getP4, DynamicQueryBuilderHelper.isEqual(filterValue));
        FilterDescriptor filter = (FilterDescriptor) conditionClauseBuilder.toFilter();
        Assert.assertEquals(FilterCondition.OR, filter.getCondition());
        Assert.assertEquals("p4", filter.getPropertyName());
        Assert.assertEquals(FilterOperator.EQUAL, filter.getOperator());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void floatOrTest() {
        Float filterValue = 1.1f;
        ConditionClauseBuilder<ExampleModel> conditionClauseBuilder =
                DynamicQueryBuilderHelper.or(ExampleModel::getP5, DynamicQueryBuilderHelper.isEqual(filterValue));
        FilterDescriptor filter = (FilterDescriptor) conditionClauseBuilder.toFilter();
        Assert.assertEquals(FilterCondition.OR, filter.getCondition());
        Assert.assertEquals("p5", filter.getPropertyName());
        Assert.assertEquals(FilterOperator.EQUAL, filter.getOperator());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void integerOrTest() {
        Integer filterValue = 1;
        ConditionClauseBuilder<ExampleModel> conditionClauseBuilder =
                DynamicQueryBuilderHelper.or(ExampleModel::getP6, DynamicQueryBuilderHelper.isEqual(filterValue));
        FilterDescriptor filter = (FilterDescriptor) conditionClauseBuilder.toFilter();
        Assert.assertEquals(FilterCondition.OR, filter.getCondition());
        Assert.assertEquals("p6", filter.getPropertyName());
        Assert.assertEquals(FilterOperator.EQUAL, filter.getOperator());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void longOrTest() {
        Long filterValue = 1L;
        ConditionClauseBuilder<ExampleModel> conditionClauseBuilder =
                DynamicQueryBuilderHelper.or(ExampleModel::getP7, DynamicQueryBuilderHelper.isEqual(filterValue));
        FilterDescriptor filter = (FilterDescriptor) conditionClauseBuilder.toFilter();
        Assert.assertEquals(FilterCondition.OR, filter.getCondition());
        Assert.assertEquals("p7", filter.getPropertyName());
        Assert.assertEquals(FilterOperator.EQUAL, filter.getOperator());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void shortOrTest() {
        short filterValue = 1;
        ConditionClauseBuilder<ExampleModel> conditionClauseBuilder =
                DynamicQueryBuilderHelper.or(ExampleModel::getP8, DynamicQueryBuilderHelper.isEqual(filterValue));
        FilterDescriptor filter = (FilterDescriptor) conditionClauseBuilder.toFilter();
        Assert.assertEquals(FilterCondition.OR, filter.getCondition());
        Assert.assertEquals("p8", filter.getPropertyName());
        Assert.assertEquals(FilterOperator.EQUAL, filter.getOperator());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void stringOrTest() {
        String filterValue = "1";
        ConditionClauseBuilder<ExampleModel> conditionClauseBuilder =
                DynamicQueryBuilderHelper.or(ExampleModel::getP9, DynamicQueryBuilderHelper.isEqual(filterValue));
        FilterDescriptor filter = (FilterDescriptor) conditionClauseBuilder.toFilter();
        Assert.assertEquals(FilterCondition.OR, filter.getCondition());
        Assert.assertEquals("p9", filter.getPropertyName());
        Assert.assertEquals(FilterOperator.EQUAL, filter.getOperator());
        Assert.assertEquals(filterValue, filter.getValue());
    }
}
