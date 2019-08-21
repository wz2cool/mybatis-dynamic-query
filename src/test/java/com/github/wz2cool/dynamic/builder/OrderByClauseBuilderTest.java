package com.github.wz2cool.dynamic.builder;

import com.github.wz2cool.dynamic.DynamicQuery;
import com.github.wz2cool.dynamic.SortDescriptor;
import com.github.wz2cool.dynamic.SortDirection;
import com.github.wz2cool.dynamic.model.Student;
import org.junit.Assert;
import org.junit.Test;

import static com.github.wz2cool.dynamic.builder.DynamicQueryBuilderHelper.desc;
import static com.github.wz2cool.dynamic.builder.DynamicQueryBuilderHelper.isEqual;

public class OrderByClauseBuilderTest {

    @Test
    public void thenByTest() {
        DynamicQuery<Student> dynamicQuery = DynamicQueryBuilder.create(Student.class)
                .selectAll()
                .where(Student::getName, isEqual("frank"))
                .orderBy(Student::getAge)
                .thenBy(Student::getName, desc())
                .thenBy(Student::isDeleted)
                .build();

        SortDescriptor sort1 = (SortDescriptor) dynamicQuery.getSorts()[0];
        SortDescriptor sort2 = (SortDescriptor) dynamicQuery.getSorts()[1];
        SortDescriptor sort3 = (SortDescriptor) dynamicQuery.getSorts()[2];

        Assert.assertEquals("age", sort1.getPropertyName());
        Assert.assertEquals(SortDirection.ASC, sort1.getDirection());

        Assert.assertEquals("name", sort2.getPropertyName());
        Assert.assertEquals(SortDirection.DESC, sort2.getDirection());

        Assert.assertEquals("deleted", sort3.getPropertyName());
        Assert.assertEquals(SortDirection.ASC, sort3.getDirection());
    }
}
