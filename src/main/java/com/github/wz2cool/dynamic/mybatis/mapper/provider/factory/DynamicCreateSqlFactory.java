package com.github.wz2cool.dynamic.mybatis.mapper.provider.factory;

import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import com.github.wz2cool.dynamic.mybatis.mapper.helper.DynamicQuerySqlHelper;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;

import javax.annotation.Nullable;
import java.util.Arrays;
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

            final String template = "and %s != ''";
            for (ProviderColumn column : providerTable.getColumns()) {
                final String stringIf = column.getColumnType().isAssignableFrom(String.class)
                        ? CommonsHelper.format(template, column.getJavaColumn()) : "";
                sqlBuilder.append(CommonsHelper.format("<if test=\"%s != null %s\">%s,</if>",
                        column.getJavaColumn(), stringIf, column.getDbColumn()));
            }
            sqlBuilder.append("</trim>");

            sqlBuilder.append("<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">");
            for (ProviderColumn column : providerTable.getColumns()) {
                final String stringIf = column.getColumnType().isAssignableFrom(String.class)
                        ? CommonsHelper.format(template, column.getJavaColumn()) : "";
                sqlBuilder.append(CommonsHelper.format("<if test=\"%s != null %s\">#{%s},</if>",
                        column.getJavaColumn(),stringIf, column.getJavaColumn()));
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
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("<script>");
        sqlBuilder.append(CommonsHelper.format("update %s ", providerTable.getTableName()));
        sqlBuilder.append("<set>");
        sqlBuilder.append(this.getDynamicDefaultIfSet(null, updateSelective, true));
        sqlBuilder.append("</set>");
        sqlBuilder.append(CommonsHelper.format(" where %s = #{%s}",
                providerTable.getPrimaryKey().getDbColumn(), providerTable.getPrimaryKey().getJavaColumn()));
        sqlBuilder.append("</script>");
        return sqlBuilder.toString();
    }


    /**
     * @return sql
     */
    public String getDynamicQuery() {
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


    public String getDynamicDelete() {
        Class<?> entityClass = providerTable.getEntityClass();
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("<script>");
        //add bind
        sqlBuilder.append(DynamicQuerySqlHelper.getBindFilterParams(true));
        sqlBuilder.append("delete from " + providerTable.getTableName() + " ");
        sqlBuilder.append(DynamicQuerySqlHelper.getWhereClause(entityClass));
        sqlBuilder.append("</script>");
        return sqlBuilder.toString();
    }


    public String getDynamicCount() {
        Class<?> entityClass = providerTable.getEntityClass();
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("<script>");
        //add bind
        sqlBuilder.append(DynamicQuerySqlHelper.getBindFilterParams(true));
        //count(*) 和count(0)  一样的
        sqlBuilder.append("select COUNT(*) from " + providerTable.getTableName() + " ");
        sqlBuilder.append(DynamicQuerySqlHelper.getWhereClause(entityClass));
        sqlBuilder.append("</script>");
        return sqlBuilder.toString();
    }

    /**
     * @return sql
     */
    public String getDynamicUpdate() {
        Class<?> entityClass = providerTable.getEntityClass();
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("<script>");
        //add bind
        sqlBuilder.append(DynamicQuerySqlHelper.getUpdateBindFilterParams(true));
        sqlBuilder.append(CommonsHelper.format("update %s ", providerTable.getTableName()));
        sqlBuilder.append(DynamicQuerySqlHelper.getSetClause());

//        sqlBuilder.append(this.getDynamicDefaultIfSet("entity", true));

        sqlBuilder.append(DynamicQuerySqlHelper.getWhereClause(entityClass));
        sqlBuilder.append("</script>");
        return sqlBuilder.toString();
    }

    public String getDynamicUpdateForSelective() {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("<script>");
        //add bind
        sqlBuilder.append(DynamicQuerySqlHelper.getBindFilterParams(true));
        sqlBuilder.append(CommonsHelper.format("update %s ", providerTable.getTableName()));
        sqlBuilder.append("<set>");
        sqlBuilder.append(this.getDynamicDefaultIfSet("entity", true, true));
        sqlBuilder.append("</set>");
        sqlBuilder.append(DynamicQuerySqlHelper.getWhereClause(providerTable.getEntityClass()));
        sqlBuilder.append("</script>");
        System.out.println(sqlBuilder.toString());
        return sqlBuilder.toString();
    }


    //    end
    //#####################################################################################################
    //############################################公共私有方法,请勿更改######################################
    //#####################################################################################################
    private static final String if_set_template = "<if test=\"aliasName%s != null\">%s = #{aliasName%s},</if>";
    private static final String if_set_string_template = "<if test=\"aliasName%s != null and aliasName%s != ''\">%s = #{aliasName%s},</if>";
    private static final String set_template = "%s = #{aliasName%s}";
    private static final String ALIAS_NAME = "aliasName";


    /**
     * @param aliasName       是否生成别名:
     *                        假设传了aaa:  #{aaa.parseMethod}
     *                        如果不传则为默认:  #{parseMethod}
     * @param setIf           是否生成带if的:
     *                        带if的: <if test="test.parseMethod != null and test.parseMethod != ''">parse_method = #{test.parseMethod},</if>
     *                        不带if的: parse_method = #{aaaaa.parseMethod}
     * @param breakPrimaryKey 是否跳过主键. 不生成主键的条件
     * @return sql
     */
    private String getDynamicDefaultIfSet(@Nullable String aliasName, boolean setIf, boolean breakPrimaryKey) {
        final String aliasNameNew = aliasName == null ? "" : aliasName + ".";
        final StringBuilder sqlBuilder = new StringBuilder();

        if (setIf) {
            final String setIfTemplate = if_set_template.replace(ALIAS_NAME, aliasNameNew);
            final String setIfStringTemplate = if_set_string_template.replace(ALIAS_NAME, aliasNameNew);
            for (ProviderColumn column : providerTable.getColumns()) {
                if (column.isPrimaryKey && breakPrimaryKey) {
                    continue;
                }
                if (column.getColumnType().isAssignableFrom(String.class)) {
                    sqlBuilder.append(CommonsHelper.format(setIfStringTemplate, column.getJavaColumn(), column.getJavaColumn()
                            , column.getDbColumn(), column.getJavaColumn()));
                } else {
                    sqlBuilder.append(CommonsHelper.format(setIfTemplate, column.getJavaColumn(), column.getDbColumn(), column.getJavaColumn()));
                }
            }
            return sqlBuilder.toString();

        }


        final String setTemplate = set_template.replace(ALIAS_NAME, aliasNameNew);
        for (ProviderColumn column : providerTable.getColumns()) {
            if (column.isPrimaryKey) {
                continue;
            }
            sqlBuilder.append(CommonsHelper.format(setTemplate, column.getDbColumn(), column.getJavaColumn()));
            sqlBuilder.append(",");
        }
        sqlBuilder.deleteCharAt(sqlBuilder.lastIndexOf(","));
        return sqlBuilder.toString();
    }

    public static void main(String[] args) {
        DynamicCreateSqlFactory sqlFactory = DynamicCreateSqlFactory.getSqlFactory(ProviderTableHelper.getProviderTable(BIConversion.User.class));
        System.out.println(sqlFactory.getDynamicDefaultIfSet("test", true, false));
        System.out.println(sqlFactory.getDynamicDefaultIfSet("test", true, true));

    }
}
