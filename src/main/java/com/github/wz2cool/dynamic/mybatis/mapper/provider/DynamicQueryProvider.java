package com.github.wz2cool.dynamic.mybatis.mapper.provider;

import com.github.wz2cool.dynamic.DynamicQuery;
import com.github.wz2cool.dynamic.FilterDescriptorBase;
import com.github.wz2cool.dynamic.SortDescriptorBase;
import com.github.wz2cool.dynamic.mybatis.ParamExpression;
import com.github.wz2cool.dynamic.mybatis.QueryHelper;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import com.github.wz2cool.dynamic.mybatis.mapper.helper.DynamicQuerySqlHelper;
import com.github.wz2cool.exception.PropertyNotFoundException;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Map;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Frank
 * \* Date: 8/8/2017
 * \* Time: 3:13 PM
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class DynamicQueryProvider extends MapperTemplate {
    private final static QueryHelper queryHelper = new QueryHelper();

    public DynamicQueryProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String selectCountByDynamicQuery(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(DynamicQuerySqlHelper.getBindFilterParams());
        sql.append(SqlHelper.selectCount(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(DynamicQuerySqlHelper.getWhereClause());
        return sql.toString();
    }

    public String deleteByDynamicQuery(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(DynamicQuerySqlHelper.getBindFilterParams());
        sql.append(SqlHelper.deleteFromTable(entityClass, tableName(entityClass)));
        sql.append(DynamicQuerySqlHelper.getWhereClause());
        return sql.toString();
    }

    public String selectByDynamicQuery(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder();
        sql.append(DynamicQuerySqlHelper.getBindFilterParams());
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
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(DynamicQuerySqlHelper.getBindFilterParams());
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass), "example"));
        sql.append(SqlHelper.updateSetColumns(entityClass, "record", true, isNotEmpty()));
        sql.append(DynamicQuerySqlHelper.getWhereClause());
        return sql.toString();
    }

    public String updateByDynamicQuery(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(DynamicQuerySqlHelper.getBindFilterParams());
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass), "example"));
        sql.append(SqlHelper.updateSetColumns(entityClass, "record", false, isNotEmpty()));
        sql.append(DynamicQuerySqlHelper.getWhereClause());
        return sql.toString();
    }

    /// region for xml query.
    // add filterParams prefix
    public static Map<String, Object> getDynamicQueryParamInternal(final DynamicQuery dynamicQuery)
            throws PropertyNotFoundException {
        Class<?> entityClass = dynamicQuery.getEntityClass();
        FilterDescriptorBase[] filters = dynamicQuery.getFilters();
        SortDescriptorBase[] sorts = dynamicQuery.getSorts();
        String[] selectFields = dynamicQuery.getSelectProperties();

        ParamExpression whereParamExpression = queryHelper.toWhereExpression(entityClass, filters);
        String whereExpression = whereParamExpression.getExpression();
        Map<String, Object> paramMap = whereParamExpression.getParamMap();
        for (Map.Entry<String, Object> param : paramMap.entrySet()) {
            String key = param.getKey();
            String newKey = String.format("%s.%s", MapperConstants.DYNAMIC_QUERY_PARAMS, key);
            whereExpression = whereExpression.replace(key, newKey);
        }
        paramMap.put(MapperConstants.WHERE_EXPRESSION, whereExpression);

        ParamExpression sortExpression = queryHelper.toSortExpression(entityClass, sorts);
        paramMap.put(MapperConstants.SORT_EXPRESSION, sortExpression.getExpression());
        paramMap.put(MapperConstants.DISTINCT, dynamicQuery.isDistinct());

        String selectColumnExpression = queryHelper.toSelectColumnsExpression(entityClass, selectFields);
        paramMap.put(MapperConstants.SELECT_COLUMNS_EXPRESSION, selectColumnExpression);
        return paramMap;
    }
    // endregion
}