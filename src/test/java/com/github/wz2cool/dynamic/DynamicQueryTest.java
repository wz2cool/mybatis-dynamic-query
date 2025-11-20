package com.github.wz2cool.dynamic;

import static com.github.wz2cool.dynamic.builder.DynamicQueryBuilderHelper.*;

import com.github.wz2cool.dynamic.model.Student;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

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
                .orderBy(Student::getAge, desc());

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

    @Test
    public void testQueryParamWithMap() {
        DynamicQuery<Student> query = DynamicQuery.createQuery(Student.class);
        
        // 创建测试参数Map
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("param1", "value1");
        paramMap.put("param2", 123);
        paramMap.put("param3", true);
        
        // 调用queryParam方法
        query.queryParam(paramMap);
        
        // 验证参数是否正确添加到customDynamicQueryParams
        Map<String, Object> customParams = query.getCustomDynamicQueryParams();
        assertEquals("value1", customParams.get("param1"));
        assertEquals(123, customParams.get("param2"));
        assertEquals(true, customParams.get("param3"));
        assertEquals(3, customParams.size());
    }

    @Test
    public void testQueryParamWithMapAndEnableFalse() {
        DynamicQuery<Student> query = DynamicQuery.createQuery(Student.class);
        
        // 创建测试参数Map
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("param1", "value1");
        paramMap.put("param2", 123);
        
        // 调用queryParam方法，enable=false
        query.queryParam(false, paramMap);
        
        // 验证参数没有被添加到customDynamicQueryParams
        Map<String, Object> customParams = query.getCustomDynamicQueryParams();
        assertEquals(0, customParams.size());
    }

    @Test
    public void testQueryParamWithMapAndEnableTrue() {
        DynamicQuery<Student> query = DynamicQuery.createQuery(Student.class);
        
        // 创建测试参数Map
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("param1", "value1");
        paramMap.put("param2", 123);
        
        // 调用queryParam方法，enable=true
        query.queryParam(true, paramMap);
        
        // 验证参数正确添加到customDynamicQueryParams
        Map<String, Object> customParams = query.getCustomDynamicQueryParams();
        assertEquals("value1", customParams.get("param1"));
        assertEquals(123, customParams.get("param2"));
        assertEquals(2, customParams.size());
    }

    @Test
    public void testQueryParamWithMapMerge() {
        DynamicQuery<Student> query = DynamicQuery.createQuery(Student.class);
        
        // 先添加一些参数
        Map<String, Object> firstParamMap = new HashMap<>();
        firstParamMap.put("param1", "value1");
        firstParamMap.put("param2", 123);
        query.queryParam(firstParamMap);
        
        // 再添加更多参数
        Map<String, Object> secondParamMap = new HashMap<>();
        secondParamMap.put("param3", "value3");
        secondParamMap.put("param4", 456);
        query.queryParam(secondParamMap);
        
        // 验证所有参数都正确合并
        Map<String, Object> customParams = query.getCustomDynamicQueryParams();
        assertEquals("value1", customParams.get("param1"));
        assertEquals(123, customParams.get("param2"));
        assertEquals("value3", customParams.get("param3"));
        assertEquals(456, customParams.get("param4"));
        assertEquals(4, customParams.size());
    }

    @Test
    public void testQueryParamWithMapOverwrite() {
        DynamicQuery<Student> query = DynamicQuery.createQuery(Student.class);
        
        // 先添加参数
        Map<String, Object> firstParamMap = new HashMap<>();
        firstParamMap.put("param1", "old_value");
        firstParamMap.put("param2", 123);
        query.queryParam(firstParamMap);
        
        // 使用相同的key添加新值
        Map<String, Object> secondParamMap = new HashMap<>();
        secondParamMap.put("param1", "new_value");
        secondParamMap.put("param3", 456);
        query.queryParam(secondParamMap);
        
        // 验证参数被正确覆盖
        Map<String, Object> customParams = query.getCustomDynamicQueryParams();
        assertEquals("new_value", customParams.get("param1")); // 被覆盖
        assertEquals(123, customParams.get("param2"));
        assertEquals(456, customParams.get("param3"));
        assertEquals(3, customParams.size());
    }
}