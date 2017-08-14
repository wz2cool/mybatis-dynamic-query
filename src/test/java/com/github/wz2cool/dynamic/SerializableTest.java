package com.github.wz2cool.dynamic;

import com.github.wz2cool.model.Student;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;

public class SerializableTest {

    @Test
    public void testSerializeFilterDescriptor() throws Exception {
        FilterDescriptor filterDescriptor =
                new FilterDescriptor(Student.class, Student::getAge, FilterOperator.EQUAL, 1);
        byte[] bytes = pickle(filterDescriptor);
        FilterDescriptor copy = unpickle(bytes, FilterDescriptor.class);
        assertEquals(filterDescriptor.getPropertyPath(), copy.getPropertyPath());
        assertEquals(filterDescriptor.getValue(), copy.getValue());
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