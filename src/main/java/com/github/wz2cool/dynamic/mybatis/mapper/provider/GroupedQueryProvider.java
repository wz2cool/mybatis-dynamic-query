package com.github.wz2cool.dynamic.mybatis.mapper.provider;

import com.github.wz2cool.dynamic.GroupedQuery;
import com.github.wz2cool.dynamic.mybatis.QueryHelper;
import com.github.wz2cool.dynamic.mybatis.mapper.helper.BaseEnhancedMapperTemplate;
import com.github.wz2cool.dynamic.mybatis.mapper.helper.GroupedQuerySqlHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Map;

/**
 * @author Frank
 * @date 2020/11/28
 **/
public class GroupedQueryProvider extends BaseEnhancedMapperTemplate {
    private static final QueryHelper QUERY_HELPER = new QueryHelper();

    public GroupedQueryProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
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

    public String selectByGroupedQuery(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder();
        sql.append(GroupedQuerySqlHelper.getBindFilterParams(ms.getConfiguration().isMapUnderscoreToCamelCase()));
        sql.append("SELECT");
        //支持查询指定列
        sql.append(GroupedQuerySqlHelper.getSelectColumnsClause());
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(GroupedQuerySqlHelper.getWhereClause());
        sql.append(GroupedQuerySqlHelper.getGroupByClause());
        sql.append(GroupedQuerySqlHelper.getHavingClause());
        sql.append(GroupedQuerySqlHelper.getSortClause());
        return sql.toString();
    }

    /// region for xml query

    public static Map<String, Object> getGroupedQueryParamInternal(
            final GroupedQuery groupedQuery,
            final boolean isMapUnderscoreToCamelCase) {
        return groupedQuery.toQueryParamMap(isMapUnderscoreToCamelCase);
    }
    // endregion
}
