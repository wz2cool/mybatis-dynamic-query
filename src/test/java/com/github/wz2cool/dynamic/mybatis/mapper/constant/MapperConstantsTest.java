package com.github.wz2cool.dynamic.mybatis.mapper.constant;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class MapperConstantsTest {
    @Test(expected = InvocationTargetException.class)
    public void TestMapperConstants() throws Exception {
        Constructor<MapperConstants> c = MapperConstants.class.getDeclaredConstructor();
        c.setAccessible(true);
        c.newInstance();
    }
}