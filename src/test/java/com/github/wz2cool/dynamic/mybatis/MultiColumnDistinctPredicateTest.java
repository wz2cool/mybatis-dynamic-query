package com.github.wz2cool.dynamic.mybatis;

import com.github.wz2cool.dynamic.DynamicQuery;
import com.github.wz2cool.dynamic.FilterCondition;
import com.github.wz2cool.dynamic.FilterDescriptor;
import com.github.wz2cool.dynamic.FilterOperator;
import com.github.wz2cool.dynamic.exception.PropertyNotFoundInternalException;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.table.User;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicQueryProvider;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Pure JUnit tests for the multi-column distinct count judgement:
 * {@code DynamicQueryProvider.isMultiColumnDistinct},
 * {@code QueryHelper.countSelectColumns} / {@code QueryHelper.toCountColumnsExpression}
 * and the {@code multiColumnDistinct} flag in
 * {@code BaseDynamicQuery.toQueryParamMap}.
 *
 * @author Frank
 */
public class MultiColumnDistinctPredicateTest {

    private final QueryHelper queryHelper = new QueryHelper();

    // region DynamicQueryProvider.isMultiColumnDistinct

    @Test
    public void testIsMultiColumnDistinctReturnsFalseForNullColumn() {
        assertFalse(DynamicQueryProvider.isMultiColumnDistinct(true, null));
    }

    @Test
    public void testIsMultiColumnDistinctReturnsFalseForSingleColumn() {
        assertFalse(DynamicQueryProvider.isMultiColumnDistinct(true, "a"));
    }

    @Test
    public void testIsMultiColumnDistinctReturnsTrueForMultiColumns() {
        assertTrue(DynamicQueryProvider.isMultiColumnDistinct(true, "a, b"));
    }

    @Test
    public void testIsMultiColumnDistinctReturnsFalseWhenNotDistinct() {
        assertFalse(DynamicQueryProvider.isMultiColumnDistinct(false, "a, b"));
    }

    /** OGNL 传入 null distinct（queryParam("distinct", null) 覆盖场景）必须回退标量而非崩溃。 */
    @Test
    public void testIsMultiColumnDistinctReturnsFalseForNullDistinct() {
        assertFalse(DynamicQueryProvider.isMultiColumnDistinct(null, "a, b"));
    }

    // endregion

    // region QueryHelper.countSelectColumns

    @Test
    public void testCountSelectColumnsReturnsAllColumnsWithoutSelectAndIgnore() {
        DynamicQuery<User> query = DynamicQuery.createQuery(User.class);
        int count = queryHelper.countSelectColumns(
                User.class, query.getSelectedProperties(), query.getIgnoredProperties());
        assertEquals(3, count);
    }

    @Test
    public void testCountSelectColumnsWithSingleSelectedProperty() {
        DynamicQuery<User> query = DynamicQuery.createQuery(User.class);
        query.addSelectedProperties("id");
        int count = queryHelper.countSelectColumns(
                User.class, query.getSelectedProperties(), query.getIgnoredProperties());
        assertEquals(1, count);
    }

    @Test
    public void testCountSelectColumnsWithTwoSelectedProperties() {
        DynamicQuery<User> query = DynamicQuery.createQuery(User.class);
        query.addSelectedProperties("id", "userName");
        int count = queryHelper.countSelectColumns(
                User.class, query.getSelectedProperties(), query.getIgnoredProperties());
        assertEquals(2, count);
    }

    @Test
    public void testCountSelectColumnsWithIgnoredProperties() {
        DynamicQuery<User> query = DynamicQuery.createQuery(User.class);
        query.ignoreSelectedProperties("id", "userName");
        int count = queryHelper.countSelectColumns(
                User.class, query.getSelectedProperties(), query.getIgnoredProperties());
        assertEquals(1, count);
    }

    // endregion

    // region toQueryParamMap multiColumnDistinct flag

    @Test
    public void testToQueryParamMapMultiColumnDistinctTrueForAllColumnsDistinct() {
        DynamicQuery<User> query = DynamicQuery.createQuery(User.class);
        query.setDistinct(true);
        Map<String, Object> paramMap = query.toQueryParamMap();
        assertEquals(Boolean.TRUE, paramMap.get(MapperConstants.MULTI_COLUMN_DISTINCT));
    }

    @Test
    public void testToQueryParamMapMultiColumnDistinctFalseForSingleColumnDistinct() {
        DynamicQuery<User> query = DynamicQuery.createQuery(User.class);
        query.addSelectedProperties("id");
        query.setDistinct(true);
        Map<String, Object> paramMap = query.toQueryParamMap();
        assertEquals(Boolean.FALSE, paramMap.get(MapperConstants.MULTI_COLUMN_DISTINCT));
    }

    @Test
    public void testToQueryParamMapMultiColumnDistinctFalseWhenNotDistinct() {
        DynamicQuery<User> query = DynamicQuery.createQuery(User.class);
        Map<String, Object> paramMap = query.toQueryParamMap();
        assertEquals(Boolean.FALSE, paramMap.get(MapperConstants.MULTI_COLUMN_DISTINCT));
    }

    // endregion

    // region comma-bearing single expression must not be misjudged (spec scenario)

    /**
     * 列表达式自身含逗号的单列（如 {@code CONCAT(a, b)}）必须按真实列数（1 列）判定为单列，
     * 维持内联分支，不进入子查询包裹——否则该表达式的 NULL 语义会由排除静默变为计一行。
     */
    @Test
    public void testCountSelectColumnsSingleCommaExpressionNotMisjudged() {
        DynamicQuery<CommaExpressionEntity> query = DynamicQuery.createQuery(CommaExpressionEntity.class);
        query.addSelectedProperties("combined");
        int count = queryHelper.countSelectColumns(
                CommaExpressionEntity.class, query.getSelectedProperties(), query.getIgnoredProperties());
        assertEquals(1, count);
    }

    @Test
    public void testToQueryParamMapSingleCommaExpressionStaysInline() {
        DynamicQuery<CommaExpressionEntity> query = DynamicQuery.createQuery(CommaExpressionEntity.class);
        query.addSelectedProperties("combined");
        query.setDistinct(true);
        Map<String, Object> paramMap = query.toQueryParamMap();
        assertEquals(Boolean.FALSE, paramMap.get(MapperConstants.MULTI_COLUMN_DISTINCT));
    }

    // endregion

    // region count sub-query must use unique, valid aliases (derived-table requirement)

    /**
     * 派生表要求列名唯一。字段名经 camelCaseToUnderscore 转换后可能碰撞
     * （fooBar 与 foo_bar 都变成 foo_bar），而列表查询的 AS 别名正来自该转换；
     * 因此 count 子查询必须使用按列下标生成的唯一别名 mdq_col_&lt;i&gt;，而非字段名别名。
     */
    @Test
    public void testCountColumnsExpressionUsesUniqueIndexAliases() {
        DynamicQuery<CollisionExpressionEntity> query = DynamicQuery.createQuery(CollisionExpressionEntity.class);
        query.addSelectedProperties("fooBar", "foo_bar");
        assertEquals(2, queryHelper.countSelectColumns(
                CollisionExpressionEntity.class, query.getSelectedProperties(), query.getIgnoredProperties()));
        String expression = queryHelper.toCountColumnsExpression(
                CollisionExpressionEntity.class, query.getSelectedProperties(), query.getIgnoredProperties());
        assertEquals("foo_bar AS mdq_col_0, other_col AS mdq_col_1", expression);
    }

    // endregion

    // region judgement boundaries

    /** 恰好 2 列是多列判定的下边界（1 列 = 单列，2 列 = 多列）。 */
    @Test
    public void testToQueryParamMapTwoColumnsDistinctIsMulti() {
        DynamicQuery<User> query = DynamicQuery.createQuery(User.class);
        query.addSelectedProperties("id", "userName");
        query.setDistinct(true);
        Map<String, Object> paramMap = query.toQueryParamMap();
        assertEquals(Boolean.TRUE, paramMap.get(MapperConstants.MULTI_COLUMN_DISTINCT));
    }

    /** select 与 ignore 同时给出时 select 优先（既有选择逻辑的优先级边界）。 */
    @Test
    public void testCountSelectColumnsSelectTakesPrecedenceOverIgnore() {
        DynamicQuery<User> query = DynamicQuery.createQuery(User.class);
        query.addSelectedProperties("id", "userName");
        query.ignoreSelectedProperties("id");
        assertEquals(2, queryHelper.countSelectColumns(
                User.class, query.getSelectedProperties(), query.getIgnoredProperties()));
    }

    /** 重复 select 同一属性只计一次（contains 过滤天然去重）。 */
    @Test
    public void testToQueryParamMapDuplicateSelectedPropertiesCountedOnce() {
        DynamicQuery<User> query = DynamicQuery.createQuery(User.class);
        query.addSelectedProperties("id", "id");
        query.setDistinct(true);
        assertEquals(1, queryHelper.countSelectColumns(
                User.class, query.getSelectedProperties(), query.getIgnoredProperties()));
        Map<String, Object> paramMap = query.toQueryParamMap();
        assertEquals(Boolean.FALSE, paramMap.get(MapperConstants.MULTI_COLUMN_DISTINCT));
    }

    /** 0 列（ignore 全部）为 design Non-Goals 的病态输入：判定为非多列，不进子查询分支。 */
    @Test
    public void testToQueryParamMapIgnoreAllColumnsNotMulti() {
        DynamicQuery<User> query = DynamicQuery.createQuery(User.class);
        query.ignoreSelectedProperties("id", "userName", "password");
        query.setDistinct(true);
        Map<String, Object> paramMap = query.toQueryParamMap();
        assertEquals(Boolean.FALSE, paramMap.get(MapperConstants.MULTI_COLUMN_DISTINCT));
    }

    /** select 的属性全部不存在时同样计 0 列，不进子查询分支。 */
    @Test
    public void testToQueryParamMapSelectOnlyUnknownPropertiesNotMulti() {
        DynamicQuery<User> query = DynamicQuery.createQuery(User.class);
        query.addSelectedProperties("notExistProperty");
        query.setDistinct(true);
        Map<String, Object> paramMap = query.toQueryParamMap();
        assertEquals(Boolean.FALSE, paramMap.get(MapperConstants.MULTI_COLUMN_DISTINCT));
    }

    /** 0 列时 count 子查询列表达式为空串（渲染为内联空括号形态，见 CountSqlShapeTest 锁定）。 */
    @Test
    public void testToCountColumnsExpressionEmptySelectionReturnsEmptyString() {
        DynamicQuery<User> query = DynamicQuery.createQuery(User.class);
        query.ignoreSelectedProperties("id", "userName", "password");
        String expression = queryHelper.toCountColumnsExpression(
                User.class, query.getSelectedProperties(), query.getIgnoredProperties());
        assertEquals("", expression);
    }

    /** 语法解析判定下的边界：畸形/无法解析的输入回退标量（升级前行为），顶层逗号判多列。 */
    @Test
    public void testIsMultiColumnDistinctCommaBoundaryValues() {
        // 畸形输入解析失败 → 回退标量（升级前渲染），不抛异常
        assertFalse(DynamicQueryProvider.isMultiColumnDistinct(true, ","));
        assertFalse(DynamicQueryProvider.isMultiColumnDistinct(true, "a,"));
        assertTrue(DynamicQueryProvider.isMultiColumnDistinct(true, "a ,b"));
        assertFalse(DynamicQueryProvider.isMultiColumnDistinct(true, ""));
        assertFalse(DynamicQueryProvider.isMultiColumnDistinct(true, "   "));
    }

    /**
     * 顶层逗号解析的边界：括号内与引号字面量内的逗号不算列分隔符——
     * COALESCE(a, b)、CONCAT(a, ',') 这类单个表达式保持标量路径
     * （lambda 重载经映射列表达式传入时同样适用，NULL 语义与升级前一致）。
     */
    @Test
    public void testIsMultiColumnDistinctIgnoresCommasInsideParensAndLiterals() {
        assertFalse(DynamicQueryProvider.isMultiColumnDistinct(true, "CONCAT(a, b)"));
        assertFalse(DynamicQueryProvider.isMultiColumnDistinct(true, "COALESCE(a, b)"));
        assertFalse(DynamicQueryProvider.isMultiColumnDistinct(true, "f(g(a, b), c)"));
        assertFalse(DynamicQueryProvider.isMultiColumnDistinct(true, "CONCAT(a, ',')"));
        assertFalse(DynamicQueryProvider.isMultiColumnDistinct(true, "CONCAT(a, '')"));
        assertFalse(DynamicQueryProvider.isMultiColumnDistinct(true, "'it''s, x'"));
        assertFalse(DynamicQueryProvider.isMultiColumnDistinct(true, "CASE WHEN a THEN 'x,y' ELSE c END"));
        assertTrue(DynamicQueryProvider.isMultiColumnDistinct(true, "f(a), g(b)"));
        // 引名标识符（含逗号的名字）在语法树上是一个列——手写扫描器的两个盲区，语法解析正确处理
        assertFalse(DynamicQueryProvider.isMultiColumnDistinct(true, "`a, b`"));
        assertFalse(DynamicQueryProvider.isMultiColumnDistinct(true, "[a, b]"));
        assertTrue(DynamicQueryProvider.isMultiColumnDistinct(true, "CONCAT(a, b), price"));
    }

    // endregion

    // region exception paths

    /** entityClass 为 null 时直接快速失败（EntityCache 显式抛出 NullPointerException）。 */
    @Test(expected = NullPointerException.class)
    public void testCountSelectColumnsNullEntityClassThrows() {
        queryHelper.countSelectColumns(null, new String[0], new String[0]);
    }

    /** count 参数构建路径对未知属性过滤器抛 PropertyNotFoundInternalException（与列表查询同源校验）。 */
    @Test(expected = PropertyNotFoundInternalException.class)
    public void testToQueryParamMapUnknownPropertyFilterThrows() {
        DynamicQuery<User> query = DynamicQuery.createQuery(User.class);
        query.addFilters(new FilterDescriptor(FilterCondition.AND, "notExistProperty", FilterOperator.EQUAL, 1));
        query.setDistinct(true);
        query.toQueryParamMap();
    }

    // endregion

    /**
     * 逗号表达式列的测试实体：combined 列的查询表达式为 CONCAT(a, b)，真实列数仍为 1。
     * 仅用于 countSelectColumns 列计数（不注册 mapper、不执行 SQL）：
     * getter/setter 是属性识别所必需的，@Column 是唯一参与判定的注解。
     */
    public static class CommaExpressionEntity {
        private Integer id;
        @javax.persistence.Column(name = "CONCAT(a, b)")
        private String combined;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getCombined() {
            return combined;
        }

        public void setCombined(String combined) {
            this.combined = combined;
        }
    }

    /**
     * 别名碰撞的测试实体：fooBar 与 foo_bar 两个字段映射不同的物理列，
     * 但 camelCaseToUnderscore 后别名同为 foo_bar——列表查询可容忍重复输出列名，
     * 派生表不能。仅用于 count 子查询列子句生成，不注册 mapper、不执行 SQL。
     */
    public static class CollisionExpressionEntity {
        @javax.persistence.Column(name = "foo_bar")
        private Integer fooBar;
        @javax.persistence.Column(name = "other_col")
        private Integer foo_bar;

        public Integer getFooBar() {
            return fooBar;
        }

        public void setFooBar(Integer fooBar) {
            this.fooBar = fooBar;
        }

        public Integer getFoo_bar() {
            return foo_bar;
        }

        public void setFoo_bar(Integer foo_bar) {
            this.foo_bar = foo_bar;
        }
    }
}
