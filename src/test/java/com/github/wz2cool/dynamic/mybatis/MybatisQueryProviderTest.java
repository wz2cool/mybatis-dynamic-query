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
    MybatisQueryProvider mybatisQueryProvider = new MybatisQueryProvider(DatabaseType.MYSQL);

    @Test
    public void TestGetInsertExpression() throws Exception {
        Student newStudent = new Student();
        newStudent.setName("frank");
        newStudent.setAge(30);
        newStudent.setNote("this is a test");

        ParamExpression result = mybatisQueryProvider.getInsertExpression(newStudent);
        assertEquals("INSERT INTO student (dbColumn.note, name, age) VALUES (#{note}, #{name}, #{age})", result.getExpression());
        assertEquals("frank", result.getParamMap().get("name"));
        assertEquals(30, result.getParamMap().get("age"));
        assertEquals("this is a test", result.getParamMap().get("note"));
    }

    @Test(expected = NullPointerException.class)
    public void TestGEtInsertExpressionNullPointer() throws Exception {
        mybatisQueryProvider.getInsertExpression(null);
    }

    @Test
    public void TestUpdateExpression() throws Exception {
        Student newStudent = new Student();
        newStudent.setName("frank");
        newStudent.setAge(30);
        newStudent.setNote("this is a test");
        ParamExpression result = mybatisQueryProvider.getUpdateExpression(newStudent);
        assertEquals("UPDATE student SET `dbColumn.note`=#{note}, `name`=#{name}, `age`=#{age}", result.getExpression());

        FilterDescriptor filterDescriptor = new FilterDescriptor(FilterCondition.AND, "age", FilterOperator.EQUAL, 20);
        result = mybatisQueryProvider.getUpdateExpression(newStudent, filterDescriptor);

        String pattern = "^UPDATE student SET `dbColumn\\.note`=#\\{note\\}, `name`=#\\{name\\}, `age`=#\\{age\\} WHERE \\(age = #\\{param_age_EQUAL_\\w+}\\)$";
        assertEquals(true, Pattern.matches(pattern, result.getExpression()));
        assertEquals("frank", result.getParamMap().get("name"));
        assertEquals(30, result.getParamMap().get("age"));
        assertEquals("this is a test", result.getParamMap().get("note"));
    }

    @Test(expected = NullPointerException.class)
    public void TestUpdateExpressionNullPointer() throws Exception {
        mybatisQueryProvider.getUpdateExpression(null);
    }

    @Test
    public void TestToPlaceholder() {
        String result = mybatisQueryProvider.toPlaceholder("test");
        assertEquals("#{test}", result);
    }

    @Test
    public void TestGetWhereExpression() throws Exception {
        FilterDescriptor filterDescriptor =
                new FilterDescriptor(FilterCondition.AND, "age", FilterOperator.EQUAL, 30);

        ParamExpression result = mybatisQueryProvider.getWhereExpression(Student.class, filterDescriptor);
        String pattern = "^\\(age = #\\{param_age_EQUAL_\\w+\\}\\)$";
        assertEquals(true, Pattern.matches(pattern, result.getExpression()));
        assertEquals("30", result.getParamMap().values().iterator().next());
    }

    @Test
    public void TestGetSortExpression() throws Exception {
        SortDescriptor sortDescriptor =
                new SortDescriptor("age", SortDirection.DESC);

        String result = mybatisQueryProvider.getSortExpression(Student.class, sortDescriptor);
        assertEquals("age DESC", result);
    }

    @Test
    public void TestValidFilters() throws Exception {
        mybatisQueryProvider.validFilters(Student.class);

        FilterDescriptor ageFilter =
                new FilterDescriptor(FilterCondition.AND, "age", FilterOperator.EQUAL, 20);
        mybatisQueryProvider.validFilters(Student.class, ageFilter);

        FilterGroupDescriptor filterGroupDescriptor = new FilterGroupDescriptor();
        FilterDescriptor nameFilter =
                new FilterDescriptor(FilterCondition.AND, "name", FilterOperator.START_WITH, "a");
        filterGroupDescriptor.addFilters(ageFilter, nameFilter);

        mybatisQueryProvider.validFilters(Student.class, filterGroupDescriptor);
    }

    @Test(expected = PropertyNotFoundException.class)
    public void TestValidFiltersNotFound() throws Exception {
        FilterDescriptor filterDescriptor =
                new FilterDescriptor(FilterCondition.AND, "NotFoundException", FilterOperator.EQUAL, 20);
        mybatisQueryProvider.validFilters(Student.class, filterDescriptor);
    }

    @Test
    public void TestValidSorts() throws Exception {
        SortDescriptor ageSort = new SortDescriptor("name", SortDirection.DESC);
        mybatisQueryProvider.validSorts(Student.class, ageSort);

        mybatisQueryProvider.validSorts(Student.class);
    }

    @Test(expected = PropertyNotFoundException.class)
    public void TestValidSortNotFound() throws Exception {
        SortDescriptor ageSort = new SortDescriptor("NotFoundException", SortDirection.DESC);
        mybatisQueryProvider.validSorts(Student.class, ageSort);
    }

    @Test
    public void TestGetFieldValue() {
        Student student = new Student();
        student.setName("frank");

        Field nameField =
                EntityHelper.getPropertyField("name", ReflectHelper.getProperties(student.getClass()));
        nameField.setAccessible(true);
        Object result = mybatisQueryProvider.getFieldValue(student, nameField);
        assertEquals("frank", result);
    }

    @Test(expected = InternalRuntimeException.class)
    public void TestGetFieldValueThrowException() {
        Student student = new Student();
        student.setName("frank");

        Field nameField =
                EntityHelper.getPropertyField("name", ReflectHelper.getProperties(student.getClass()));

        Object result = mybatisQueryProvider.getFieldValue(student, nameField);
        assertEquals("frank", result);
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
