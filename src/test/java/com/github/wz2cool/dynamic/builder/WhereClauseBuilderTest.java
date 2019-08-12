package com.github.wz2cool.dynamic.builder;

import com.github.wz2cool.dynamic.*;
import com.github.wz2cool.dynamic.model.Student;
import org.junit.Assert;
import org.junit.Test;

import static com.github.wz2cool.dynamic.builder.DynamicQueryBuilderHelper.desc;
import static com.github.wz2cool.dynamic.builder.DynamicQueryBuilderHelper.isEqual;

public class WhereClauseBuilderTest {

    @Test
    public void testWhereClauseBuilder() {
        WhereClauseBuilder<Student> whereClauseBuilder = new DynamicQueryBuilder<Student>()
                .selectAll()
                .where(Student::getName, isEqual("frank"));

        OrderByClauseBuilder<Student> orderByClauseBuilder = whereClauseBuilder.orderBy(Student::getAge);
        SortDescriptor sortDescriptor = (SortDescriptor) orderByClauseBuilder.getSorts()[0];
        Assert.assertEquals("age", sortDescriptor.getPropertyName());
        Assert.assertEquals(SortDirection.ASC, sortDescriptor.getDirection());

        OrderByClauseBuilder<Student> orderByClauseBuilder1 = whereClauseBuilder.orderBy(Student::getAge, desc());
        SortDescriptor sortDescriptor1 = (SortDescriptor) orderByClauseBuilder1.getSorts()[0];
        Assert.assertEquals("age", sortDescriptor1.getPropertyName());
        Assert.assertEquals(SortDirection.DESC, sortDescriptor1.getDirection());
    }

    @Test
    public void buildTest() {
        WhereClauseBuilder<Student> whereClauseBuilder = new DynamicQueryBuilder<Student>()
                .selectAll()
                .where(Student::getName, isEqual("frank"));

        DynamicQuery<Student> dynamicQuery = whereClauseBuilder.build();
        FilterDescriptor filterDescriptor = (FilterDescriptor) dynamicQuery.getFilters()[0];
        Assert.assertEquals("name", filterDescriptor.getPropertyName());
        Assert.assertEquals(FilterOperator.EQUAL, filterDescriptor.getOperator());
        Assert.assertEquals("frank", filterDescriptor.getValue());
    }
}
