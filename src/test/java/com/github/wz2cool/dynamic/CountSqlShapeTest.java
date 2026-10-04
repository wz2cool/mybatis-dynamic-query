package com.github.wz2cool.dynamic;

import com.github.wz2cool.dynamic.mybatis.db.mapper.UserDao;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.table.User;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * BoundSql SQL-shape assertions for the count statements (multi-column distinct count fix).
 * <p>
 * 期望的 SQL 字符串（含连续空格）为对该代码库状态实测逐字节校验的结果，
 * 断言使用普通 String 相等比较，空格数量不可改动。
 * <p>
 * 注意：渲染参数必须是含 {@code dynamicQuery}（property 版另含 {@code column}）键的 Map，
 * 不能直接传 {@link DynamicQuery#toQueryParamMap()} 的结果。
 * <p>
 * 对应 openspec change: fix-multi-column-distinct-count (task 3.3)。
 *
 * @author Frank
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TestApplication.class)
public class CountSqlShapeTest {

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    private String render(String statementId, Map<String, Object> params) {
        Configuration configuration = sqlSessionFactory.getConfiguration();
        MappedStatement ms = configuration.getMappedStatement(statementId);
        BoundSql boundSql = ms.getBoundSql(params);
        return boundSql.getSql();
    }

    private Map<String, Object> buildParams(DynamicQuery<User> query, boolean withColumn, String column) {
        Map<String, Object> params = new HashMap<>(4);
        if (withColumn) {
            params.put("column", column);
        }
        params.put("dynamicQuery", query);
        return params;
    }

    @Test
    public void multiColumnCount_subQueryShape() {
        String dao = UserDao.class.getName();
        DynamicQuery<User> query = DynamicQuery.createQuery(User.class);
        query.setDistinct(true);

        String sql = render(dao + ".selectCountByDynamicQuery", buildParams(query, false, null));

        assertTrue(sql.contains("COUNT(*) FROM ( SELECT DISTINCT"));
        assertTrue(sql.contains(") mdq_count"));
        // 子查询列带 AS 别名（派生表列名）
        assertTrue(sql.contains(" AS "));
        // count 子查询使用按下标生成的唯一别名 mdq_col_&lt;i&gt;（字段名别名可能碰撞，见
        // QueryHelper.toCountColumnsExpression）
        assertTrue(sql.contains("mdq_col_0"));
    }

    @Test
    public void lastClause_staysOutsideDerivedTable() {
        String dao = UserDao.class.getName();
        DynamicQuery<User> query = DynamicQuery.createQuery(User.class);
        query.setDistinct(true);
        query.last("LIMIT 3");

        String sql = render(dao + ".selectCountByDynamicQuery", buildParams(query, false, null));

        // 派生表闭括号在 last 之前，last 子句留在派生表外
        assertTrue(sql.endsWith("LIMIT 3"));
        assertTrue(sql.contains(") mdq_count"));
        assertTrue(sql.indexOf(") mdq_count") < sql.indexOf("LIMIT 3"));
    }

    @Test
    public void singleColumnCount_inlineByteIdentical() {
        String dao = UserDao.class.getName();
        DynamicQuery<User> query = DynamicQuery.createQuery(User.class);
        query.addSelectedProperties("id");
        query.setDistinct(true);

        String sql = render(dao + ".selectCountByDynamicQuery", buildParams(query, false, null));

        assertEquals("SELECT    COUNT( DISTINCT ( id )  )  FROM users", sql);
    }

    @Test
    public void notDistinctCount_byteIdentical() {
        String dao = UserDao.class.getName();
        DynamicQuery<User> query = DynamicQuery.createQuery(User.class);
        query.addSelectedProperties("id", "userName");

        String sql = render(dao + ".selectCountByDynamicQuery", buildParams(query, false, null));

        assertEquals("SELECT    COUNT(  id  )  FROM users", sql);
    }

    @Test
    public void propertyMultiColumnCount_subQueryShape() {
        String dao = UserDao.class.getName();
        DynamicQuery<User> query = DynamicQuery.createQuery(User.class);
        query.setDistinct(true);

        String sql = render(dao + ".selectCountPropertyByDynamicQuery",
                buildParams(query, true, "user_name, password"));

        assertEquals(
                "SELECT    COUNT(*) FROM ( SELECT DISTINCT user_name, password  FROM users  ) mdq_count",
                sql);
    }

    @Test
    public void propertySingleColumnCount_byteIdentical() {
        String dao = UserDao.class.getName();
        DynamicQuery<User> query = DynamicQuery.createQuery(User.class);
        query.setDistinct(true);

        String sql = render(dao + ".selectCountPropertyByDynamicQuery",
                buildParams(query, true, "user_name"));

        assertEquals("SELECT    COUNT( DISTINCT (user_name )  ) FROM users", sql);
    }

    @Test
    public void propertyNotDistinct_noDistinctSemantics() {
        String dao = UserDao.class.getName();
        DynamicQuery<User> query = DynamicQuery.createQuery(User.class);

        String sql = render(dao + ".selectCountPropertyByDynamicQuery",
                buildParams(query, true, "user_name, password"));

        // 未 distinct 的多列列串走 otherwise 分支，不得被静默施加 DISTINCT 语义
        assertEquals("SELECT    COUNT(  user_name, password   ) FROM users", sql);
        assertFalse(sql.contains("SELECT DISTINCT"));
    }

    @Test
    public void propertyNullDistinctParam_fallsBackToNonDistinctRendering() {
        // queryParam("distinct", null) 会把 paramMap 里的 distinct 覆盖为 null：
        // bind 判定必须空安全回退标量（升级前 <when test> 对 null 静默为 false 的等价行为），
        // 而不是 OGNL 拆箱失败导致整个语句渲染抛错
        String dao = UserDao.class.getName();
        DynamicQuery<User> query = DynamicQuery.createQuery(User.class);
        query.queryParam("distinct", null);

        String sql = render(dao + ".selectCountPropertyByDynamicQuery",
                buildParams(query, true, "user_name, password"));

        assertEquals("SELECT    COUNT(  user_name, password   ) FROM users", sql);
        assertFalse(sql.contains("SELECT DISTINCT"));
    }

    @Test
    public void guardFlagCannotBeOverridden() {
        String dao = UserDao.class.getName();
        DynamicQuery<User> query = DynamicQuery.createQuery(User.class);
        query.setDistinct(true);
        // 框架内部标志，用户自定义 queryParam 不能覆盖多列判定
        query.queryParam("multiColumnDistinct", false);

        String sql = render(dao + ".selectCountByDynamicQuery", buildParams(query, false, null));

        assertTrue(sql.contains("COUNT(*) FROM ( SELECT DISTINCT"));
        assertTrue(sql.contains(") mdq_count"));
    }

    @Test
    public void twoColumnsBoundary_rendersSubQuery() {
        // 恰好 2 列是多列判定的下边界（1 列 = 单列内联），2 列必须走子查询包裹
        String dao = UserDao.class.getName();
        DynamicQuery<User> query = DynamicQuery.createQuery(User.class);
        query.addSelectedProperties("id", "userName");
        query.setDistinct(true);

        String sql = render(dao + ".selectCountByDynamicQuery", buildParams(query, false, null));

        assertTrue(sql.contains("COUNT(*) FROM ( SELECT DISTINCT"));
        assertTrue(sql.contains(") mdq_count"));
    }

    @Test
    public void zeroColumnsBoundary_rendersLegacyInlineForm() {
        // 0 列（ignore 全部属性）是 design Non-Goals 明确不修的病态输入：
        // 判定为非多列、维持升级前的内联渲染（空括号形态），本用例锁定该既有行为
        String dao = UserDao.class.getName();
        DynamicQuery<User> query = DynamicQuery.createQuery(User.class);
        query.ignoreSelectedProperties("id", "userName", "password");
        query.setDistinct(true);

        String sql = render(dao + ".selectCountByDynamicQuery", buildParams(query, false, null));

        assertTrue(sql.contains("COUNT( DISTINCT (  )  )"));
        assertFalse(sql.contains("mdq_count"));
    }

    @Test
    public void missingDynamicQueryParam_throws() {
        // 调用方误用：渲染参数缺少 dynamicQuery 键时 bind OGNL 求值失败，
        // MyBatis 包装为 BuilderException（selectCountPropertyByDynamicQuery 同理）
        String dao = UserDao.class.getName();
        try {
            render(dao + ".selectCountByDynamicQuery", new HashMap<String, Object>(4));
            fail("expected BuilderException when the 'dynamicQuery' param is missing");
        } catch (BuilderException expected) {
            // expected
        }
    }
}
