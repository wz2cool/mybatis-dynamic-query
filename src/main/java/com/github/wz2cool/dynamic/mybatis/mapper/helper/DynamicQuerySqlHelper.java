package com.github.wz2cool.dynamic.mybatis.mapper.helper;

import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import tk.mybatis.mapper.entity.IDynamicTableName;
import tk.mybatis.mapper.util.StringUtil;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Frank
 */
@SuppressWarnings("Duplicates")
public class DynamicQuerySqlHelper {

    private static final String FIRST_SQL = "${dynamicQueryParams.mdq_first_sql} ";
    private static final String LAST_SQL = " ${dynamicQueryParams.mdq_last_sql}";
    private static final String HINT_SQL = " ${dynamicQueryParams.mdq_hint_sql} ";
    /** Derived-table alias closing the multi-column distinct count sub-query. */
    private static final String COUNT_DERIVED_TABLE_ALIAS = "mdq_count";

    private DynamicQuerySqlHelper() {
        throw new UnsupportedOperationException();
    }

    public static String getBindFilterParams(boolean isMapUnderscoreToCamelCase) {
        StringBuilder sql = new StringBuilder();
        sql.append("<bind name=\"");
        sql.append(MapperConstants.DYNAMIC_QUERY_PARAMS).append("\" ");
        sql.append("value=\"");
        sql.append("@com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicQueryProvider");
        sql.append("@getDynamicQueryParamInternal(");
        sql.append(MapperConstants.DYNAMIC_QUERY).append(", ").append(isMapUnderscoreToCamelCase).append(")");
        sql.append("\"/>");
        return sql.toString();
    }

    public static String getUpdateBindFilterParams(boolean isMapUnderscoreToCamelCase) {
        StringBuilder sql = new StringBuilder();
        sql.append("<bind name=\"");
        sql.append(MapperConstants.DYNAMIC_QUERY_PARAMS).append("\" ");
        sql.append("value=\"");
        sql.append("@com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicQueryProvider");
        sql.append("@getUpdateQueryParamInternal(");
        sql.append(MapperConstants.DYNAMIC_QUERY).append(", ").append(isMapUnderscoreToCamelCase).append(")");
        sql.append("\"/>");
        return sql.toString();
    }

    public static String getSelectColumnsClause() {
        return String.format(" ${%s.%s} ", MapperConstants.DYNAMIC_QUERY_PARAMS, MapperConstants.SELECT_COLUMNS_EXPRESSION);
    }

    public static String getSelectUnAsColumnsClause() {
        return String.format(" ${%s.%s} ", MapperConstants.DYNAMIC_QUERY_PARAMS, MapperConstants.UN_AS_SELECT_COLUMNS_EXPRESSION);
    }

    public static String getWhereClause() {
        String newExpression = String.format("%s.%s", MapperConstants.DYNAMIC_QUERY_PARAMS, MapperConstants.WHERE_EXPRESSION);
        return String.format("<if test=\"%s != null and %s != ''\">WHERE ${%s}</if>",
                newExpression, newExpression, newExpression);
    }

    public static String getSortClause() {
        String newExpression = String.format("%s.%s", MapperConstants.DYNAMIC_QUERY_PARAMS, MapperConstants.SORT_EXPRESSION);
        return String.format("<if test=\"%s != null and %s != ''\">ORDER BY ${%s}</if>",
                newExpression, newExpression, newExpression);
    }

    public static String getSetClause() {
        String newExpression = String.format("%s.%s", MapperConstants.DYNAMIC_QUERY_PARAMS, MapperConstants.SET_EXPRESSION);
        return String.format("<if test=\"%s != null and %s != ''\">SET ${%s}</if>",
                newExpression, newExpression, newExpression);
    }

    public static String getSelectMax() {
        return String.format("SELECT %s MAX(${%s})", getHintClause(), MapperConstants.COLUMN);
    }

    public static String getSelectMin() {
        return String.format("SELECT %s MIN(${%s})", getHintClause(), MapperConstants.COLUMN);
    }

    public static String getSelectSum() {
        return String.format("SELECT %s SUM(${%s})", getHintClause(), MapperConstants.COLUMN);
    }

    public static String getSelectAvg() {
        return String.format("SELECT %s AVG(${%s})", getHintClause(), MapperConstants.COLUMN);
    }

    /**
     * Builds the count column clause: a flat three-branch choose where multi-column distinct
     * renders a derived-table wrap ({@code COUNT(*) FROM ( SELECT DISTINCT ...}; the derived-table
     * alias is provided by the closing fragment {@link #getCountDistinctCloseClause(String)}),
     * single-column distinct keeps the inline {@code COUNT(DISTINCT ( x ))}, and non-distinct
     * renders a plain {@code COUNT(...)}.
     * <p>The three fragment parameters are directly embeddable template text (they carry their own
     * {@code ${}} placeholders and legacy space padding), not bare OGNL names. The multi-column
     * when must come before the distinct when (the multi-column condition implies distinct).
     * <p>TODO(Frank): reserve a MyBatis databaseId dialect extension point — applications that
     * configure databaseId could switch back to native inline rendering per dialect
     * (mysql without parentheses / h2, postgresql with parentheses). Not enabled by default.
     *
     * @param multiColumnTest OGNL test expression of the multi-column branch
     * @param multiCols       column fragment after {@code SELECT DISTINCT} in the multi-column branch
     * @param singleInner     fragment between {@code COUNT(} and {@code )} of the single-column branch
     * @param otherwiseInner  fragment between {@code COUNT(} and {@code )} of the non-distinct branch
     * @return the embeddable three-branch choose text
     */
    public static String getCountColumnsClause(String multiColumnTest, String multiCols, String singleInner, String otherwiseInner) {
        return "<choose>"
                + "<when test=\"" + multiColumnTest + "\">COUNT(*) FROM ( SELECT DISTINCT" + multiCols + "</when>"
                + "<when test=\"" + MapperConstants.DYNAMIC_QUERY_PARAMS + "." + MapperConstants.DISTINCT + "\">COUNT(" + singleInner + ")</when>"
                + "<otherwise>COUNT(" + otherwiseInner + ")</otherwise>"
                + "</choose>";
    }

    /**
     * Closing-parenthesis fragment of the multi-column distinct derived table, placed between
     * WHERE and {@code ${mdq_last_sql}}. Its test condition must stay identical to the
     * multi-column when test of {@link #getCountColumnsClause(String, String, String, String)}.
     *
     * @param multiColumnTest OGNL test expression of the multi-column branch
     * @return a fragment shaped like {@code <if test="...">) mdq_count</if>}
     */
    public static String getCountDistinctCloseClause(String multiColumnTest) {
        return "<if test=\"" + multiColumnTest + "\">) " + COUNT_DERIVED_TABLE_ALIAS + "</if>";
    }

    /**
     * insert ignore into tableName - 动态表名
     *
     * @param entityClass
     * @param defaultTableName
     * @return
     */
    public static String insertIgnoreIntoTable(Class<?> entityClass, String defaultTableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT IGNORE INTO ");
        sql.append(getDynamicTableName(entityClass, defaultTableName));
        sql.append(" ");
        return sql.toString();
    }

    /**
     * 获取表名 - 支持动态表名
     *
     * @param entityClass
     * @param tableName
     * @return
     */
    public static String getDynamicTableName(Class<?> entityClass, String tableName) {
        if (IDynamicTableName.class.isAssignableFrom(entityClass)) {
            StringBuilder sql = new StringBuilder();
            sql.append("<choose>");
            sql.append("<when test=\"@tk.mybatis.mapper.util.OGNL@isDynamicParameter(_parameter) and dynamicTableName != null and dynamicTableName != ''\">");
            sql.append("${dynamicTableName}\n");
            sql.append("</when>");
            //不支持指定列的时候查询全部列
            sql.append("<otherwise>");
            sql.append(tableName);
            sql.append("</otherwise>");
            sql.append("</choose>");
            return sql.toString();
        } else {
            return tableName;
        }
    }

    public static String getDynamicTableName(Class<?> entityClass, String tableName, String parameterName) {
        if (IDynamicTableName.class.isAssignableFrom(entityClass)) {
            if (StringUtil.isNotEmpty(parameterName)) {
                StringBuilder sql = new StringBuilder();
                sql.append("<choose>");
                sql.append("<when test=\"@tk.mybatis.mapper.util.OGNL@isDynamicParameter(" + parameterName + ") and " + parameterName + ".dynamicTableName != null and " + parameterName + ".dynamicTableName != ''\">");
                sql.append("${" + parameterName + ".dynamicTableName}");
                sql.append("</when>");
                sql.append("<otherwise>");
                sql.append(tableName);
                sql.append("</otherwise>");
                sql.append("</choose>");
                return sql.toString();
            } else {
                return getDynamicTableName(entityClass, tableName);
            }
        } else {
            return tableName;
        }
    }

    public static String updateTable(Class<?> entityClass, String defaultTableName, String entityName) {
        StringBuilder sql = new StringBuilder();
        sql.append(String.format("UPDATE %s ", getHintClause()));
        sql.append(getDynamicTableName(entityClass, defaultTableName, entityName));
        sql.append(" ");
        return sql.toString();
    }

    public static Optional<String> insertIgnoreIntoPostgresqlTable(Class<?> entityClass) {
        Set<String> uniqueKeys = new HashSet<>();
        // 获取@Table 注解中的唯一约束
        Table tableAnnotation = entityClass.getAnnotation(Table.class);
        if (Objects.nonNull(tableAnnotation)) {
            UniqueConstraint[] constraints = tableAnnotation.uniqueConstraints();
            for (UniqueConstraint constraint : constraints) {
                uniqueKeys.addAll(Arrays.asList(constraint.columnNames()));
            }
        }
        if (uniqueKeys.isEmpty()) {
            //  默认主键
            for (Field field : entityClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    uniqueKeys.add(camelToUnderscore(field.getName()));
                }
            }
        }
        if (uniqueKeys.isEmpty()) {
            // 主键和唯一键都没有，正常插入
            return Optional.empty();
        }
        StringBuilder sql = new StringBuilder();
        sql.append("ON CONFLICT (");
        sql.append(String.join(",", uniqueKeys));
        sql.append(") DO NOTHING");
        return Optional.of(sql.toString());
    }

    /**
     * 字段驼峰转下划线
     *
     * @param param 地段
     * @return 结果
     */
    public static String camelToUnderscore(String param) {
        if (param == null || param.isEmpty()) {
            return param;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < param.length(); i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                result.append("_").append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    public static String deleteFromTable(Class<?> entityClass, String defaultTableName) {
        StringBuilder sql = new StringBuilder();
        sql.append(String.format("DELETE %s FROM ", getHintClause()));
        sql.append(getDynamicTableName(entityClass, defaultTableName));
        sql.append(" ");
        return sql.toString();
    }

    public static String getLastClause() {
        return LAST_SQL;
    }

    public static String getFirstClause() {
        return FIRST_SQL;
    }

    public static String getHintClause() {
        return HINT_SQL;
    }
}