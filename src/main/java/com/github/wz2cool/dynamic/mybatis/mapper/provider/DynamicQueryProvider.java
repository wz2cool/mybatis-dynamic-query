package com.github.wz2cool.dynamic.mybatis.mapper.provider;

import com.github.wz2cool.dynamic.DynamicQuery;
import com.github.wz2cool.dynamic.UpdateQuery;
import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.mybatis.mapper.SelectByDynamicQueryMapper;
import com.github.wz2cool.dynamic.mybatis.mapper.SelectMapper;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import com.github.wz2cool.dynamic.mybatis.mapper.helper.DynamicQuerySqlHelper;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.factory.DynamicCreateSqlFactory;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.factory.ProviderColumn;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.factory.ProviderFactory;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.factory.ProviderTable;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 处理动态查询
 *
 * @author Frank
 * @author wangjin
 */
public class DynamicQueryProvider {
    private static final Map<String, String> DYNAMIC_QUERY_CACHE = new ConcurrentHashMap<>(256);


    /**
     * 根据{@link SelectProvider#method()}解析的方法
     * 如:
     * {@link SelectByDynamicQueryMapper}
     *
     * @param providerContext 上下文
     * @return sql脚本
     */
    public String dynamicQuery(ProviderContext providerContext) {
        ProviderTable providerTable = ProviderFactory.create(providerContext);
        if (DYNAMIC_QUERY_CACHE.containsKey(providerTable.getKey())) {
            return DYNAMIC_QUERY_CACHE.get(providerTable.getKey());
        }
        final String sql = DynamicCreateSqlFactory.getSqlFactory(providerTable).getDynamicQuery();
        DYNAMIC_QUERY_CACHE.put(providerTable.getKey(), sql);
        return sql;
    }


    public String selectCountByDynamicQuery(ProviderContext providerContext) {
        ProviderTable providerTable = ProviderFactory.create(providerContext);
        if (DYNAMIC_QUERY_CACHE.containsKey(providerTable.getKey())) {
            return DYNAMIC_QUERY_CACHE.get(providerTable.getKey());
        }
        final String sql = DynamicCreateSqlFactory.getSqlFactory(providerTable).getDynamicCount();
        DYNAMIC_QUERY_CACHE.put(providerTable.getKey(), sql);
        return sql;
    }


    /**
     * 根据{@link SelectProvider#method()}解析的方法
     * 如:
     * {@link SelectMapper}
     *
     * @param providerContext 上下文
     * @return sql脚本
     */
    public String selectAll(ProviderContext providerContext) {
        ProviderTable providerTable = ProviderFactory.create(providerContext);
        if (DYNAMIC_QUERY_CACHE.containsKey(providerTable.getKey())) {
            return DYNAMIC_QUERY_CACHE.get(providerTable.getKey());
        }
        final String sql = "select * from " + providerTable.getTableName();
        DYNAMIC_QUERY_CACHE.put(providerTable.getKey(), sql);
        return sql;
    }


    /**
     * 待实体条件的查询
     *
     * @param providerContext
     * @return
     */
    public String select(ProviderContext providerContext) {
        ProviderTable providerTable = ProviderFactory.create(providerContext);
        if (DYNAMIC_QUERY_CACHE.containsKey(providerTable.getKey())) {
            return DYNAMIC_QUERY_CACHE.get(providerTable.getKey());
        }
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("<script>");
        sqlBuilder.append("select * from ");
        sqlBuilder.append(providerTable.getTableName());
        sqlBuilder.append(" ");
        sqlBuilder.append("<where>");
        for (ProviderColumn column : providerTable.getColumns()) {
            sqlBuilder.append(CommonsHelper.format("<if test=\"%s != null\"> and %s = #{%s}</if>\n",
                    column.getJavaColumn(), column.getDbColumn(), column.getJavaColumn()));
        }
        sqlBuilder.append("</where>");
        sqlBuilder.append("</script>");
        final String sql = sqlBuilder.toString();
        System.out.println(sql);
        DYNAMIC_QUERY_CACHE.put(providerTable.getKey(), sql);
        return sql;
    }


    public String selectByPrimaryKey(ProviderContext providerContext) {
        ProviderTable providerTable = ProviderFactory.create(providerContext);
        if (DYNAMIC_QUERY_CACHE.containsKey(providerTable.getKey())) {
            return DYNAMIC_QUERY_CACHE.get(providerTable.getKey());
        }
        if (providerTable.getPrimaryKey() == null) {
            throw new IllegalArgumentException(CommonsHelper.format("该类[%s]没有发现主键", providerTable.getTableName()));
        }

        final String sql = CommonsHelper.format("select * from %s where %s = #{%s}"
                , providerTable.getTableName(), providerTable.getPrimaryKey().getDbColumn(),
                providerTable.getPrimaryKey().getJavaColumn());
        DYNAMIC_QUERY_CACHE.put(providerTable.getKey(), sql);
        return sql;
    }

//
//    public String selectMaxByDynamicQuery(ProviderContext ms) {
//        Class<?> entityClass = getEntityClass(ms);
//        StringBuilder sql = new StringBuilder();
//        sql.append(DynamicQuerySqlHelper.getBindFilterParams(ms.getConfiguration().isMapUnderscoreToCamelCase()));
//        sql.append(DynamicQuerySqlHelper.getSelectMax());
//        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
//        sql.append(DynamicQuerySqlHelper.getWhereClause(entityClass));
//        return sql.toString();
//    }
//
//    public String selectMinByDynamicQuery(ProviderContext ms) {
//        Class<?> entityClass = getEntityClass(ms);
//        StringBuilder sql = new StringBuilder();
//        sql.append(DynamicQuerySqlHelper.getBindFilterParams(ms.getConfiguration().isMapUnderscoreToCamelCase()));
//        sql.append(DynamicQuerySqlHelper.getSelectMin());
//        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
//        sql.append(DynamicQuerySqlHelper.getWhereClause(entityClass));
//        return sql.toString();
//    }
//
//    public String selectSumByDynamicQuery(ProviderContext ms) {
//        Class<?> entityClass = getEntityClass(ms);
//        StringBuilder sql = new StringBuilder();
//        sql.append(DynamicQuerySqlHelper.getBindFilterParams(ms.getConfiguration().isMapUnderscoreToCamelCase()));
//        sql.append(DynamicQuerySqlHelper.getSelectSum());
//        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
//        sql.append(DynamicQuerySqlHelper.getWhereClause(entityClass));
//        return sql.toString();
//    }
//
//    public String selectAvgByDynamicQuery(ProviderContext ms) {
//        Class<?> entityClass = getEntityClass(ms);
//        StringBuilder sql = new StringBuilder();
//        sql.append(DynamicQuerySqlHelper.getBindFilterParams(ms.getConfiguration().isMapUnderscoreToCamelCase()));
//        sql.append(DynamicQuerySqlHelper.getSelectAvg());
//        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
//        sql.append(DynamicQuerySqlHelper.getWhereClause(entityClass));
//        return sql.toString();
//    }
//
//    public String deleteByDynamicQuery(ProviderContext ms) {
//        Class<?> entityClass = getEntityClass(ms);
//        StringBuilder sql = new StringBuilder();
//        sql.append(DynamicQuerySqlHelper.getBindFilterParams(ms.getConfiguration().isMapUnderscoreToCamelCase()));
//        if (SqlHelper.hasLogicDeleteColumn(entityClass)) {
//            sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
//            sql.append("<set>");
//            sql.append(SqlHelper.logicDeleteColumnEqualsValue(entityClass, true));
//            sql.append("</set>");
//            MetaObjectUtil.forObject(ms).setValue("sqlCommandType", SqlCommandType.UPDATE);
//        } else {
//            sql.append(SqlHelper.deleteFromTable(entityClass, tableName(entityClass)));
//        }
//        sql.append(DynamicQuerySqlHelper.getWhereClause(entityClass));
//        return sql.toString();
//    }
//

    public String selectByDynamicQuery(ProviderContext providerContext) {
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


    @Deprecated
    public String dynamicSQL(ProviderContext providerContext) {
        return dynamicQuery(providerContext);
    }

//
//    protected void setResultType(MappedStatement ms, Class<?> entityClass) {
//        List<ResultMap> resultMaps = new ArrayList();
//        resultMaps.add(this.getResultMap(entityClass, ms.getConfiguration()));
//        MetaObject metaObject = SystemMetaObject.forObject(ms);
//        metaObject.setValue("resultMaps", Collections.unmodifiableList(resultMaps));
//    }
//


    public String selectRowBoundsByDynamicQuery(ProviderContext providerContext) {
        return selectByDynamicQuery(providerContext);
    }

//    public String updateSelectiveByDynamicQuery(ProviderContext ms) {
//        return updateByDynamicQuery(ms, true);
//    }
//
//    public String updateByDynamicQuery(ProviderContext ms) {
//        return updateByDynamicQuery(ms, false);
//    }

//    public String updateByUpdateQuery(ProviderContext ms) {
//        Class<?> entityClass = getEntityClass(ms);
//        StringBuilder sql = new StringBuilder();
//        sql.append(DynamicQuerySqlHelper.getUpdateBindFilterParams(ms.getConfiguration().isMapUnderscoreToCamelCase()));
//        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass), "example"));
//        sql.append(DynamicQuerySqlHelper.getSetClause());
//        sql.append(DynamicQuerySqlHelper.getWhereClause(entityClass));
//        return sql.toString();
//    }
//
//    private String updateByDynamicQuery(ProviderContext ms, boolean noNull) {
//        Class<?> entityClass = getEntityClass(ms);
//        StringBuilder sql = new StringBuilder();
//        sql.append(DynamicQuerySqlHelper.getBindFilterParams(ms.getConfiguration().isMapUnderscoreToCamelCase()));
//        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass), "example"));
//        sql.append(SqlHelper.updateSetColumns(entityClass, "record", noNull, isNotEmpty()));
//        sql.append(DynamicQuerySqlHelper.getWhereClause(entityClass));
//        return sql.toString();
//    }

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