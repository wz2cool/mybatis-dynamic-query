package com.github.wz2cool.dynamic.mybatis.mapper.provider;

import com.github.wz2cool.dynamic.DynamicQuery;
import com.github.wz2cool.dynamic.UpdateQuery;
import com.github.wz2cool.dynamic.mybatis.QueryHelper;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import com.github.wz2cool.dynamic.mybatis.mapper.helper.BaseEnhancedMapperTemplate;
import com.github.wz2cool.dynamic.mybatis.mapper.helper.DynamicQuerySqlHelper;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Map;
import java.util.Set;

import static com.github.wz2cool.dynamic.mybatis.mapper.helper.DynamicQuerySqlHelper.getHintClause;
import static com.github.wz2cool.dynamic.mybatis.mapper.helper.DynamicQuerySqlHelper.getSelectUnAsColumnsClause;

/**
 * @author Frank
 */
@SuppressWarnings("Duplicates")
public class DynamicQueryProvider extends BaseEnhancedMapperTemplate {
    private static final QueryHelper QUERY_HELPER = new QueryHelper();

    /**
     * Pseudo table name used to wrap a caller-supplied column string into a parseable
     * statement; the identifier is never executed against a real database.
     */
    private static final String COLUMN_PARSING_PSEUDO_TABLE = "__mdq_columns__";

    public DynamicQueryProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    @Override
    protected String tableName(Class<?> entityClass) {
        String viewExpression = QUERY_HELPER.getViewExpression(entityClass);
        if (StringUtils.isNoneBlank(viewExpression)) {
            return viewExpression;
        } else {
            return super.tableName(entityClass);
        }
    }

    public String selectCountByDynamicQuery(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(DynamicQuerySqlHelper.getBindFilterParams(ms.getConfiguration().isMapUnderscoreToCamelCase()));
        sql.append(DynamicQuerySqlHelper.getFirstClause());
        sql.append(selectCount(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(DynamicQuerySqlHelper.getWhereClause());
        sql.append(DynamicQuerySqlHelper.getCountDistinctCloseClause(getMultiColumnDistinctTest()));
        sql.append(DynamicQuerySqlHelper.getLastClause());
        return sql.toString();
    }

    public String selectCountPropertyByDynamicQuery(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(DynamicQuerySqlHelper.getBindFilterParams(ms.getConfiguration().isMapUnderscoreToCamelCase()));
        sql.append(getMultiColumnDistinctBind());
        sql.append(DynamicQuerySqlHelper.getFirstClause());
        String countColumns = DynamicQuerySqlHelper.getCountColumnsClause(
                MapperConstants.MULTI_COLUMN_DISTINCT,
                String.format(" ${%s} ", MapperConstants.COLUMN),
                String.format(" DISTINCT (${%s} )  ", MapperConstants.COLUMN),
                String.format("  ${%s}   ", MapperConstants.COLUMN));
        sql.append(String.format("SELECT %s%s", getHintClause(), countColumns));
        // MyBatis joins each root-level node's output with StringJoiner(" "); keeping fromTable's
        // leading space would render one extra space for the untouched single/not-distinct branches,
        // so it is stripped here to keep those branches byte-identical to the pre-change rendering.
        sql.append(StringUtils.stripStart(SqlHelper.fromTable(entityClass, tableName(entityClass)), null));
        sql.append(DynamicQuerySqlHelper.getWhereClause());
        sql.append(DynamicQuerySqlHelper.getCountDistinctCloseClause(MapperConstants.MULTI_COLUMN_DISTINCT));
        return sql.toString();
    }

    /**
     * Column clause for the query-path count statement. The multi-column distinct branch opens
     * with "COUNT(*) FROM ( SELECT DISTINCT ..."; its derived-table closing parenthesis
     * ") mdq_count" is appended by {@link #selectCountByDynamicQuery(MappedStatement)} right
     * after WHERE via {@link DynamicQuerySqlHelper#getCountDistinctCloseClause(String)}.
     * The two pieces must always be paired: any other template embedding this method without
     * appending the closing clause would render unbalanced SQL for distinct + multi-column.
     *
     * @param entityClass the entity class whose primary key (or {@code *}) backs the
     *                    non-distinct count branch
     * @return the embeddable count column-clause template (without FROM/WHERE, which
     *         {@code selectCountByDynamicQuery} appends)
     */
    public static String selectCount(Class<?> entityClass) {
        Set<EntityColumn> pkColumns = EntityHelper.getPKColumns(entityClass);
        String countKey = pkColumns.size() == 1 ? pkColumns.iterator().next().getColumn() : "*";
        String countColumns = DynamicQuerySqlHelper.getCountColumnsClause(
                getMultiColumnDistinctTest(),
                String.format(" ${%s.%s} ",
                        MapperConstants.DYNAMIC_QUERY_PARAMS, MapperConstants.COUNT_SELECT_COLUMNS_EXPRESSION),
                " DISTINCT (" + getSelectUnAsColumnsClause() + ")  ",
                "  " + countKey + "  ");
        return String.format("SELECT %s%s", getHintClause(), countColumns);
    }

    private static String getMultiColumnDistinctTest() {
        return MapperConstants.DYNAMIC_QUERY_PARAMS + "." + MapperConstants.MULTI_COLUMN_DISTINCT;
    }

    private static String getMultiColumnDistinctBind() {
        return String.format("<bind name=\"%s\" value=\"@%s@isMultiColumnDistinct(%s.%s, %s)\"/>",
                MapperConstants.MULTI_COLUMN_DISTINCT,
                DynamicQueryProvider.class.getName(),
                MapperConstants.DYNAMIC_QUERY_PARAMS, MapperConstants.DISTINCT,
                MapperConstants.COLUMN);
    }

    /**
     * Multi-column distinct judgement for the property path
     * ({@code selectCountPropertyByDynamicQuery}): the column is a caller-supplied bare string,
     * so the column count is determined by parsing it with the embedded (shaded) JSqlParser —
     * {@code COALESCE(a, b)}, {@code CONCAT(a, ',')} or a CASE expression all resolve to ONE
     * select item and stay on the inline scalar path with pre-change NULL semantics, while
     * {@code a, b} resolves to TWO and takes the derived-table wrap. This also covers the
     * lambda overload, whose column comes from a mapped {@code @Column} expression that may
     * itself contain commas invisible at the call site. The distinct conjunction is mandatory:
     * without it a non-distinct multi-column column string would silently gain DISTINCT
     * semantics.
     * <p>Any parse failure falls back to the scalar path, which matches the pre-change
     * rendering — the fail-safe direction. This includes dialect syntax, malformed input and
     * {@code StackOverflowError} from pathologically deep nesting (the recursive-descent
     * parser overflows the stack around a few thousand parenthesis levels; the pre-change
     * scanner never crashed on such input, so the fallback preserves that robustness). The
     * parser is shaded into this library's own package, so downstream classpaths are
     * unaffected by it.
     *
     * @param distinct whether the query enables distinct; any non-TRUE value (including
     *                 {@code null}, which OGNL passes through when the map entry was
     *                 overridden via {@code queryParam("distinct", null)}) is treated as
     *                 not distinct, matching the pre-change {@code <when test>} behavior
     *                 where a null silently evaluated to false
     * @param column   the caller-supplied column string
     * @return whether to render the derived-table wrapping for multi-column distinct
     */
    public static boolean isMultiColumnDistinct(Boolean distinct, String column) {
        if (!Boolean.TRUE.equals(distinct) || column == null) {
            return false;
        }
        try {
            Statement stmt = CCJSqlParserUtil.parse("SELECT " + column + " FROM " + COLUMN_PARSING_PSEUDO_TABLE);
            PlainSelect plain = (PlainSelect) ((Select) stmt).getSelectBody();
            return plain.getSelectItems().size() > 1;
        } catch (JSQLParserException | RuntimeException | StackOverflowError parseFailure) {
            // 解析失败（方言语法/畸形输入/嵌套过深撑爆解析器栈）一律回退标量路径
            // = 升级前渲染，安全方向；此处无状态无资源持有，捕获该 Error 是安全的
            return false;
        }
    }

    public String selectMaxByDynamicQuery(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(DynamicQuerySqlHelper.getBindFilterParams(ms.getConfiguration().isMapUnderscoreToCamelCase()));
        sql.append(DynamicQuerySqlHelper.getFirstClause());
        sql.append(DynamicQuerySqlHelper.getSelectMax());
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(DynamicQuerySqlHelper.getWhereClause());
        sql.append(DynamicQuerySqlHelper.getLastClause());
        return sql.toString();
    }

    public String selectMinByDynamicQuery(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(DynamicQuerySqlHelper.getBindFilterParams(ms.getConfiguration().isMapUnderscoreToCamelCase()));
        sql.append(DynamicQuerySqlHelper.getFirstClause());
        sql.append(DynamicQuerySqlHelper.getSelectMin());
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(DynamicQuerySqlHelper.getWhereClause());
        sql.append(DynamicQuerySqlHelper.getLastClause());
        return sql.toString();
    }

    public String selectSumByDynamicQuery(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(DynamicQuerySqlHelper.getBindFilterParams(ms.getConfiguration().isMapUnderscoreToCamelCase()));
        sql.append(DynamicQuerySqlHelper.getFirstClause());
        sql.append(DynamicQuerySqlHelper.getSelectSum());
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(DynamicQuerySqlHelper.getWhereClause());
        sql.append(DynamicQuerySqlHelper.getLastClause());
        return sql.toString();
    }

    public String selectAvgByDynamicQuery(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(DynamicQuerySqlHelper.getBindFilterParams(ms.getConfiguration().isMapUnderscoreToCamelCase()));
        sql.append(DynamicQuerySqlHelper.getFirstClause());
        sql.append(DynamicQuerySqlHelper.getSelectAvg());
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(DynamicQuerySqlHelper.getWhereClause());
        sql.append(DynamicQuerySqlHelper.getLastClause());
        return sql.toString();
    }

    public String deleteByDynamicQuery(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(DynamicQuerySqlHelper.getBindFilterParams(ms.getConfiguration().isMapUnderscoreToCamelCase()));
        sql.append(DynamicQuerySqlHelper.getFirstClause());
        sql.append(DynamicQuerySqlHelper.deleteFromTable(entityClass, tableName(entityClass)));
        sql.append(DynamicQuerySqlHelper.getWhereClause());
        sql.append(DynamicQuerySqlHelper.getLastClause());
        return sql.toString();
    }

    public String selectByDynamicQuery(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder();
        sql.append(DynamicQuerySqlHelper.getBindFilterParams(ms.getConfiguration().isMapUnderscoreToCamelCase()));
        sql.append(DynamicQuerySqlHelper.getFirstClause());
        sql.append(String.format("SELECT %s ", getHintClause()));
        sql.append(String.format("<if test=\"%s.%s\">distinct</if>",
                MapperConstants.DYNAMIC_QUERY_PARAMS, MapperConstants.DISTINCT));
        //支持查询指定列
        sql.append(DynamicQuerySqlHelper.getSelectColumnsClause());
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(DynamicQuerySqlHelper.getWhereClause());
        sql.append(DynamicQuerySqlHelper.getSortClause());
        sql.append(DynamicQuerySqlHelper.getLastClause());
        return sql.toString();
    }

    public String selectRowBoundsByDynamicQuery(MappedStatement ms) {
        return selectByDynamicQuery(ms);
    }

    public String updateSelectiveByDynamicQuery(MappedStatement ms) {
        return updateByDynamicQuery(ms, true);
    }

    public String updateByDynamicQuery(MappedStatement ms) {
        return updateByDynamicQuery(ms, false);
    }

    public String updateByUpdateQuery(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(DynamicQuerySqlHelper.getUpdateBindFilterParams(ms.getConfiguration().isMapUnderscoreToCamelCase()));
        sql.append(DynamicQuerySqlHelper.getFirstClause());
        sql.append(DynamicQuerySqlHelper.updateTable(entityClass, tableName(entityClass), "example"));
        sql.append(DynamicQuerySqlHelper.getSetClause());
        sql.append(DynamicQuerySqlHelper.getWhereClause());
        sql.append(DynamicQuerySqlHelper.getLastClause());
        return sql.toString();
    }

    private String updateByDynamicQuery(MappedStatement ms, boolean noNull) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(DynamicQuerySqlHelper.getBindFilterParams(ms.getConfiguration().isMapUnderscoreToCamelCase()));
        sql.append(DynamicQuerySqlHelper.getFirstClause());
        sql.append(DynamicQuerySqlHelper.updateTable(entityClass, tableName(entityClass), "example"));
        sql.append(SqlHelper.updateSetColumns(entityClass, "record", noNull, isNotEmpty()));
        sql.append(DynamicQuerySqlHelper.getWhereClause());
        sql.append(DynamicQuerySqlHelper.getLastClause());
        return sql.toString();
    }

    /// region for xml query

    public static Map<String, Object> getDynamicQueryParamInternal(
            final DynamicQuery dynamicQuery,
            final boolean isMapUnderscoreToCamelCase) {
        return dynamicQuery.toQueryParamMap(isMapUnderscoreToCamelCase);
    }

    public static Map<String, Object> getUpdateQueryParamInternal(
            final UpdateQuery updateQuery,
            final boolean isMapUnderscoreToCamelCase) {
        return updateQuery.toQueryParamMap(isMapUnderscoreToCamelCase);
    }
    // endregion
}