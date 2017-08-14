package com.github.wz2cool.dynamic;

import com.github.wz2cool.model.Student;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class SerializableTest {

    @Test
    public void testSerializeFilterDescriptor() throws Exception {
        FilterDescriptor filterDescriptor =
                new FilterDescriptor(Student.class, Student::getAge, FilterOperator.EQUAL, 1);
        byte[] bytes = pickle(filterDescriptor);
        FilterDescriptor copy = unpickle(bytes, FilterDescriptor.class);
        assertEquals(filterDescriptor.getPropertyPath(), copy.getPropertyPath());
        assertEquals(filterDescriptor.getValue(), copy.getValue());

        FilterDescriptorBase[] filters = new FilterDescriptorBase[]{filterDescriptor};
        byte[] arrayBtypes = pickle(filters);
        FilterDescriptorBase[] filtersCopy = unpickle(arrayBtypes, FilterDescriptorBase[].class);
        copy = (FilterDescriptor) filtersCopy[0];
        assertEquals(filterDescriptor.getPropertyPath(), copy.getPropertyPath());
        assertEquals(filterDescriptor.getValue(), copy.getValue());
    }

    @Test
    public void testSerializeFilterGroupDescriptor() throws Exception {
        FilterDescriptor ageFilter =
                new FilterDescriptor(Student.class, Student::getAge, FilterOperator.EQUAL, 1);
        FilterGroupDescriptor filterGroupDescriptor = new FilterGroupDescriptor();
        filterGroupDescriptor.addFilters(ageFilter);
        byte[] bytes = pickle(filterGroupDescriptor);
        FilterGroupDescriptor groupCopy = unpickle(bytes, FilterGroupDescriptor.class);
        FilterDescriptor copy = (FilterDescriptor) groupCopy.getFilters()[0];
        assertEquals(ageFilter.getPropertyPath(), copy.getPropertyPath());
        assertEquals(ageFilter.getValue(), copy.getValue());

        FilterDescriptorBase[] filters = new FilterDescriptorBase[]{filterGroupDescriptor};
        byte[] arrayBtypes = pickle(filters);
        FilterDescriptorBase[] filtersCopy = unpickle(arrayBtypes, FilterDescriptorBase[].class);
        groupCopy = (FilterGroupDescriptor) filtersCopy[0];
        copy = (FilterDescriptor) groupCopy.getFilters()[0];
        assertEquals(ageFilter.getPropertyPath(), copy.getPropertyPath());
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


        FilterDescriptorBase[] filters = new FilterDescriptorBase[]{customFilterDescriptor};
        byte[] arrayBytes = pickle(filters);
        FilterDescriptorBase[] filtersCopy = unpickle(arrayBytes, FilterDescriptorBase[].class);
        customCopy = (CustomFilterDescriptor) filtersCopy[0];
        assertEquals(customFilterDescriptor.getExpression(), customCopy.getExpression());
        assertEquals(customFilterDescriptor.getParams()[0], customCopy.getParams()[0]);
        assertEquals(customFilterDescriptor.getParams()[1], customCopy.getParams()[1]);
    }

    @Test
    public void testSerializeComplexFilter() throws Exception {
        FilterDescriptor nameFilter =
                new FilterDescriptor(Student.class, Student::getName, FilterOperator.START_WITH, "frank");
        FilterDescriptor ageFilter =
                new FilterDescriptor(Student.class, Student::getAge, FilterOperator.GREATER_THAN, 20);
        FilterDescriptor ageFilter2 =
                new FilterDescriptor(Student.class, Student::getAge, FilterOperator.LESS_THAN, 30);
        FilterGroupDescriptor ageGroupFilter = new FilterGroupDescriptor();
        ageGroupFilter.addFilters(ageFilter);
        ageGroupFilter.addFilters(ageFilter2);

        CustomFilterDescriptor noteFilter = new CustomFilterDescriptor();
        noteFilter.setExpression("note test expression");
        noteFilter.setParams(20, 30);

        FilterDescriptorBase[] filters = new FilterDescriptorBase[]{nameFilter, ageGroupFilter, noteFilter};
        byte[] arrayBytes = pickle(filters);
        FilterDescriptorBase[] filtersCopy = unpickle(arrayBytes, FilterDescriptorBase[].class);
        assertEquals(true, filtersCopy[0] instanceof FilterDescriptor);
        assertEquals(true, filters[1] instanceof FilterGroupDescriptor);
        assertEquals(true, filters[2] instanceof CustomFilterDescriptor);

        FilterDescriptor nameFilterCopy = (FilterDescriptor) filtersCopy[0];
        FilterGroupDescriptor ageGroupFilterCopy = (FilterGroupDescriptor) filtersCopy[1];
        CustomFilterDescriptor noteFilterCopy = (CustomFilterDescriptor) filtersCopy[2];

        assertEquals(nameFilter.getPropertyPath(), nameFilterCopy.getPropertyPath());
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