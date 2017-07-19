package com.frwan.query.dynamic.mybatis;

import com.frwan.query.dynamic.*;
import com.frwan.query.exception.PropertyNotFoundInternalException;
import com.frwan.query.model.Student;
import org.junit.Test;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

/**
 * Created by Frank on 7/12/2017.
 */
public class QueryHelperTest {

    QueryHelper queryHelper = new QueryHelper(DatabaseType.MYSQL);
    @Test
    public void TestToSortExpression() {
        SortDescriptor nameSort = new SortDescriptor("name", SortDirection.DESC);
        SortDescriptor ageSort = new SortDescriptor("age", SortDirection.ASC);

        String result = queryHelper.toSortExpression(Student.class, nameSort);
        assertEquals("name DESC", result);

        result = queryHelper.toSortExpression(Student.class, nameSort, ageSort);
        assertEquals("name DESC, age ASC", result);

        result = queryHelper.toSortExpression(Student.class);
        assertEquals("", result);

        result = queryHelper.toSortExpression(null);
        assertEquals("", result);
    }

    @Test
    public void TestCleanFilterValue() {
        String result = queryHelper.cleanFilterValue(null);
        assertEquals("", result);

        result = queryHelper.cleanFilterValue("");
        assertEquals("", result);

        result = queryHelper.cleanFilterValue("\"hello\"");
        assertEquals("hello", result);

        result = queryHelper.cleanFilterValue("`hello`");
        assertEquals("hello", result);

        result = queryHelper.cleanFilterValue("'hello'");
        assertEquals("hello", result);
    }

    @Test
    public void TestCleanFilterValues() {
        String[] result = queryHelper.cleanFilterValues(null, "hello", 1);
        assertEquals(3, result.length);
        assertEquals("", result[0]);
        assertEquals("hello", result[1]);
        assertEquals("1", result[2]);

        result = queryHelper.cleanFilterValues();
        assertEquals(0, result.length);
    }

    @Test
    public void TestGetFilterValuesInOperator() {
        FilterDescriptor filterDescriptor = new FilterDescriptor(
                FilterCondition.AND,
                "name",
                FilterOperator.IN,
                new String[]{"frank", "Marry"});

        String[] result = queryHelper.getFilterValues(filterDescriptor);
        assertEquals(2, result.length);
        assertEquals("frank", result[0]);
        assertEquals("Marry", result[1]);
    }

    @Test(expected = InvalidParameterException.class)
    public void TestGetFilterValuesInvalidInOperator() {
        FilterDescriptor filterDescriptor = new FilterDescriptor(
                FilterCondition.AND,
                "name",
                FilterOperator.IN,
                "Marry");
        queryHelper.getFilterValues(filterDescriptor);
    }

    @Test
    public void TestGetFilterValuesBetweenOperator() {
        FilterDescriptor filterDescriptor = new FilterDescriptor(
                FilterCondition.AND,
                "age",
                FilterOperator.BETWEEN,
                new int[]{20, 30});

        String[] result = queryHelper.getFilterValues(filterDescriptor);
        assertEquals(2, result.length);
        assertEquals("20", result[0]);
        assertEquals("30", result[1]);
    }

    @Test(expected = InvalidParameterException.class)
    public void TestGetFilterValuesInvalidBetweenOperator() {
        FilterDescriptor filterDescriptor = new FilterDescriptor(
                FilterCondition.AND,
                "age",
                FilterOperator.BETWEEN,
                "noArray");

        queryHelper.getFilterValues(filterDescriptor);
    }

    @Test(expected = InvalidParameterException.class)
    public void TestGetFilterValuesInvalidCountBetweenOperator() {
        FilterDescriptor filterDescriptor = new FilterDescriptor(
                FilterCondition.AND,
                "age",
                FilterOperator.BETWEEN,
                new int[]{20, 30, 40, 50});

        queryHelper.getFilterValues(filterDescriptor);
    }

    @Test(expected = InvalidParameterException.class)
    public void TestGetFilterValuesInvalidOtherOperator() {
        FilterDescriptor filterDescriptor = new FilterDescriptor(
                FilterCondition.AND,
                "age",
                FilterOperator.EQUAL,
                new int[]{20, 30, 40, 50});

        queryHelper.getFilterValues(filterDescriptor);
    }

    @Test
    public void TestGetFilterValuesSingleValue() {
        FilterDescriptor filterDescriptor = new FilterDescriptor(
                FilterCondition.AND,
                "age",
                FilterOperator.EQUAL,
                10);

        String[] result = queryHelper.getFilterValues(filterDescriptor);
        assertEquals(1, result.length);
        assertEquals("10", result[0]);


        filterDescriptor.setValue(null);
        result = queryHelper.getFilterValues(filterDescriptor);
        assertEquals(1, result.length);
        assertEquals(null, result[0]);
    }

    @Test
    public void TestGenerateFilterExpressionForEqual() {
        FilterDescriptor filterDescriptor =
                new FilterDescriptor(FilterCondition.AND, "name", FilterOperator.EQUAL, "frank");

        String result = queryHelper.generateFilterExpression(Student.class, filterDescriptor, "placeholder");
        assertEquals("name = #{placeholder}", result);

        filterDescriptor.setValue(null);
        result = queryHelper.generateFilterExpression(Student.class, filterDescriptor, "placeholder");
        assertEquals("name IS NULL", result);
    }

    @Test(expected = PropertyNotFoundInternalException.class)
    public void TestGenerateFilterExpressionForEqualPropertyNotFound() {
        FilterDescriptor filterDescriptor =
                new FilterDescriptor(FilterCondition.AND, "notProperty", FilterOperator.EQUAL, "frank");

        queryHelper.generateFilterExpression(Student.class, filterDescriptor, "placeholder");
    }

    @Test
    public void TestGenerateFilterExpressionLessThan() {
        FilterDescriptor filterDescriptor =
                new FilterDescriptor(FilterCondition.AND, "age", FilterOperator.LESS_THAN, 30);

        String result = queryHelper.generateFilterExpression(Student.class, filterDescriptor, "placeholder");
        assertEquals("age < #{placeholder}", result);
    }

    @Test
    public void TestGenerateFilterExpressionLESS_THAN_OR_EQUAL() {
        FilterDescriptor filterDescriptor =
                new FilterDescriptor(FilterCondition.AND, "age", FilterOperator.LESS_THAN_OR_EQUAL, 30);

        String result = queryHelper.generateFilterExpression(Student.class, filterDescriptor, "placeholder");
        assertEquals("age <= #{placeholder}", result);
    }

    @Test
    public void TestGenerateFilterExpressionNOT_EQUAL() {
        FilterDescriptor filterDescriptor =
                new FilterDescriptor(FilterCondition.AND, "age", FilterOperator.NOT_EQUAL, 30);

        String result = queryHelper.generateFilterExpression(Student.class, filterDescriptor, "placeholder");
        assertEquals("age <> #{placeholder}", result);

        filterDescriptor.setValue(null);
        result = queryHelper.generateFilterExpression(Student.class, filterDescriptor, "placeholder");
        assertEquals("age IS NOT NULL", result);
    }

    @Test
    public void TestGenerateFilterExpressionGREATER_THAN_OR_EQUAL() {
        FilterDescriptor filterDescriptor =
                new FilterDescriptor(FilterCondition.AND, "age", FilterOperator.GREATER_THAN_OR_EQUAL, 30);

        String result = queryHelper.generateFilterExpression(Student.class, filterDescriptor, "placeholder");
        assertEquals("age >= #{placeholder}", result);
    }

    @Test
    public void TestGenerateFilterExpressionGREATER_THAN() {
        FilterDescriptor filterDescriptor =
                new FilterDescriptor(FilterCondition.AND, "age", FilterOperator.GREATER_THAN, 30);

        String result = queryHelper.generateFilterExpression(Student.class, filterDescriptor, "placeholder");
        assertEquals("age > #{placeholder}", result);
    }

    @Test
    public void TestGenerateFilterExpressionSTART_WITH() {
        FilterDescriptor filterDescriptor =
                new FilterDescriptor(FilterCondition.AND, "name", FilterOperator.START_WITH, "frank");

        String result = queryHelper.generateFilterExpression(Student.class, filterDescriptor, "placeholder");
        assertEquals("name LIKE #{placeholder}", result);
    }

    @Test
    public void TestGenerateFilterExpressionEND_WITH() {
        FilterDescriptor filterDescriptor =
                new FilterDescriptor(FilterCondition.AND, "name", FilterOperator.END_WITH, "frank");

        String result = queryHelper.generateFilterExpression(Student.class, filterDescriptor, "placeholder");
        assertEquals("name LIKE #{placeholder}", result);
    }

    @Test
    public void TestGenerateFilterExpressionCONTAINS() {
        FilterDescriptor filterDescriptor =
                new FilterDescriptor(FilterCondition.AND, "name", FilterOperator.CONTAINS, "frank");

        String result = queryHelper.generateFilterExpression(Student.class, filterDescriptor, "placeholder");
        assertEquals("name LIKE #{placeholder}", result);
    }

    @Test
    public void TestGenerateFilterExpressionIN() {
        FilterDescriptor filterDescriptor =
                new FilterDescriptor(FilterCondition.AND, "name", FilterOperator.IN, new String[]{"frank", "Marry"});

        String result = queryHelper.generateFilterExpression(Student.class, filterDescriptor,
                "placeholder1", "placeholder2");
        assertEquals("name IN (#{placeholder1},#{placeholder2})", result);

        result = queryHelper.generateFilterExpression(Student.class, filterDescriptor);
        assertEquals("", result);
    }

    @Test
    public void TestGenerateFilterExpressionNOT_IN() {
        FilterDescriptor filterDescriptor =
                new FilterDescriptor(FilterCondition.AND, "name", FilterOperator.NOT_IN, new String[]{"frank", "Marry"});

        String result = queryHelper.generateFilterExpression(Student.class, filterDescriptor,
                "placeholder1", "placeholder2");
        assertEquals("name NOT IN (#{placeholder1},#{placeholder2})", result);

        result = queryHelper.generateFilterExpression(Student.class, filterDescriptor);
        assertEquals("", result);
    }

    @Test
    public void TestGenerateFilterExpressionBETWEEN() {
        FilterDescriptor filterDescriptor =
                new FilterDescriptor(FilterCondition.AND, "age", FilterOperator.BETWEEN, new int[]{1, 100});

        String result = queryHelper.generateFilterExpression(Student.class, filterDescriptor,
                "placeholder1", "placeholder2");
        assertEquals("age BETWEEN #{placeholder1} AND #{placeholder2}", result);
    }

    @Test(expected = InvalidParameterException.class)
    public void TestGenerateFilterExpressionBETWEENInvalid() {
        FilterDescriptor filterDescriptor =
                new FilterDescriptor(FilterCondition.AND, "age", FilterOperator.BETWEEN, new int[]{1, 100});

        queryHelper.generateFilterExpression(Student.class, filterDescriptor,
                "placeholder1");
    }

    @Test
    public void TestGenerateFilterExpressionBITWISE_GREATER_ZERO() {
        FilterDescriptor filterDescriptor =
                new FilterDescriptor(FilterCondition.AND, "age", FilterOperator.BITAND_GREATER_ZERO, 2);

        String result = queryHelper.generateFilterExpression(Student.class, filterDescriptor,
                "placeholder1");
        assertEquals("age & #{placeholder1} > 0", result);
    }

    @Test
    public void TestGenerateFilterExpressionBITWISE_EQUAL_ZERO() {
        FilterDescriptor filterDescriptor =
                new FilterDescriptor(FilterCondition.AND, "age", FilterOperator.BITAND_EQUAL_ZERO, 2);

        String result = queryHelper.generateFilterExpression(Student.class, filterDescriptor,
                "placeholder1");
        assertEquals("age & #{placeholder1} = 0", result);
    }

    @Test
    public void TestGenerateFilterExpressionBITWISE_EQUAL_INPUT() {
        FilterDescriptor filterDescriptor =
                new FilterDescriptor(FilterCondition.AND, "age", FilterOperator.BITAND_EQUAL_INPUT, 2);

        String result = queryHelper.generateFilterExpression(Student.class, filterDescriptor,
                "placeholder1");
        assertEquals("age & #{placeholder1} = #{placeholder1}", result);
    }

    @Test
    public void TestToWhereExpressionForFilterDescriptor() {
        FilterDescriptor filterDescriptor =
                new FilterDescriptor(FilterCondition.AND, "age", FilterOperator.EQUAL, 20);

        ParamExpression result = queryHelper.toWhereExpression(Student.class, filterDescriptor);
        assertEquals(true, result.getExpression().startsWith("age = #{param_age_EQUAL"));
        assertEquals(true, result.getParamMap().keySet().iterator().next().startsWith("param_age_EQUAL"));
        assertEquals("20", result.getParamMap().values().iterator().next());

        filterDescriptor.setOperator(FilterOperator.BETWEEN);
        filterDescriptor.setValue(new int[]{20, 30});
        result = queryHelper.toWhereExpression(Student.class, filterDescriptor);
        assertEquals(true, result.getExpression().startsWith("age BETWEEN #{param_age_BETWEEN"));
        assertEquals(true, result.getParamMap().keySet().iterator().next().startsWith("param_age_BETWEEN"));
        List<Object> values = new ArrayList<>();
        values.addAll(result.getParamMap().values());
        assertEquals("20", values.get(0));
        assertEquals("30", values.get(1));

        filterDescriptor.setOperator(FilterOperator.IN);
        filterDescriptor.setValue(new int[]{20, 30, 40, 50});
        result = queryHelper.toWhereExpression(Student.class, filterDescriptor);
        assertEquals(true, result.getExpression().startsWith("age IN (#{param_age_IN"));
        assertEquals(true, result.getParamMap().keySet().iterator().next().startsWith("param_age_IN"));
        values = new ArrayList<>();
        values.addAll(result.getParamMap().values());
        assertEquals("20", values.get(0));
        assertEquals("30", values.get(1));
        assertEquals("40", values.get(2));
        assertEquals("50", values.get(3));

        result = queryHelper.toWhereExpression(Student.class, (FilterDescriptorBase) null);
        assertEquals("", result.getExpression());

        result = queryHelper.toWhereExpression(Student.class, (FilterDescriptorBase[]) null);
        assertEquals("", result.getExpression());
    }

    @Test
    public void TestToWhereExpressionForFilterGroupDescriptor() {
        FilterGroupDescriptor filterGroupDescriptor = new FilterGroupDescriptor();
        FilterDescriptor ageFilter =
                new FilterDescriptor(FilterCondition.AND, "age", FilterOperator.EQUAL, 20);
        FilterDescriptor nameFilter =
                new FilterDescriptor(FilterCondition.AND, "name", FilterOperator.EQUAL, "frank");
        filterGroupDescriptor.addFilters(ageFilter, nameFilter);

        ParamExpression result = queryHelper.toWhereExpression(Student.class, filterGroupDescriptor);
        String pattern = "^\\(age = #\\{param_age_EQUAL_\\w+\\} AND name = #\\{param_name_EQUAL_\\w+\\}\\)$";
        boolean test = Pattern.matches(pattern, result.getExpression());
        assertEquals(true, test);
    }
}
