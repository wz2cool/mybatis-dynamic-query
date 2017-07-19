package com.frwan.query.dynamic.mybatis;

import com.frwan.query.model.HelloWorld;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Frank on 7/19/2017.
 */
public class DbExpressionHelperTest {

    @Test
    public void testGetEqualExpression() {
        DbExpressionHelper h2DbExpressionHelper = new DbExpressionHelper(DatabaseType.H2);

        QueryColumnInfo queryColumnInfo = new QueryColumnInfo();
        queryColumnInfo.setQueryColumn("name");
        Object filterValue = null;
        String placeholder = "placeholder";

        String result = h2DbExpressionHelper.getEqualExpression(queryColumnInfo, filterValue, placeholder);
        assertEquals("name IS NULL", result);

        filterValue = "frank";
        result = h2DbExpressionHelper.getEqualExpression(queryColumnInfo, filterValue, placeholder);
        assertEquals("name = #{placeholder}", result);
    }

    @Test
    public void testGetEqualExpressionForPostresql() {
        DbExpressionHelper dbExpressionHelper = new DbExpressionHelper(DatabaseType.POSTRESQL);

        // string field.
        QueryColumnInfo queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "stringProperty");
        Object filterValue = null;
        String placeholder = "placeholder";

        String result = dbExpressionHelper.getEqualExpression(queryColumnInfo, filterValue, placeholder);
        assertEquals("string_property IS NULL", result);

        filterValue = "frank";
        result = dbExpressionHelper.getEqualExpression(queryColumnInfo, filterValue, placeholder);
        assertEquals("string_property = #{placeholder}", result);


        queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "integerProperty");

        filterValue = "123";
        result = dbExpressionHelper.getEqualExpression(queryColumnInfo, filterValue, placeholder);
        assertEquals("integer_property = #{placeholder}::NUMERIC", result);


        queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "dateProperty");

        filterValue = "2017-7-19";
        result = dbExpressionHelper.getEqualExpression(queryColumnInfo, filterValue, placeholder);
        assertEquals("date_property::TEXT = #{placeholder}", result);
    }

    @Test
    public void testGetNotEqualExpression() {
        DbExpressionHelper h2DbExpressionHelper = new DbExpressionHelper(DatabaseType.H2);

        QueryColumnInfo queryColumnInfo = new QueryColumnInfo();
        queryColumnInfo.setQueryColumn("name");
        Object filterValue = null;
        String placeholder = "placeholder";

        String result = h2DbExpressionHelper.getNotEqualExpression(queryColumnInfo, filterValue, placeholder);
        assertEquals("name IS NOT NULL", result);

        filterValue = "frank";
        result = h2DbExpressionHelper.getNotEqualExpression(queryColumnInfo, filterValue, placeholder);
        assertEquals("name <> #{placeholder}", result);
    }

    @Test
    public void testGetNotEqualExpressionForPostresql() {
        DbExpressionHelper dbExpressionHelper = new DbExpressionHelper(DatabaseType.POSTRESQL);

        // string field.
        QueryColumnInfo queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "stringProperty");
        Object filterValue = null;
        String placeholder = "placeholder";

        String result = dbExpressionHelper.getNotEqualExpression(queryColumnInfo, filterValue, placeholder);
        assertEquals("string_property IS NOT NULL", result);

        filterValue = "frank";
        result = dbExpressionHelper.getNotEqualExpression(queryColumnInfo, filterValue, placeholder);
        assertEquals("string_property <> #{placeholder}", result);


        queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "integerProperty");

        filterValue = "123";
        result = dbExpressionHelper.getNotEqualExpression(queryColumnInfo, filterValue, placeholder);
        assertEquals("integer_property <> #{placeholder}::NUMERIC", result);


        queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "dateProperty");

        filterValue = "2017-7-19";
        result = dbExpressionHelper.getNotEqualExpression(queryColumnInfo, filterValue, placeholder);
        assertEquals("date_property::TEXT <> #{placeholder}", result);
    }


    @Test
    public void testLessThanExpression() {
        DbExpressionHelper h2DbExpressionHelper = new DbExpressionHelper(DatabaseType.H2);

        QueryColumnInfo queryColumnInfo = new QueryColumnInfo();
        queryColumnInfo.setQueryColumn("name");
        String placeholder = "placeholder";

        String result = h2DbExpressionHelper.getLessThanExpression(queryColumnInfo, placeholder);
        assertEquals("name < #{placeholder}", result);
    }

    @Test
    public void testLessThanExpressionForPostresql() {
        DbExpressionHelper dbExpressionHelper = new DbExpressionHelper(DatabaseType.POSTRESQL);

        // string field.
        QueryColumnInfo queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "stringProperty");
        String placeholder = "placeholder";

        String result = dbExpressionHelper.getLessThanExpression(queryColumnInfo, placeholder);
        assertEquals("string_property < #{placeholder}", result);

        queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "integerProperty");

        result = dbExpressionHelper.getLessThanExpression(queryColumnInfo, placeholder);
        assertEquals("integer_property < #{placeholder}::NUMERIC", result);


        queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "dateProperty");

        result = dbExpressionHelper.getLessThanExpression(queryColumnInfo, placeholder);
        assertEquals("date_property::TEXT < #{placeholder}", result);
    }

    @Test
    public void testGetLessThanOrEqualExpression() {
        DbExpressionHelper h2DbExpressionHelper = new DbExpressionHelper(DatabaseType.H2);

        QueryColumnInfo queryColumnInfo = new QueryColumnInfo();
        queryColumnInfo.setQueryColumn("name");
        String placeholder = "placeholder";

        String result = h2DbExpressionHelper.getLessThanOrEqualExpression(queryColumnInfo, placeholder);
        assertEquals("name <= #{placeholder}", result);
    }

    @Test
    public void testLessThanOrEqualExpressionForPostresql() {
        DbExpressionHelper dbExpressionHelper = new DbExpressionHelper(DatabaseType.POSTRESQL);

        // string field.
        QueryColumnInfo queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "stringProperty");
        String placeholder = "placeholder";

        String result = dbExpressionHelper.getLessThanOrEqualExpression(queryColumnInfo, placeholder);
        assertEquals("string_property <= #{placeholder}", result);

        queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "integerProperty");

        result = dbExpressionHelper.getLessThanOrEqualExpression(queryColumnInfo, placeholder);
        assertEquals("integer_property <= #{placeholder}::NUMERIC", result);

        queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "dateProperty");

        result = dbExpressionHelper.getLessThanOrEqualExpression(queryColumnInfo, placeholder);
        assertEquals("date_property::TEXT <= #{placeholder}", result);
    }

    @Test
    public void testGetGreaterThanOrEqualExpression() {
        DbExpressionHelper h2DbExpressionHelper = new DbExpressionHelper(DatabaseType.H2);

        QueryColumnInfo queryColumnInfo = new QueryColumnInfo();
        queryColumnInfo.setQueryColumn("name");
        String placeholder = "placeholder";

        String result = h2DbExpressionHelper.getGreaterThanOrEqualExpression(queryColumnInfo, placeholder);
        assertEquals("name >= #{placeholder}", result);
    }

    @Test
    public void testGreaterThanOrEqualExpressionForPostresql() {
        DbExpressionHelper dbExpressionHelper = new DbExpressionHelper(DatabaseType.POSTRESQL);

        // string field.
        QueryColumnInfo queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "stringProperty");
        String placeholder = "placeholder";

        String result = dbExpressionHelper.getGreaterThanOrEqualExpression(queryColumnInfo, placeholder);
        assertEquals("string_property >= #{placeholder}", result);

        queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "integerProperty");

        result = dbExpressionHelper.getGreaterThanOrEqualExpression(queryColumnInfo, placeholder);
        assertEquals("integer_property >= #{placeholder}::NUMERIC", result);

        queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "dateProperty");

        result = dbExpressionHelper.getGreaterThanOrEqualExpression(queryColumnInfo, placeholder);
        assertEquals("date_property::TEXT >= #{placeholder}", result);
    }


    @Test
    public void testGetGreaterThanExpression() {
        DbExpressionHelper h2DbExpressionHelper = new DbExpressionHelper(DatabaseType.H2);

        QueryColumnInfo queryColumnInfo = new QueryColumnInfo();
        queryColumnInfo.setQueryColumn("name");
        String placeholder = "placeholder";

        String result = h2DbExpressionHelper.getGreaterThanExpression(queryColumnInfo, placeholder);
        assertEquals("name > #{placeholder}", result);
    }

    @Test
    public void testGreaterThanExpressionForPostresql() {
        DbExpressionHelper dbExpressionHelper = new DbExpressionHelper(DatabaseType.POSTRESQL);

        // string field.
        QueryColumnInfo queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "stringProperty");
        String placeholder = "placeholder";

        String result = dbExpressionHelper.getGreaterThanExpression(queryColumnInfo, placeholder);
        assertEquals("string_property > #{placeholder}", result);

        queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "integerProperty");

        result = dbExpressionHelper.getGreaterThanExpression(queryColumnInfo, placeholder);
        assertEquals("integer_property > #{placeholder}::NUMERIC", result);

        queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "dateProperty");

        result = dbExpressionHelper.getGreaterThanExpression(queryColumnInfo, placeholder);
        assertEquals("date_property::TEXT > #{placeholder}", result);
    }

    @Test
    public void testGetLikeExpression() {
        DbExpressionHelper dbExpressionHelper = new DbExpressionHelper(DatabaseType.POSTRESQL);
        QueryColumnInfo queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "stringProperty");
        String placeholder = "placeholder";

        String result = dbExpressionHelper.getLikeExpression(queryColumnInfo, placeholder);
        assertEquals("string_property LIKE #{placeholder}", result);
    }

    @Test
    public void testGetLikeExpressionForPostresql() {
        DbExpressionHelper dbExpressionHelper = new DbExpressionHelper(DatabaseType.POSTRESQL);

        // string field.
        QueryColumnInfo queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "stringProperty");
        String placeholder = "placeholder";

        String result = dbExpressionHelper.getLikeExpression(queryColumnInfo, placeholder);
        assertEquals("string_property LIKE #{placeholder}", result);

        queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "integerProperty");

        result = dbExpressionHelper.getLikeExpression(queryColumnInfo, placeholder);
        assertEquals("integer_property::TEXT LIKE #{placeholder}", result);

        queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "dateProperty");

        result = dbExpressionHelper.getLikeExpression(queryColumnInfo, placeholder);
        assertEquals("date_property::TEXT LIKE #{placeholder}", result);
    }

    @Test
    public void testGetInExpressionTest() {
        DbExpressionHelper dbExpressionHelper = new DbExpressionHelper(DatabaseType.POSTRESQL);
        QueryColumnInfo queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "stringProperty");

        String result = dbExpressionHelper.getInExpression(queryColumnInfo);
        assertEquals("", result);

        result = dbExpressionHelper.getInExpression(queryColumnInfo, "p1", "p2");
        assertEquals("string_property IN (#{p1},#{p2})", result);
    }

    @Test
    public void testGetInExpressionTestForPostresql() {
        DbExpressionHelper dbExpressionHelper = new DbExpressionHelper(DatabaseType.POSTRESQL);

        // string field.
        QueryColumnInfo queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "stringProperty");


        String result = dbExpressionHelper.getInExpression(queryColumnInfo, "p1", "p2");
        assertEquals("string_property IN (#{p1},#{p2})", result);

        queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "integerProperty");

        result = dbExpressionHelper.getInExpression(queryColumnInfo, "p1", "p2");
        assertEquals("integer_property IN (#{p1}::NUMERIC,#{p2}::NUMERIC)", result);

        queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "dateProperty");

        result = dbExpressionHelper.getInExpression(queryColumnInfo, "p1", "p2");
        assertEquals("date_property::TEXT IN (#{p1},#{p2})", result);
    }

    @Test
    public void testGetNotInExpressionTest() {
        DbExpressionHelper dbExpressionHelper = new DbExpressionHelper(DatabaseType.H2);
        QueryColumnInfo queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "stringProperty");

        String result = dbExpressionHelper.getNotInExpression(queryColumnInfo);
        assertEquals("", result);

        result = dbExpressionHelper.getNotInExpression(queryColumnInfo, "p1", "p2");
        assertEquals("string_property NOT IN (#{p1},#{p2})", result);
    }

    @Test
    public void testGetNotInExpressionTestForPostresql() {
        DbExpressionHelper dbExpressionHelper = new DbExpressionHelper(DatabaseType.POSTRESQL);

        // string field.
        QueryColumnInfo queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "stringProperty");


        String result = dbExpressionHelper.getNotInExpression(queryColumnInfo, "p1", "p2");
        assertEquals("string_property NOT IN (#{p1},#{p2})", result);

        queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "integerProperty");

        result = dbExpressionHelper.getNotInExpression(queryColumnInfo, "p1", "p2");
        assertEquals("integer_property NOT IN (#{p1}::NUMERIC,#{p2}::NUMERIC)", result);

        queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "dateProperty");

        result = dbExpressionHelper.getNotInExpression(queryColumnInfo, "p1", "p2");
        assertEquals("date_property::TEXT NOT IN (#{p1},#{p2})", result);
    }

    @Test
    public void testGetBitAndGreaterZero() {
        DbExpressionHelper dbExpressionHelper = new DbExpressionHelper(DatabaseType.MYSQL);
        QueryColumnInfo queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "stringProperty");

        String result = dbExpressionHelper.getBitAndGreaterZero(queryColumnInfo, "p1");
        assertEquals("string_property & #{p1} > 0", result);
    }

    @Test
    public void testGetBitAndGreaterZeroForH2() {
        DbExpressionHelper dbExpressionHelper = new DbExpressionHelper(DatabaseType.H2);
        QueryColumnInfo queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "stringProperty");

        String result = dbExpressionHelper.getBitAndGreaterZero(queryColumnInfo, "p1");
        assertEquals("BITAND(string_property, #{p1}) > 0", result);
    }

    @Test
    public void testGetBitAndGreaterZeroForPostresql() {
        DbExpressionHelper dbExpressionHelper = new DbExpressionHelper(DatabaseType.POSTRESQL);

        // string field.
        QueryColumnInfo queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "stringProperty");


        String result = dbExpressionHelper.getBitAndGreaterZero(queryColumnInfo, "p1");
        assertEquals("string_property::BIGINT & #{p1}::BIGINT > 0", result);

        queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "integerProperty");

        result = dbExpressionHelper.getBitAndGreaterZero(queryColumnInfo, "p1");
        assertEquals("integer_property & #{p1}::BIGINT > 0", result);

        queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "dateProperty");

        result = dbExpressionHelper.getBitAndGreaterZero(queryColumnInfo, "p1");
        assertEquals("date_property::BIGINT & #{p1}::BIGINT > 0", result);
    }

    @Test
    public void testGetBitAndEqualZero() {
        DbExpressionHelper dbExpressionHelper = new DbExpressionHelper(DatabaseType.MYSQL);
        QueryColumnInfo queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "stringProperty");

        String result = dbExpressionHelper.getBitAndEqualZero(queryColumnInfo, "p1");
        assertEquals("string_property & #{p1} = 0", result);
    }

    @Test
    public void testGetBitAndEqualZeroForH2() {
        DbExpressionHelper dbExpressionHelper = new DbExpressionHelper(DatabaseType.H2);
        QueryColumnInfo queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "stringProperty");

        String result = dbExpressionHelper.getBitAndEqualZero(queryColumnInfo, "p1");
        assertEquals("BITAND(string_property, #{p1}) = 0", result);
    }

    @Test
    public void testGetBitAndEqualZeroForPostresql() {
        DbExpressionHelper dbExpressionHelper = new DbExpressionHelper(DatabaseType.POSTRESQL);

        // string field.
        QueryColumnInfo queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "stringProperty");


        String result = dbExpressionHelper.getBitAndEqualZero(queryColumnInfo, "p1");
        assertEquals("string_property::BIGINT & #{p1}::BIGINT = 0", result);

        queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "integerProperty");

        result = dbExpressionHelper.getBitAndEqualZero(queryColumnInfo, "p1");
        assertEquals("integer_property & #{p1}::BIGINT = 0", result);

        queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "dateProperty");

        result = dbExpressionHelper.getBitAndEqualZero(queryColumnInfo, "p1");
        assertEquals("date_property::BIGINT & #{p1}::BIGINT = 0", result);
    }


    @Test
    public void testGetBitAndEqualInput() {
        DbExpressionHelper dbExpressionHelper = new DbExpressionHelper(DatabaseType.MYSQL);
        QueryColumnInfo queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "stringProperty");

        String result = dbExpressionHelper.getBitAndEqualInput(queryColumnInfo, "p1");
        assertEquals("string_property & #{p1} = #{p1}", result);
    }

    @Test
    public void testGetBitAndEqualInputForH2() {
        DbExpressionHelper dbExpressionHelper = new DbExpressionHelper(DatabaseType.H2);
        QueryColumnInfo queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "stringProperty");

        String result = dbExpressionHelper.getBitAndEqualInput(queryColumnInfo, "p1");
        assertEquals("BITAND(string_property, #{p1}) = #{p1}", result);
    }

    @Test
    public void testGetBitAndEqualInputForPostresql() {
        DbExpressionHelper dbExpressionHelper = new DbExpressionHelper(DatabaseType.POSTRESQL);

        // string field.
        QueryColumnInfo queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "stringProperty");


        String result = dbExpressionHelper.getBitAndEqualInput(queryColumnInfo, "p1");
        assertEquals("string_property::BIGINT & #{p1}::BIGINT = #{p1}::BIGINT", result);

        queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "integerProperty");

        result = dbExpressionHelper.getBitAndEqualInput(queryColumnInfo, "p1");
        assertEquals("integer_property & #{p1}::BIGINT = #{p1}::BIGINT", result);

        queryColumnInfo =
                EntityCache.getInstance().getQueryColumnInfo(HelloWorld.class, "dateProperty");

        result = dbExpressionHelper.getBitAndEqualInput(queryColumnInfo, "p1");
        assertEquals("date_property::BIGINT & #{p1}::BIGINT = #{p1}::BIGINT", result);
    }
}

