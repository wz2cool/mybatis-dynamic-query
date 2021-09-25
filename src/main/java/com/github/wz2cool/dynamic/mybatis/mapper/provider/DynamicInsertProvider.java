package com.github.wz2cool.dynamic.mybatis.mapper.provider;

import com.github.wz2cool.dynamic.DynamicQuery;
import com.github.wz2cool.dynamic.UpdateQuery;
import com.github.wz2cool.dynamic.mybatis.QueryHelper;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import com.github.wz2cool.dynamic.mybatis.mapper.helper.DynamicQuerySqlHelper;
import com.github.wz2cool.dynamic.provider.ProviderFactory;
import com.github.wz2cool.dynamic.provider.ProviderTable;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

/**
 * @author Frank
 */
public class DynamicInsertProvider {
    private static final QueryHelper QUERY_HELPER = new QueryHelper();

    public String selectCountByDynamicQuery(ProviderContext providerContext) {
        ProviderTable providerTable = ProviderFactory.create(providerContext);
        Class<?> entityClass = providerTable.getEntityClass();
        return new SQL().INTO_COLUMNS().FROM(providerTable.getTableName())
                .WHERE(DynamicQuerySqlHelper.getWhereClause(entityClass)).toString();
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
        sql.append("form " + providerTable.getTableName() + " ");
        sql.append(DynamicQuerySqlHelper.getWhereClause(entityClass));
        sql.append(DynamicQuerySqlHelper.getSortClause());
        return sql.toString();
    }

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