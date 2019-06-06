package com.github.wz2cool.dynamic.mybatis;

import com.github.wz2cool.model.HelloWorld;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Frank on 7/19/2017.
 */
public class ExpressionHelperTest {

    @Test
    public void testGetEqualExpression() {
        ExpressionHelper h2DbExpressionHelper = new ExpressionHelper();

        ColumnInfo queryColumnInfo = new ColumnInfo();
        queryColumnInfo.setColumnName("name");
        Object filterValue = null;
        String placeholder = "placeholder";

        String result = h2DbExpressionHelper.getEqualExpression(queryColumnInfo, filterValue, placeholder);
        assertEquals("name IS NULL", result);

        filterValue = "frank";
        result = h2DbExpressionHelper.getEqualExpression(queryColumnInfo, filterValue, placeholder);
        assertEquals("name = #{placeholder}", result);
    }

    @Test
    public void testGetNotEqualExpression() {
        ExpressionHelper h2DbExpressionHelper = new ExpressionHelper();

        ColumnInfo queryColumnInfo = new ColumnInfo();
        queryColumnInfo.setColumnName("name");
        Object filterValue = null;
        String placeholder = "placeholder";

        String result = h2DbExpressionHelper.getNotEqualExpression(queryColumnInfo, filterValue, placeholder);
        assertEquals("name IS NOT NULL", result);

        filterValue = "frank";
        result = h2DbExpressionHelper.getNotEqualExpression(queryColumnInfo, filterValue, placeholder);
        assertEquals("name <> #{placeholder}", result);
    }

    @Test
    public void testLessThanExpression() {
        ExpressionHelper h2DbExpressionHelper = new ExpressionHelper();

        ColumnInfo queryColumnInfo = new ColumnInfo();
        queryColumnInfo.setColumnName("name");
        String placeholder = "placeholder";

        String result = h2DbExpressionHelper.getLessThanExpression(queryColumnInfo, placeholder);
        assertEquals("name < #{placeholder}", result);
    }

    @Test
    public void testGetLessThanOrEqualExpression() {
        ExpressionHelper h2DbExpressionHelper = new ExpressionHelper();

        ColumnInfo queryColumnInfo = new ColumnInfo();
        queryColumnInfo.setColumnName("name");
        String placeholder = "placeholder";

        String result = h2DbExpressionHelper.getLessThanOrEqualExpression(queryColumnInfo, placeholder);
        assertEquals("name <= #{placeholder}", result);
    }


    @Test
    public void testGetGreaterThanOrEqualExpression() {
        ExpressionHelper h2DbExpressionHelper = new ExpressionHelper();

        ColumnInfo queryColumnInfo = new ColumnInfo();
        queryColumnInfo.setColumnName("name");
        String placeholder = "placeholder";

        String result = h2DbExpressionHelper.getGreaterThanOrEqualExpression(queryColumnInfo, placeholder);
        assertEquals("name >= #{placeholder}", result);
    }

    @Test
    public void testGetGreaterThanExpression() {
        ExpressionHelper h2DbExpressionHelper = new ExpressionHelper();

        ColumnInfo queryColumnInfo = new ColumnInfo();
        queryColumnInfo.setColumnName("name");
        String placeholder = "placeholder";

        String result = h2DbExpressionHelper.getGreaterThanExpression(queryColumnInfo, placeholder);
        assertEquals("name > #{placeholder}", result);
    }

    @Test
    public void testGetLikeExpression() {
        ExpressionHelper dbExpressionHelper = new ExpressionHelper();
        ColumnInfo queryColumnInfo =
                EntityCache.getInstance().getColumnInfo(HelloWorld.class, "stringProperty");
        String placeholder = "placeholder";

        String result = dbExpressionHelper.getLikeExpression(queryColumnInfo, placeholder);
        assertEquals("string_property LIKE #{placeholder}", result);
    }

    @Test
    public void testGetInExpressionTest() {
        ExpressionHelper dbExpressionHelper = new ExpressionHelper();
        ColumnInfo queryColumnInfo =
                EntityCache.getInstance().getColumnInfo(HelloWorld.class, "stringProperty");

        String result = dbExpressionHelper.getInExpression(queryColumnInfo);
        assertEquals("FALSE", result);

        result = dbExpressionHelper.getInExpression(queryColumnInfo, "p1", "p2");
        assertEquals("string_property IN (#{p1},#{p2})", result);
    }

    @Test
    public void testGetNotInExpressionTest() {
        ExpressionHelper dbExpressionHelper = new ExpressionHelper();
        ColumnInfo queryColumnInfo =
                EntityCache.getInstance().getColumnInfo(HelloWorld.class, "stringProperty");

        String result = dbExpressionHelper.getNotInExpression(queryColumnInfo);
        assertEquals("TRUE", result);

        result = dbExpressionHelper.getNotInExpression(queryColumnInfo, "p1", "p2");
        assertEquals("string_property NOT IN (#{p1},#{p2})", result);
    }

    @Test
    public void testGetBetweenExpression() {
        ExpressionHelper dbExpressionHelper = new ExpressionHelper();
        ColumnInfo queryColumnInfo =
                EntityCache.getInstance().getColumnInfo(HelloWorld.class, "stringProperty");

        String result = dbExpressionHelper.getBetweenExpression(queryColumnInfo, "p1", "p2");
        assertEquals("string_property BETWEEN #{p1} AND #{p2}", result);
    }
}

