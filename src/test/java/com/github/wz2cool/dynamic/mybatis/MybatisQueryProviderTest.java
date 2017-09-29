package com.github.wz2cool.dynamic.mybatis;

import com.github.wz2cool.dynamic.*;
import com.github.wz2cool.model.Student;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

public class MybatisQueryProviderTest {

    @Test(expected = InvocationTargetException.class)
    public void TestMybatisQueryProvider() throws Exception {
        Constructor<MybatisQueryProvider> c = MybatisQueryProvider.class.getDeclaredConstructor();
        c.setAccessible(true);
        c.newInstance();
    }

    @Test
    public void TestGetWhereExpression() throws Exception {
        FilterDescriptor filterDescriptor =
                new FilterDescriptor(FilterCondition.AND, "age", FilterOperator.EQUAL, 30);

        ParamExpression result = MybatisQueryProvider.getWhereExpression(Student.class, filterDescriptor);
        String pattern = "^\\(age = #\\{param_age_EQUAL_\\w+\\}\\)$";
        assertEquals(true, Pattern.matches(pattern, result.getExpression()));
        assertEquals(30, result.getParamMap().values().iterator().next());
    }

    @Test
    public void TestGetSortExpression() throws Exception {
        SortDescriptor sortDescriptor =
                new SortDescriptor("age", SortDirection.DESC);

        ParamExpression result = MybatisQueryProvider.getSortExpression(Student.class, sortDescriptor);
        assertEquals("age DESC", result.getExpression());
    }

    @Test
    public void testGetSortQueryParamMap() throws Exception {
        SortDescriptor ageSort = new SortDescriptor();
        ageSort.setPropertyPath("age");
        ageSort.setSortDirection(SortDirection.DESC);
        Map<String, Object> result = MybatisQueryProvider.getSortQueryParamMap(Student.class, "sortExpression", ageSort);
        assertEquals("age DESC", result.get("sortExpression"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetSortQueryParamMapThrowNull() throws Exception {
        SortDescriptor ageSort = new SortDescriptor();
        ageSort.setPropertyPath("age");
        ageSort.setSortDirection(SortDirection.DESC);
        MybatisQueryProvider.getSortQueryParamMap(Student.class, "", ageSort);
    }

    @Test(expected = NullPointerException.class)
    public void testGetWhereQueryParamMapThrowNull() throws Exception {
        FilterDescriptor nameFilter = new FilterDescriptor();
        nameFilter.setPropertyPath("name");
        nameFilter.setOperator(FilterOperator.EQUAL);
        nameFilter.setValue("frank");

        MybatisQueryProvider.getWhereQueryParamMap(Student.class, "", nameFilter);
    }

    @Test
    public void testGetQueryParamMap() throws Exception {
        DynamicQuery<Student> dynamicQuery = new DynamicQuery<>(Student.class);
        FilterDescriptor nameFilter =
                new FilterDescriptor(FilterCondition.AND,
                        Student.class, Student::getName, FilterOperator.EQUAL, "frank");
        SortDescriptor ageSort =
                new SortDescriptor(Student.class, Student::getAge, SortDirection.DESC);
        dynamicQuery.addFilters(nameFilter);
        dynamicQuery.addSorts(ageSort);

        Map<String, Object> result = MybatisQueryProvider.getQueryParamMap(
                dynamicQuery,
                "wherePlaceholder",
                "sortPlaceholder",
                "columnsPlaceholder");
    }

    @Test(expected = NullPointerException.class)
    public void testGetQueryParamMapThrowNull() throws Exception {
        Map<String, Object> result = MybatisQueryProvider.getQueryParamMap(
                null,
                "wherePlaceholder",
                "sortPlaceholder",
                "columnsPlaceholder");
    }

}
