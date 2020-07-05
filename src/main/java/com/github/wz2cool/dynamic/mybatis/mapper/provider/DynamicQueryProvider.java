package com.github.wz2cool.dynamic.mybatis.mapper.provider;

import com.github.wz2cool.dynamic.BaseFilterDescriptor;
import com.github.wz2cool.dynamic.DynamicQuery;
import com.github.wz2cool.dynamic.BaseSortDescriptor;
import com.github.wz2cool.dynamic.mybatis.ParamExpression;
import com.github.wz2cool.dynamic.mybatis.QueryHelper;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import com.github.wz2cool.dynamic.mybatis.mapper.helper.DynamicQuerySqlHelper;
import com.github.wz2cool.dynamic.mybatis.mapper.helper.BaseEnhancedMapperTemplate;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Map;

/**
 * @author Frank
 */
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
        sql.append(SqlHelper.selectCount(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(DynamicQuerySqlHelper.getWhereClause());
        return sql.toString();
    }


    public String selectMaxByDynamicQuery(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(DynamicQuerySqlHelper.getBindFilterParams(ms.getConfiguration().isMapUnderscoreToCamelCase()));
        sql.append(DynamicQuerySqlHelper.getSelectMax());
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(DynamicQuerySqlHelper.getWhereClause());
        return sql.toString();
    }

    public String selectMinByDynamicQuery(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(DynamicQuerySqlHelper.getBindFilterParams(ms.getConfiguration().isMapUnderscoreToCamelCase()));
        sql.append(DynamicQuerySqlHelper.getSelectMin());
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(DynamicQuerySqlHelper.getWhereClause());
        return sql.toString();
    }

    public String deleteByDynamicQuery(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(DynamicQuerySqlHelper.getBindFilterParams(ms.getConfiguration().isMapUnderscoreToCamelCase()));
        sql.append(SqlHelper.deleteFromTable(entityClass, tableName(entityClass)));
        sql.append(DynamicQuerySqlHelper.getWhereClause());
        return sql.toString();
    }

    public String selectByDynamicQuery(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder();
        sql.append(DynamicQuerySqlHelper.getBindFilterParams(ms.getConfiguration().isMapUnderscoreToCamelCase()));
        sql.append("SELECT");
        sql.append(String.format("<if test=\"%s.%s\">distinct</if>",
                MapperConstants.DYNAMIC_QUERY_PARAMS, MapperConstants.DISTINCT));
        //支持查询指定列
        sql.append(DynamicQuerySqlHelper.getSelectColumnsClause());
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(DynamicQuerySqlHelper.getWhereClause());
        sql.append(DynamicQuerySqlHelper.getSortClause());
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

    private String updateByDynamicQuery(MappedStatement ms, boolean noNull) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(DynamicQuerySqlHelper.getBindFilterParams(ms.getConfiguration().isMapUnderscoreToCamelCase()));
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass), "example"));
        sql.append(SqlHelper.updateSetColumns(entityClass, "record", noNull, isNotEmpty()));
        sql.append(DynamicQuerySqlHelper.getWhereClause());
        return sql.toString();
    }


    /// region for xml query

    public static Map<String, Object> getDynamicQueryParamInternal(
            final DynamicQuery dynamicQuery,
            final boolean isMapUnderscoreToCamelCase) {
        Class<?> entityClass = dynamicQuery.getEntityClass();
        BaseFilterDescriptor[] filters = dynamicQuery.getFilters();
        BaseSortDescriptor[] sorts = dynamicQuery.getSorts();
        String[] selectedProperties = dynamicQuery.getSelectedProperties();
        String[] ignoredProperties = dynamicQuery.getIgnoredProperties();

        ParamExpression whereParamExpression = QUERY_HELPER.toWhereExpression(entityClass, filters);
        String whereExpression = whereParamExpression.getExpression();
        Map<String, Object> paramMap = whereParamExpression.getParamMap();
        for (Map.Entry<String, Object> param : paramMap.entrySet()) {
            String key = param.getKey();
            String newKey = String.format("%s.%s", MapperConstants.DYNAMIC_QUERY_PARAMS, key);
            whereExpression = whereExpression.replace(key, newKey);
        }
        paramMap.put(MapperConstants.WHERE_EXPRESSION, whereExpression);

        ParamExpression sortExpression = QUERY_HELPER.toSortExpression(entityClass, sorts);
        paramMap.put(MapperConstants.SORT_EXPRESSION, sortExpression.getExpression());
        paramMap.put(MapperConstants.DISTINCT, dynamicQuery.isDistinct());

        String selectColumnExpression = QUERY_HELPER.toSelectColumnsExpression(
                entityClass, selectedProperties, ignoredProperties, isMapUnderscoreToCamelCase);
        paramMap.put(MapperConstants.SELECT_COLUMNS_EXPRESSION, selectColumnExpression);
        return paramMap;
    }
    // endregion
}