package com.github.wz2cool.dynamic.mybatis.mapper.provider;

import com.github.wz2cool.dynamic.FilterDescriptorBase;
import com.github.wz2cool.dynamic.SortDescriptor;
import com.github.wz2cool.dynamic.mybatis.MybatisQueryProvider;
import com.github.wz2cool.dynamic.mybatis.ParamExpression;
import com.github.wz2cool.dynamic.mybatis.QueryHelper;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperContant;
import com.github.wz2cool.dynamic.mybatis.mapper.helper.DynamicQuerySqlHelper;
import com.github.wz2cool.exception.PropertyNotFoundException;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.HashMap;
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
        String result = sql.toString();
        return result;
    }


    // add filterParams prefix
    public static Map<String, Object> getWhereQueryParamInternal(
            final Class entityClass,
            final FilterDescriptorBase[] filters) throws PropertyNotFoundException {
        ParamExpression paramExpression = queryHelper.toWhereExpression(entityClass, filters);
        String expression = paramExpression.getExpression();
        Map<String, Object> paramMap = paramExpression.getParamMap();
        for (Map.Entry<String, Object> param : paramMap.entrySet()) {
            String key = param.getKey();
            String newKey = String.format("%s.%s", MapperContant.FILTER_PARAMS, key);
            expression = expression.replace(key, newKey);
        }
        paramMap.put(MapperContant.WHERE_EXPRESSION, expression);
        return paramMap;
    }

    public static Map<String, Object> getSortQueryParamMap(
            final Class entityClass,
            final String sortExpressionPlaceholder,
            final SortDescriptor[] sorts) throws PropertyNotFoundException {
        return MybatisQueryProvider.getSortQueryParamMap(entityClass, sortExpressionPlaceholder, sorts);
    }

}