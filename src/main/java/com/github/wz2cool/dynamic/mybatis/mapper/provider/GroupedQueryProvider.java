package com.github.wz2cool.dynamic.mybatis.mapper.provider;

import com.github.wz2cool.dynamic.mybatis.QueryHelper;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import com.github.wz2cool.dynamic.mybatis.mapper.helper.DynamicQuerySqlHelper;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.factory.ProviderFactory;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.factory.ProviderTable;
import org.apache.ibatis.builder.annotation.ProviderContext;


/**
 * @author Frank
 **/
public class GroupedQueryProvider {
    private static final QueryHelper QUERY_HELPER = new QueryHelper();

    @Deprecated
    public String dynamicSQL(ProviderContext providerContext) {
        ProviderTable providerTable = ProviderFactory.create(providerContext);
        Class<?> entityClass = providerTable.getEntityClass();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT");
        sql.append(String.format("<if test=\"%s.%s\">distinct</if>",
                MapperConstants.DYNAMIC_QUERY_PARAMS, MapperConstants.DISTINCT));
        //支持查询指定列
        sql.append(DynamicQuerySqlHelper.getSelectColumnsClause());
        sql.append("from " + providerTable.getTableName() + " ");
        sql.append(DynamicQuerySqlHelper.getWhereClause(entityClass));
        sql.append(DynamicQuerySqlHelper.getSortClause());
        return sql.toString();
    }

//
//    public String selectByGroupedQuery(MappedStatement ms) {
//        Class<?> selectClass = getSelectClass(ms);
//        Class<?> entityClass = getEntityClass(ms);
//        setResultType(ms, selectClass);
//        StringBuilder sql = new StringBuilder();
//        sql.append(GroupedQuerySqlHelper.getBindFilterParams(ms.getConfiguration().isMapUnderscoreToCamelCase()));
//        sql.append("SELECT");
//        //支持查询指定列
//        sql.append(GroupedQuerySqlHelper.getSelectColumnsClause());
//        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
//        sql.append(GroupedQuerySqlHelper.getWhereClause());
//        sql.append(GroupedQuerySqlHelper.getGroupByClause());
//        sql.append(GroupedQuerySqlHelper.getHavingClause());
//        sql.append(GroupedQuerySqlHelper.getSortClause());
//        return sql.toString();
//    }
//
//    public String selectRowBoundsByGroupedQuery(MappedStatement ms) {
//        return selectByGroupedQuery(ms);
//    }
//
//    public String selectMaxByGroupedQuery(MappedStatement ms) {
//        Class<?> entityClass = getEntityClass(ms);
//        StringBuilder sql = new StringBuilder();
//        sql.append(GroupedQuerySqlHelper.getBindFilterParams(ms.getConfiguration().isMapUnderscoreToCamelCase()));
//        //支持查询指定列
//        sql.append(GroupedQuerySqlHelper.getSelectMax());
//        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
//        sql.append(GroupedQuerySqlHelper.getWhereClause());
//        sql.append(GroupedQuerySqlHelper.getGroupByClause());
//        sql.append(GroupedQuerySqlHelper.getHavingClause());
//        sql.append(GroupedQuerySqlHelper.getSortClause());
//        return sql.toString();
//    }
//
//    public String selectMaxRowBoundsByGroupedQuery(MappedStatement ms) {
//        return selectMaxByGroupedQuery(ms);
//    }
//
//    public String selectMinByGroupedQuery(MappedStatement ms) {
//        Class<?> entityClass = getEntityClass(ms);
//        StringBuilder sql = new StringBuilder();
//        sql.append(GroupedQuerySqlHelper.getBindFilterParams(ms.getConfiguration().isMapUnderscoreToCamelCase()));
//        //支持查询指定列
//        sql.append(GroupedQuerySqlHelper.getSelectMin());
//        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
//        sql.append(GroupedQuerySqlHelper.getWhereClause());
//        sql.append(GroupedQuerySqlHelper.getGroupByClause());
//        sql.append(GroupedQuerySqlHelper.getHavingClause());
//        sql.append(GroupedQuerySqlHelper.getSortClause());
//        return sql.toString();
//    }
//
//    public String selectMinRowBoundsByGroupedQuery(MappedStatement ms) {
//        return selectMinByGroupedQuery(ms);
//    }
//
//    /// region for xml query
//
//    public static Map<String, Object> getGroupedQueryParamInternal(
//            final GroupedQuery groupedQuery,
//            final boolean isMapUnderscoreToCamelCase) {
//        return groupedQuery.toQueryParamMap(isMapUnderscoreToCamelCase);
//    }
//    // endregion
//
//    public Class<?> getSelectClass(MappedStatement ms) {
//        String msId = ms.getId() + "_selectClass";
//        if (entityClassMap.containsKey(msId)) {
//            return entityClassMap.get(msId);
//        } else {
//            Class<?> mapperClass = getMapperClass(msId);
//            Type[] types = mapperClass.getGenericInterfaces();
//            for (Type type : types) {
//                if (type instanceof ParameterizedType) {
//                    ParameterizedType t = (ParameterizedType) type;
//                    if (t.getRawType() == this.mapperClass || this.mapperClass.isAssignableFrom((Class<?>) t.getRawType())) {
//                        if (t.getActualTypeArguments().length > 1) {
//                            Class<?> returnType = (Class<?>) t.getActualTypeArguments()[t.getActualTypeArguments().length - 1];
//                            //获取该类型后，第一次对该类型进行初始化
//                            EntityHelper.initEntityNameMap(returnType, mapperHelper.getConfig());
//                            entityClassMap.put(msId, returnType);
//                            return returnType;
//                        }
//
//                    }
//                }
//            }
//        }
//        throw new MapperException("无法获取 " + msId + " 方法的泛型信息!");
//    }
}
