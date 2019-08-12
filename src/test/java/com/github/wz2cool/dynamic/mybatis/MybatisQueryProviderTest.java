package com.github.wz2cool.dynamic.mybatis;

import com.github.wz2cool.dynamic.*;
import com.github.wz2cool.dynamic.model.Student;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.view.ProductView;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

public class MybatisQueryProviderTest {

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
        ageSort.setPropertyName("age");
        ageSort.setSortDirection(SortDirection.DESC);
        Map<String, Object> result = MybatisQueryProvider.getSortQueryParamMap(Student.class, "sortExpression", ageSort);
        assertEquals("age DESC", result.get("sortExpression"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetSortQueryParamMapThrowNull() throws Exception {
        SortDescriptor ageSort = new SortDescriptor();
        ageSort.setPropertyName("age");
        ageSort.setSortDirection(SortDirection.DESC);
        MybatisQueryProvider.getSortQueryParamMap(Student.class, "", ageSort);
    }

    @Test(expected = NullPointerException.class)
    public void testGetWhereQueryParamMapThrowNull() throws Exception {
        FilterDescriptor nameFilter = new FilterDescriptor();
        nameFilter.setPropertyName("name");
        nameFilter.setOperator(FilterOperator.EQUAL);
        nameFilter.setValue("frank");

        MybatisQueryProvider.getWhereQueryParamMap(Student.class, "", nameFilter);
    }

    @Test
    public void testGetQueryParamMap() throws Exception {
        DynamicQuery<Student> dynamicQuery = new DynamicQuery<>(Student.class);
        FilterDescriptor nameFilter =
                new FilterDescriptor(FilterCondition.AND, Student::getName, FilterOperator.EQUAL, "frank");
        SortDescriptor ageSort =
                new SortDescriptor(Student::getAge, SortDirection.DESC);
        dynamicQuery.addFilters(nameFilter);
        dynamicQuery.addSorts(ageSort);


        Method method = MybatisQueryProvider.class.getDeclaredMethod("getQueryParamMap",
                DynamicQuery.class, String.class, String.class, String.class);
        method.setAccessible(true);

        Map<String, Object> result = (Map<String, Object>) method.invoke(MybatisQueryProvider.class,
                dynamicQuery,
                "wherePlaceholder",
                "sortPlaceholder",
                "columnsPlaceholder");
        assertEquals("age DESC", result.get("sortPlaceholder"));
        assertEquals("queryColumn.note AS note, deleted AS deleted, name AS name, age AS age", result.get("columnsPlaceholder"));
    }

    @Test(expected = InvocationTargetException.class)
    public void testGetQueryParamMapThrowNull() throws Exception {
        Method method = MybatisQueryProvider.class.getDeclaredMethod("getQueryParamMap",
                DynamicQuery.class, String.class, String.class, String.class);
        method.setAccessible(true);
        method.invoke(
                MybatisQueryProvider.class,
                null,
                "wherePlaceholder",
                "sortPlaceholder",
                "columnsPlaceholder");
    }

    @Test
    public void testGetQueryColumn() throws Exception {
        String nameColumn = MybatisQueryProvider.getQueryColumn(Student::getName);
        assertEquals("name", nameColumn);

        String productIdColumn = MybatisQueryProvider.getQueryColumn(ProductView::getProductID);
        assertEquals("product.product_id", productIdColumn);
    }
}
