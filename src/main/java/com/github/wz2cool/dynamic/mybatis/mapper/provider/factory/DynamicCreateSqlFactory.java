package com.github.wz2cool.dynamic.mybatis.mapper.provider.factory;

import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import com.github.wz2cool.dynamic.mybatis.mapper.helper.DynamicQuerySqlHelper;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 通过本类可以获取动态sql
 *
 * @author wangjin
 */
public final class DynamicCreateSqlFactory {

    private final ProviderTable providerTable;

    private DynamicCreateSqlFactory(ProviderTable providerTable) {
        this.providerTable = providerTable;
    }

    public static DynamicCreateSqlFactory getSqlFactory(ProviderTable providerTable) {
        return new DynamicCreateSqlFactory(providerTable);
    }


    /**
     * @param deleteByPrimaryKey 如果是deleteByPrimaryKey, 则说明只通过主键删除
     * @return sql
     */
    public String getDeleteSql(boolean deleteByPrimaryKey) {
        if (deleteByPrimaryKey) {
            if (providerTable.getPrimaryKey() == null) {
                throw new IllegalArgumentException(CommonsHelper.format("该类[%s]没有发现主键", providerTable.getTableName()));
            }
            // 简单sql
            return CommonsHelper.format("delete from %s where %s = #{%s}",
                    providerTable.getTableName(), providerTable.getPrimaryKey().getDbColumn(), providerTable.getPrimaryKey().getJavaColumn());
        }

        //复杂sql <script>
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("<script>");
        sqlBuilder.append(CommonsHelper.format("delete from %s ", providerTable.getTableName()));
        sqlBuilder.append("<where>");
        for (ProviderColumn column : providerTable.getColumns()) {
            sqlBuilder.append(CommonsHelper.format("<if test=\"%s != null\"> and %s = #{%s}</if>",
                    column.getJavaColumn(), column.getDbColumn(), column.getJavaColumn()));
        }
        sqlBuilder.append("</where>");
        sqlBuilder.append("</script>");
        return sqlBuilder.toString();
    }


    /**
     * @param insertSelective insertSelective
     * @return sql
     */
    public String getInsertSql(boolean insertSelective) {
        if (insertSelective) {
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("<script>");
            sqlBuilder.append("insert into ");
            sqlBuilder.append(providerTable.getTableName());
            sqlBuilder.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
            for (ProviderColumn column : providerTable.getColumns()) {
                sqlBuilder.append(CommonsHelper.format("<if test=\"%s != null\">%s,</if>",
                        column.getJavaColumn(), column.getDbColumn()));
            }
            sqlBuilder.append("</trim>");

            sqlBuilder.append("<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">");
            for (ProviderColumn column : providerTable.getColumns()) {
                sqlBuilder.append(CommonsHelper.format("<if test=\"%s != null\">#{%s},</if>",
                        column.getJavaColumn(), column.getJavaColumn()));
            }
            sqlBuilder.append("</trim>");
            sqlBuilder.append("</script>");
            return sqlBuilder.toString();
        }
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("<script>");
        sqlBuilder.append("insert into ");
        sqlBuilder.append(providerTable.getTableName());
        sqlBuilder.append("(");
        sqlBuilder.append(Arrays.stream(providerTable.getColumns())
                .map(ProviderColumn::getDbColumn).collect(Collectors.joining(",")));
        sqlBuilder.append(") values (");
        sqlBuilder.append(Arrays.stream(providerTable.getColumns())
                .map(ProviderColumn::getJavaColumn)
                .map(a -> CommonsHelper.format("#{%s}", a))
                .collect(Collectors.joining(",")));
        sqlBuilder.append(")");
        sqlBuilder.append("</script>");
        return sqlBuilder.toString();
    }

    /**
     * @param updateSelective updateSelective
     * @return sql
     */
    public String getUpdateByPrimaryKeySql(boolean updateSelective) {
        if (providerTable.getPrimaryKey() == null) {
            throw new IllegalArgumentException(CommonsHelper.format("该类[%s]没有发现主键", providerTable.getTableName()));
        }
        if (updateSelective) {
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("<script>");
            sqlBuilder.append(CommonsHelper.format("update %s ", providerTable.getTableName()));
            sqlBuilder.append("<set>");
            sqlBuilder.append(Arrays.stream(providerTable.getColumns())
                    .filter(a -> !a.isPrimaryKey())
                    .map(a -> CommonsHelper.format("<if test=\"%s != null %s\">%s = #{%s},</if>",
                            a.getJavaColumn(),
                            Objects.equals(a.getColumnType().getSimpleName(),
                                    "String") ? CommonsHelper.format("and %s != ''", a.getJavaColumn()) : "",
                            a.getDbColumn(), a.getJavaColumn()))
                    .collect(Collectors.joining()));
            sqlBuilder.append("</set>");
            sqlBuilder.append(CommonsHelper.format(" where %s = #{%s}",
                    providerTable.getPrimaryKey().getDbColumn(), providerTable.getPrimaryKey().getJavaColumn()));
            sqlBuilder.append("</script>");
            return sqlBuilder.toString();
        }
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("<script>");
        sqlBuilder.append(CommonsHelper.format("update %s set ", providerTable.getTableName()));
        sqlBuilder.append(Arrays.stream(providerTable.getColumns())
                .filter(a -> !a.isPrimaryKey())
                .map(a -> CommonsHelper.format("%s = #{%s}", a.getDbColumn(), a.getJavaColumn()))
                .collect(Collectors.joining(",")));
        sqlBuilder.append(CommonsHelper.format(" where %s = #{%s}",
                providerTable.getPrimaryKey().getDbColumn(), providerTable.getPrimaryKey().getJavaColumn()));
        sqlBuilder.append("</script>");
        return sqlBuilder.toString();
    }


    /**
     * @return sql
     */
    public String getDynamicSql() {
        Class<?> entityClass = providerTable.getEntityClass();
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("<script>");
        //add bind
        sqlBuilder.append(DynamicQuerySqlHelper.getBindFilterParams(true));
        sqlBuilder.append("select");
        sqlBuilder.append(String.format("<if test=\"%s.%s\">distinct</if>",
                MapperConstants.DYNAMIC_QUERY_PARAMS, MapperConstants.DISTINCT));
        //支持查询指定列
        sqlBuilder.append(DynamicQuerySqlHelper.getSelectColumnsClause());
        sqlBuilder.append(" from " + providerTable.getTableName() + " ");
        sqlBuilder.append(DynamicQuerySqlHelper.getWhereClause(entityClass));
        sqlBuilder.append(DynamicQuerySqlHelper.getSortClause());
        sqlBuilder.append("</script>");
        return sqlBuilder.toString();
    }


}
