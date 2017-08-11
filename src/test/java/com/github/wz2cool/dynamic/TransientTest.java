package com.github.wz2cool.dynamic;

import com.github.wz2cool.dynamic.mybatis.MybatisQueryProvider;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.table.User;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Frank
 * \* Date: 8/11/2017
 * \* Time: 5:38 PM
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class TransientTest {
    @Test
    public void testGetColumns() {
        String result = MybatisQueryProvider.getAllColumnsExpression(User.class);
        assertEquals(false, result.contains("useless"));
    }
}