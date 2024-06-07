package com.github.wz2cool.dynamic.mybatis.mapper.provider;

import com.github.wz2cool.dynamic.DynamicQuery;
import com.github.wz2cool.dynamic.UpdateQuery;
import com.github.wz2cool.dynamic.mybatis.QueryHelper;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import com.github.wz2cool.dynamic.mybatis.mapper.helper.BaseEnhancedMapperTemplate;
import com.github.wz2cool.dynamic.mybatis.mapper.helper.DynamicQuerySqlHelper;
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
        sql.append(DynamicQuerySqlHelper.getLastClause());
        return sql.toString();
    }

    public String selectCountPropertyByDynamicQuery(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(DynamicQuerySqlHelper.getBindFilterParams(ms.getConfiguration().isMapUnderscoreToCamelCase()));
        sql.append(DynamicQuerySqlHelper.getFirstClause());
        String countColumns = String.format("<choose> <when test=\"%s.%s\">DISTINCT (%s) </when > <otherwise> %s </otherwise> </choose>",
                MapperConstants.DYNAMIC_QUERY_PARAMS, MapperConstants.DISTINCT,
                String.format("${%s} ", MapperConstants.COLUMN), String.format("${%s} ", MapperConstants.COLUMN));
        sql.append(String.format("SELECT %s COUNT(%s)", getHintClause(), countColumns));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(DynamicQuerySqlHelper.getWhereClause());
        return sql.toString();
    }

    public static String selectCount(Class<?> entityClass) {
        Set<EntityColumn> pkColumns = EntityHelper.getPKColumns(entityClass);
        String countKey = pkColumns.size() == 1 ? pkColumns.iterator().next().getColumn() : "*";
        String countColumns = String.format("<choose> <when test=\"%s.%s\">DISTINCT (%s) </when > <otherwise> %s </otherwise> </choose>",
                MapperConstants.DYNAMIC_QUERY_PARAMS, MapperConstants.DISTINCT,
                getSelectUnAsColumnsClause(), countKey);
        return String.format("SELECT %s COUNT(%s) ", getHintClause(), countColumns);
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