package com.github.wz2cool.dynamic.mybatis.mapper.helper;

import com.github.wz2cool.dynamic.mybatis.MybatisQueryProvider;
import com.github.wz2cool.dynamic.mybatis.mapper.helper.DynamicQuerySqlHelper;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Frank
 * \* Date: 8/11/2017
 * \* Time: 3:00 PM
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class DynamicQuerySqlHelperTest {
    @Test(expected = InvocationTargetException.class)
    public void TestDynamicQuerySqlHelper() throws Exception {
        Constructor<DynamicQuerySqlHelper> c = DynamicQuerySqlHelper.class.getDeclaredConstructor();
        c.setAccessible(true);
        c.newInstance();
    }
}