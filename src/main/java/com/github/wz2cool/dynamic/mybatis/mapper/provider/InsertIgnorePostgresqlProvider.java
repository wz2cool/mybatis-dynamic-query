package com.github.wz2cool.dynamic.mybatis.mapper.provider;

import com.github.wz2cool.dynamic.mybatis.mapper.helper.DynamicQuerySqlHelper;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.SelectKeyHelper;
import tk.mybatis.mapper.mapperhelper.SqlHelper;
import tk.mybatis.mapper.provider.base.BaseInsertProvider;

import java.util.Set;

import static tk.mybatis.mapper.mapperhelper.SqlHelper.insertIntoTable;

/**
 * @author zfx
 */
public class InsertIgnorePostgresqlProvider extends BaseInsertProvider {

    public InsertIgnorePostgresqlProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String insertIgnore(MappedStatement ms) {
        return buildInsertIgnoreSql(ms, false);
    }


    public String insertIgnoreSelective(MappedStatement ms) {
        return buildInsertIgnoreSql(ms, true);
    }

    private void processKey(StringBuilder sql, Class<?> entityClass, MappedStatement ms, Set<EntityColumn> columnList) {
        //Identity列只能有一个
        Boolean hasIdentityKey = false;
        //先处理cache或bind节点
        for (EntityColumn column : columnList) {
            if (column.isIdentity()) {
                //这种情况下,如果原先的字段有值,需要先缓存起来,否则就一定会使用自动增长
                //这是一个bind节点
                sql.append(SqlHelper.getBindCache(column));
                //如果是Identity列，就需要插入selectKey
                //如果已经存在Identity列，抛出异常
                if (hasIdentityKey) {
                    //jdbc类型只需要添加一次
                    if (column.getGenerator() != null && "JDBC".equals(column.getGenerator())) {
                        continue;
                    }
                    throw new MapperException(ms.getId() + "对应的实体类" + entityClass.getName() + "中包含多个MySql的自动增长列,最多只能有一个!");
                }
                //插入selectKey
                SelectKeyHelper.newSelectKeyMappedStatement(ms, column, entityClass, isBEFORE(), getIDENTITY(column));
                hasIdentityKey = true;
            } else if (column.getGenIdClass() != null) {
                sql.append("<bind name=\"").append(column.getColumn()).append("GenIdBind\" value=\"@tk.mybatis.mapper.genid.GenIdUtil@genId(");
                sql.append("_parameter").append(", '").append(column.getProperty()).append("'");
                sql.append(", @").append(column.getGenIdClass().getName()).append("@class");
                sql.append(", '").append(tableName(entityClass)).append("'");
                sql.append(", '").append(column.getColumn()).append("')");
                sql.append("\"/>");
            }

        }
    }

    private void dealColumn(Set<EntityColumn> columnList, EntityColumn logicDeleteColumn, StringBuilder sql, boolean selective) {
        for (EntityColumn column : columnList) {
            if (!column.isInsertable()) {
                continue;
            }
            if (logicDeleteColumn != null && logicDeleteColumn == column) {
                sql.append(SqlHelper.getLogicDeletedValue(column, false)).append(",");
                continue;
            }
            //优先使用传入的属性值,当原属性property!=null时，用原属性
            //自增的情况下,如果默认有值,就会备份到property_cache中,所以这里需要先判断备份的值是否存在
            if (column.isIdentity()) {
                sql.append(SqlHelper.getIfCacheNotNull(column, column.getColumnHolder(null, "_cache", ",")));
            } else {
                //其他情况值仍然存在原property中
                sql.append(SqlHelper.getIfNotNull(column, column.getColumnHolder(null, null, ","), isNotEmpty()));
            }
            //当属性为null时，如果存在主键策略，会自动获取值，如果不存在，则使用null
            if (column.isIdentity()) {
                sql.append(SqlHelper.getIfCacheIsNull(column, column.getColumnHolder() + ","));
            } else {
                if (!selective){
                    //当null的时候，如果不指定jdbcType，oracle可能会报异常，指定VARCHAR不影响其他
                    sql.append(SqlHelper.getIfIsNull(column, column.getColumnHolder(null, null, ","), isNotEmpty()));
                }
            }
        }
    }
    private String buildInsertIgnoreSql(MappedStatement ms, boolean selective) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        EntityColumn logicDeleteColumn = SqlHelper.getLogicDeleteColumn(entityClass);
        processKey(sql, entityClass, ms, columnList);
        sql.append(insertIntoTable(entityClass, tableName(entityClass)));

        if (selective) {
            sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
            for (EntityColumn column : columnList) {
                if (!column.isInsertable()) continue;
                if (column.isIdentity() || (logicDeleteColumn != null && logicDeleteColumn == column)) {
                    sql.append(column.getColumn()).append(",");
                } else {
                    sql.append(SqlHelper.getIfNotNull(column, column.getColumn() + ",", isNotEmpty()));
                }
            }
            sql.append("</trim>");
        } else {
            sql.append(SqlHelper.insertColumns(entityClass, false, false, false));
        }

        sql.append("<trim prefix=\"VALUES(\" suffix=\")\" suffixOverrides=\",\">");
        dealColumn(columnList, logicDeleteColumn, sql, selective);
        sql.append("</trim>");

        DynamicQuerySqlHelper.insertIgnoreIntoPostgresqlTable(entityClass).ifPresent(sql::append);
        return sql.toString();
    }
}