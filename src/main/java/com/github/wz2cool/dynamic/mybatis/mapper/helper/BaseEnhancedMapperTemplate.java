package com.github.wz2cool.dynamic.mybatis.mapper.helper;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultFlag;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Frank
 */
public abstract class BaseEnhancedMapperTemplate extends MapperTemplate {

    private static Map<Class, ResultMap> resultMapCache = new ConcurrentHashMap<>();

    public BaseEnhancedMapperTemplate(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    @Override
    protected void setResultType(MappedStatement ms, Class<?> entityClass) {
        List<ResultMap> resultMaps = new ArrayList<>();
        resultMaps.add(getResultMap(entityClass, ms.getConfiguration()));
        MetaObject metaObject = SystemMetaObject.forObject(ms);
        metaObject.setValue("resultMaps", Collections.unmodifiableList(resultMaps));
    }

    private ResultMap getResultMap(final Class<?> entityClass, final Configuration configuration) {
        ResultMap result = resultMapCache.getOrDefault(entityClass, null);
        if (result != null) {
            return result;
        } else {
            result = getResultMapInternal(entityClass, configuration);
            resultMapCache.put(entityClass, result);
            return result;
        }
    }

    private ResultMap getResultMapInternal(final Class<?> entityClass, final Configuration configuration) {
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        List<ResultMapping> resultMappings = new ArrayList<>();
        Set<EntityColumn> entityColumnSet = entityTable.getEntityClassColumns();

        boolean isMapUnderscoreToCamelCase = configuration.isMapUnderscoreToCamelCase();
        for (EntityColumn entityColumn : entityColumnSet) {
            String column = entityColumn.getColumn();

            String useColumn = isMapUnderscoreToCamelCase ? com.github.wz2cool.dynamic.mybatis.EntityHelper.camelCaseToUnderscore(column) : column;
            ResultMapping.Builder builder = new ResultMapping.Builder(configuration, entityColumn.getProperty(), useColumn, entityColumn.getJavaType());
            if (entityColumn.getJdbcType() != null) {
                builder.jdbcType(entityColumn.getJdbcType());
            }

            if (entityColumn.getTypeHandler() != null) {
                try {
                    builder.typeHandler(entityColumn.getTypeHandler().newInstance());
                } catch (Exception var9) {
                    throw new MapperException(var9);
                }
            }

            List<ResultFlag> flags = new ArrayList<>();
            if (entityColumn.isId()) {
                flags.add(ResultFlag.ID);
            }

            builder.flags(flags);
            resultMappings.add(builder.build());
        }
        org.apache.ibatis.mapping.ResultMap.Builder builder =
                new org.apache.ibatis.mapping.ResultMap.Builder(configuration, "BaseMapperResultMap", entityClass, resultMappings, true);
        return builder.build();
    }
}
