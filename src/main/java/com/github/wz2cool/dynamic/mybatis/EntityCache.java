package com.github.wz2cool.dynamic.mybatis;

import com.github.wz2cool.exception.PropertyNotFoundInternalException;
import com.github.wz2cool.helper.ReflectHelper;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class EntityCache {
    private static EntityCache instance = new EntityCache();
    private final Map<Class, String[]> propertyNameCacheMap = new ConcurrentHashMap<>();
    private final Map<Class, Map<String, ColumnInfo>> columnInfoCacheMap = new ConcurrentHashMap<>();
    private static final String ENTITY_CLASS = "entityClass";

    // region implement singleton.
    private EntityCache() {
    }

    static EntityCache getInstance() {
        return instance;
    }

    // endregion

    String[] getPropertyNames(final Class entityClass) {
        if (entityClass == null) {
            throw new NullPointerException(ENTITY_CLASS);
        }

        if (propertyNameCacheMap.containsKey(entityClass)) {
            return propertyNameCacheMap.get(entityClass);
        } else {
            Field[] fields = ReflectHelper.getProperties(entityClass);
            Collection<String> propertyNames = new ArrayList<>();
            for (Field field : fields) {
                propertyNames.add(field.getName());
            }
            String[] fieldArray = propertyNames.toArray(new String[propertyNames.size()]);
            propertyNameCacheMap.put(entityClass, fieldArray);
            return fieldArray;
        }
    }

    boolean hasProperty(final Class entityClass, final String propertyName) {
        if (StringUtils.isBlank(propertyName)) {
            return false;
        }

        String[] propertyNames = getPropertyNames(entityClass);
        for (String pName : propertyNames) {
            if (propertyName.equalsIgnoreCase(pName)) {
                return true;
            }
        }

        return false;
    }

    ColumnInfo getColumnInfo(Class entityClass, String propertyName) {
        if (propertyName == null) {
            throw new NullPointerException("propertyName");
        }

        Map<String, ColumnInfo> propertyDbColumnMap = getPropertyColumnInfoMap(entityClass);
        if (!propertyDbColumnMap.containsKey(propertyName)) {
            throw new PropertyNotFoundInternalException(String.format("Can't found property: %s", propertyName));
        }

        return propertyDbColumnMap.get(propertyName);
    }

    ColumnInfo[] getColumnInfos(Class entityClass) {
        Map<String, ColumnInfo> propertyDbColumnMap = getPropertyColumnInfoMap(entityClass);
        Collection<ColumnInfo> columnInfoCollection = propertyDbColumnMap.values();
        return columnInfoCollection.toArray(new ColumnInfo[columnInfoCollection.size()]);
    }

    private Map<String, ColumnInfo> getPropertyColumnInfoMap(Class entityClass) {
        if (entityClass == null) {
            throw new NullPointerException(ENTITY_CLASS);
        }

        Map<String, ColumnInfo> propertyDbColumnMap;
        if (columnInfoCacheMap.containsKey(entityClass)) {
            propertyDbColumnMap = columnInfoCacheMap.get(entityClass);
        } else {
            Map<String, ColumnInfo> map = new ConcurrentHashMap<>();
            Field[] properties = ReflectHelper.getProperties(entityClass);
            for (Field field : properties) {
                field.setAccessible(true);
                ColumnInfo columnInfo = new ColumnInfo();
                columnInfo.setField(field);
                String pName = field.getName();
                boolean updatable = EntityHelper.isPropertyUpdatable(pName, properties);
                boolean insertable = EntityHelper.isPropertyInsertable(pName, properties);
                String columnName = EntityHelper.getColumnNameByProperty(pName, properties);

                columnInfo.setUpdatable(updatable);
                columnInfo.setInsertable(insertable);
                columnInfo.setColumnName(columnName);

                Column column = EntityHelper.getColumnByProperty(pName, properties);
                String tableOrAlias = column == null ? "" : column.table();
                columnInfo.setTableOrAlias(tableOrAlias);
                map.put(pName, columnInfo);
            }
            columnInfoCacheMap.put(entityClass, map);
            propertyDbColumnMap = map;
        }

        return propertyDbColumnMap;
    }
}
