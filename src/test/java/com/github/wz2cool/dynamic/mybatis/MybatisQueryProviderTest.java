package com.github.wz2cool.dynamic.mybatis;

import com.github.wz2cool.dynamic.*;
import com.github.wz2cool.exception.InternalRuntimeException;
import com.github.wz2cool.exception.PropertyNotFoundException;
import com.github.wz2cool.helper.ReflectHelper;
import com.github.wz2cool.model.Student;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

public class MybatisQueryProviderTest {
    MybatisQueryProvider mybatisQueryProvider = new MybatisQueryProvider();

    @Test
    public void TestGetWhereExpression() throws Exception {
        FilterDescriptor filterDescriptor =
                new FilterDescriptor(FilterCondition.AND, "age", FilterOperator.EQUAL, 30);

        ParamExpression result = mybatisQueryProvider.getWhereExpression(Student.class, filterDescriptor);
        String pattern = "^\\(age = #\\{param_age_EQUAL_\\w+\\}\\)$";
        assertEquals(true, Pattern.matches(pattern, result.getExpression()));
        assertEquals(30, result.getParamMap().values().iterator().next());
    }

    @Test
    public void TestGetSortExpression() throws Exception {
        SortDescriptor sortDescriptor =
                new SortDescriptor("age", SortDirection.DESC);

        String result = mybatisQueryProvider.getSortExpression(Student.class, sortDescriptor);
        assertEquals("age DESC", result);
    }

    @Test
    public void testGetSortQueryParamMap() throws Exception {
        SortDescriptor ageSort = new SortDescriptor();
        ageSort.setPropertyPath("age");
        ageSort.setSortDirection(SortDirection.DESC);
        Map<String, Object> result = mybatisQueryProvider.getSortQueryParamMap(Student.class, "sortExpression", ageSort);
        assertEquals("age DESC", result.get("sortExpression"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetSortQueryParamMapThrowNull() throws Exception {
        SortDescriptor ageSort = new SortDescriptor();
        ageSort.setPropertyPath("age");
        ageSort.setSortDirection(SortDirection.DESC);
        mybatisQueryProvider.getSortQueryParamMap(Student.class, "", ageSort);
    }

    @Test(expected = NullPointerException.class)
    public void testGetWhereQueryParamMapThrowNull() throws Exception {
        FilterDescriptor nameFilter = new FilterDescriptor();
        nameFilter.setPropertyPath("name");
        nameFilter.setOperator(FilterOperator.EQUAL);
        nameFilter.setValue("frank");

        mybatisQueryProvider.getWhereQueryParamMap(Student.class, "", nameFilter);
    }


}
