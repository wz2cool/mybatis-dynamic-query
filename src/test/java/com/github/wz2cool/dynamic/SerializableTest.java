package com.github.wz2cool.dynamic;

import com.github.wz2cool.dynamic.model.Student;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;

public class SerializableTest {

    @Test
    public void testSerializeFilterDescriptor() throws Exception {
        FilterDescriptor filterDescriptor =
                new FilterDescriptor(Student::getAge, FilterOperator.EQUAL, 1);
        byte[] bytes = pickle(filterDescriptor);
        FilterDescriptor copy = unpickle(bytes, FilterDescriptor.class);
        assertEquals(filterDescriptor.getPropertyName(), copy.getPropertyName());
        assertEquals(filterDescriptor.getValue(), copy.getValue());

        BaseFilterDescriptor[] filters = new BaseFilterDescriptor[]{filterDescriptor};
        byte[] arrayBtypes = pickle(filters);
        BaseFilterDescriptor[] filtersCopy = unpickle(arrayBtypes, BaseFilterDescriptor[].class);
        copy = (FilterDescriptor) filtersCopy[0];
        assertEquals(filterDescriptor.getPropertyName(), copy.getPropertyName());
        assertEquals(filterDescriptor.getValue(), copy.getValue());
    }

    @Test
    public void testSerializeFilterGroupDescriptor() throws Exception {
        FilterDescriptor ageFilter =
                new FilterDescriptor(Student::getAge, FilterOperator.EQUAL, 1);
        FilterGroupDescriptor filterGroupDescriptor = new FilterGroupDescriptor();
        filterGroupDescriptor.addFilters(ageFilter);
        byte[] bytes = pickle(filterGroupDescriptor);
        FilterGroupDescriptor groupCopy = unpickle(bytes, FilterGroupDescriptor.class);
        FilterDescriptor copy = (FilterDescriptor) groupCopy.getFilters()[0];
        assertEquals(ageFilter.getPropertyName(), copy.getPropertyName());
        assertEquals(ageFilter.getValue(), copy.getValue());

        BaseFilterDescriptor[] filters = new BaseFilterDescriptor[]{filterGroupDescriptor};
        byte[] arrayBtypes = pickle(filters);
        BaseFilterDescriptor[] filtersCopy = unpickle(arrayBtypes, BaseFilterDescriptor[].class);
        groupCopy = (FilterGroupDescriptor) filtersCopy[0];
        copy = (FilterDescriptor) groupCopy.getFilters()[0];
        assertEquals(ageFilter.getPropertyName(), copy.getPropertyName());
        assertEquals(ageFilter.getValue(), copy.getValue());
    }

    @Test
    public void testSerializeCustomFilterDescriptor() throws Exception {
        CustomFilterDescriptor customFilterDescriptor = new CustomFilterDescriptor();
        customFilterDescriptor.setExpression("age > {0} AND age < {1}");
        customFilterDescriptor.setParams(20, 30);

        byte[] bytes = pickle(customFilterDescriptor);
        CustomFilterDescriptor customCopy = unpickle(bytes, CustomFilterDescriptor.class);
        assertEquals(customFilterDescriptor.getExpression(), customCopy.getExpression());
        assertEquals(customFilterDescriptor.getParams()[0], customCopy.getParams()[0]);
        assertEquals(customFilterDescriptor.getParams()[1], customCopy.getParams()[1]);


        BaseFilterDescriptor[] filters = new BaseFilterDescriptor[]{customFilterDescriptor};
        byte[] arrayBytes = pickle(filters);
        BaseFilterDescriptor[] filtersCopy = unpickle(arrayBytes, BaseFilterDescriptor[].class);
        customCopy = (CustomFilterDescriptor) filtersCopy[0];
        assertEquals(customFilterDescriptor.getExpression(), customCopy.getExpression());
        assertEquals(customFilterDescriptor.getParams()[0], customCopy.getParams()[0]);
        assertEquals(customFilterDescriptor.getParams()[1], customCopy.getParams()[1]);
    }

    @Test
    public void testSerializeComplexFilter() throws Exception {
        FilterDescriptor nameFilter =
                new FilterDescriptor(Student::getName, FilterOperator.START_WITH, "frank");
        FilterDescriptor ageFilter =
                new FilterDescriptor(Student::getAge, FilterOperator.GREATER_THAN, 20);
        FilterDescriptor ageFilter2 =
                new FilterDescriptor(Student::getAge, FilterOperator.LESS_THAN, 30);
        FilterGroupDescriptor ageGroupFilter = new FilterGroupDescriptor();
        ageGroupFilter.addFilters(ageFilter);
        ageGroupFilter.addFilters(ageFilter2);

        CustomFilterDescriptor noteFilter = new CustomFilterDescriptor();
        noteFilter.setExpression("note test expression");
        noteFilter.setParams(20, 30);

        BaseFilterDescriptor[] filters = new BaseFilterDescriptor[]{nameFilter, ageGroupFilter, noteFilter};
        byte[] arrayBytes = pickle(filters);
        BaseFilterDescriptor[] filtersCopy = unpickle(arrayBytes, BaseFilterDescriptor[].class);
        assertEquals(true, filtersCopy[0] instanceof FilterDescriptor);
        assertEquals(true, filters[1] instanceof FilterGroupDescriptor);
        assertEquals(true, filters[2] instanceof CustomFilterDescriptor);

        FilterDescriptor nameFilterCopy = (FilterDescriptor) filtersCopy[0];
        FilterGroupDescriptor ageGroupFilterCopy = (FilterGroupDescriptor) filtersCopy[1];
        CustomFilterDescriptor noteFilterCopy = (CustomFilterDescriptor) filtersCopy[2];

        assertEquals(nameFilter.getPropertyName(), nameFilterCopy.getPropertyName());
        assertEquals(nameFilter.getValue(), nameFilterCopy.getValue());
        assertEquals(ageGroupFilter.getFilters().length, ageGroupFilterCopy.getFilters().length);
        assertEquals(noteFilter.getExpression(), noteFilterCopy.getExpression());
        assertEquals(noteFilter.getParams().length, noteFilterCopy.getParams().length);
    }


    private static <T extends Serializable> byte[] pickle(T obj)
            throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.close();
        return baos.toByteArray();
    }

    private static <T extends Serializable> T unpickle(byte[] b, Class<T> cl)
            throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object o = ois.readObject();
        return cl.cast(o);
    }
}