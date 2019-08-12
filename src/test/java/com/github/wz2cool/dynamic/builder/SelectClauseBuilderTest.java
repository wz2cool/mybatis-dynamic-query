package com.github.wz2cool.dynamic.builder;

import com.github.wz2cool.dynamic.*;
import com.github.wz2cool.dynamic.model.ExampleModel;
import com.github.wz2cool.dynamic.model.Student;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;

import static com.github.wz2cool.dynamic.builder.DynamicQueryBuilderHelper.desc;
import static com.github.wz2cool.dynamic.builder.DynamicQueryBuilderHelper.isEqual;


public class SelectClauseBuilderTest {

    @Test
    public void selectPropertiesTest() {
        SelectClauseBuilder<Student> selectClauseBuilder = new DynamicQueryBuilder<Student>()
                .select(Student::getName, Student::getAge);
        String[] properties = selectClauseBuilder.getSelectedProperties();
        Assert.assertArrayEquals(new String[]{"name", "age"}, properties);
    }

    @Test
    public void selectAllTest() {
        SelectClauseBuilder<Student> selectClauseBuilder = new DynamicQueryBuilder<Student>()
                .selectAll();
        String[] properties = selectClauseBuilder.getSelectedProperties();
        Assert.assertArrayEquals(new String[]{}, properties);
    }

    @Test
    public void buildTest() {
        DynamicQuery<Student> dynamicQuery = new DynamicQueryBuilder<Student>()
                .select(Student::getName, Student::getAge)
                .build();

        String[] properties = dynamicQuery.getSelectedProperties();
        Assert.assertArrayEquals(new String[]{"name", "age"}, properties);
    }

    @Test
    public void selectTest() {
        DynamicQuery<Student> dynamicQuery = new DynamicQueryBuilder<Student>()
                .select(Student::getName, Student::getAge)
                .select(Student::getNote)
                .build();
        String[] properties = dynamicQuery.getSelectedProperties();
        Assert.assertArrayEquals(new String[]{"name", "age", "note"}, properties);
    }

    @Test
    public void whereBigDecimalTest() {
        BigDecimal filterValue = BigDecimal.valueOf(1);
        WhereClauseBuilder<ExampleModel> whereClauseBuilder = new DynamicQueryBuilder<ExampleModel>()
                .selectAll()
                .where(ExampleModel::getP1, isEqual(filterValue));
        FilterDescriptor filter = (FilterDescriptor) whereClauseBuilder.getFilters()[0];
        Assert.assertEquals("p1", filter.getPropertyName());
        Assert.assertEquals(FilterOperator.EQUAL, filter.getOperator());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void whereByteTest() {
        Byte filterValue = 1;
        WhereClauseBuilder<ExampleModel> whereClauseBuilder = new DynamicQueryBuilder<ExampleModel>()
                .selectAll()
                .where(ExampleModel::getP2, isEqual(filterValue));
        FilterDescriptor filter = (FilterDescriptor) whereClauseBuilder.getFilters()[0];
        Assert.assertEquals("p2", filter.getPropertyName());
        Assert.assertEquals(FilterOperator.EQUAL, filter.getOperator());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void whereDateTest() {
        Date filterValue = new Date();
        WhereClauseBuilder<ExampleModel> whereClauseBuilder = new DynamicQueryBuilder<ExampleModel>()
                .selectAll()
                .where(ExampleModel::getP3, isEqual(filterValue));
        FilterDescriptor filter = (FilterDescriptor) whereClauseBuilder.getFilters()[0];
        Assert.assertEquals("p3", filter.getPropertyName());
        Assert.assertEquals(FilterOperator.EQUAL, filter.getOperator());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void whereDoubleTest() {
        Double filterValue = 1.1d;
        WhereClauseBuilder<ExampleModel> whereClauseBuilder = new DynamicQueryBuilder<ExampleModel>()
                .selectAll()
                .where(ExampleModel::getP4, isEqual(filterValue));
        FilterDescriptor filter = (FilterDescriptor) whereClauseBuilder.getFilters()[0];
        Assert.assertEquals("p4", filter.getPropertyName());
        Assert.assertEquals(FilterOperator.EQUAL, filter.getOperator());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void whereFloatTest() {
        Float filterValue = 1.1f;
        WhereClauseBuilder<ExampleModel> whereClauseBuilder = new DynamicQueryBuilder<ExampleModel>()
                .selectAll()
                .where(ExampleModel::getP5, isEqual(filterValue));
        FilterDescriptor filter = (FilterDescriptor) whereClauseBuilder.getFilters()[0];
        Assert.assertEquals("p5", filter.getPropertyName());
        Assert.assertEquals(FilterOperator.EQUAL, filter.getOperator());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void whereIntegerTest() {
        Integer filterValue = 1;
        WhereClauseBuilder<ExampleModel> whereClauseBuilder = new DynamicQueryBuilder<ExampleModel>()
                .selectAll()
                .where(ExampleModel::getP6, isEqual(filterValue));
        FilterDescriptor filter = (FilterDescriptor) whereClauseBuilder.getFilters()[0];
        Assert.assertEquals("p6", filter.getPropertyName());
        Assert.assertEquals(FilterOperator.EQUAL, filter.getOperator());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void whereLongTest() {
        Long filterValue = 1L;
        WhereClauseBuilder<ExampleModel> whereClauseBuilder = new DynamicQueryBuilder<ExampleModel>()
                .selectAll()
                .where(ExampleModel::getP7, isEqual(filterValue));
        FilterDescriptor filter = (FilterDescriptor) whereClauseBuilder.getFilters()[0];
        Assert.assertEquals("p7", filter.getPropertyName());
        Assert.assertEquals(FilterOperator.EQUAL, filter.getOperator());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void whereShortTest() {
        Short filterValue = 1;
        WhereClauseBuilder<ExampleModel> whereClauseBuilder = new DynamicQueryBuilder<ExampleModel>()
                .selectAll()
                .where(ExampleModel::getP8, isEqual(filterValue));
        FilterDescriptor filter = (FilterDescriptor) whereClauseBuilder.getFilters()[0];
        Assert.assertEquals("p8", filter.getPropertyName());
        Assert.assertEquals(FilterOperator.EQUAL, filter.getOperator());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void whereStringTest() {
        String filterValue = "1";
        WhereClauseBuilder<ExampleModel> whereClauseBuilder = new DynamicQueryBuilder<ExampleModel>()
                .selectAll()
                .where(ExampleModel::getP9, isEqual(filterValue));
        FilterDescriptor filter = (FilterDescriptor) whereClauseBuilder.getFilters()[0];
        Assert.assertEquals("p9", filter.getPropertyName());
        Assert.assertEquals(FilterOperator.EQUAL, filter.getOperator());
        Assert.assertEquals(filterValue, filter.getValue());
    }

    @Test
    public void orderByTest() {
        OrderByClauseBuilder<Student> orderByClauseBuilder = new DynamicQueryBuilder<Student>()
                .selectAll()
                .orderBy(Student::getAge);
        SortDescriptor sortDescriptor = (SortDescriptor) orderByClauseBuilder.getSorts()[0];
        Assert.assertEquals("age", sortDescriptor.getPropertyName());
        Assert.assertEquals(SortDirection.ASC, sortDescriptor.getSortDirection());

        OrderByClauseBuilder<Student> orderByClauseBuilder1 = new DynamicQueryBuilder<Student>()
                .selectAll()
                .orderBy(Student::getAge, desc());
        SortDescriptor sortDescriptor1 = (SortDescriptor) orderByClauseBuilder1.getSorts()[0];
        Assert.assertEquals("age", sortDescriptor1.getPropertyName());
        Assert.assertEquals(SortDirection.DESC, sortDescriptor1.getSortDirection());
    }
}
