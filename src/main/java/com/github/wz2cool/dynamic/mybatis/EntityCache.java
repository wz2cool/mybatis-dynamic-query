package com.github.wz2cool.dynamic.mybatis;

import com.github.wz2cool.dynamic.mybatis.annotation.Column;
import com.github.wz2cool.exception.PropertyNotFoundInternalException;
import com.github.wz2cool.helper.ReflectHelper;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class EntityCache {
    private static EntityCache instance = new EntityCache();
    private final Map<Class, String[]> propertyNameCacheMap = new HashMap<>();
    private final Map<Class, Map<String, ColumnInfo>> columnInfoCacheMap = new HashMap<>();
    private static final String ENTITY_CLASS = "entityClass";

    // region implement singleton.
    private EntityCache() {
    }

    static EntityCache getInstance() {
        return instance;
    }

    // endregion

    synchronized String[] getPropertyNames(final Class entityClass) {
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

    synchronized boolean hasProperty(final Class entityClass, final String propertyName) {
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

    synchronized ColumnInfo getColumnInfo(Class entityClass, String propertyName) {
        if (propertyName == null) {
            throw new NullPointerException("propertyName");
        }

        Map<String, ColumnInfo> propertyDbColumnMap = getPropertyColumnInfoMap(entityClass);
        if (!propertyDbColumnMap.containsKey(propertyName)) {
            throw new PropertyNotFoundInternalException(String.format("Can't found property: %s", propertyName));
        }

        return propertyDbColumnMap.get(propertyName);
    }

    synchronized ColumnInfo[] getColumnInfos(Class entityClass) {
        Map<String, ColumnInfo> propertyDbColumnMap = getPropertyColumnInfoMap(entityClass);
        Collection<ColumnInfo> columnInfoCollection = propertyDbColumnMap.values();
        return columnInfoCollection.toArray(new ColumnInfo[columnInfoCollection.size()]);
    }

    private synchronized Map<String, ColumnInfo> getPropertyColumnInfoMap(Class entityClass) {
        if (entityClass == null) {
            throw new NullPointerException(ENTITY_CLASS);
        }

        Map<String, ColumnInfo> propertyDbColumnMap;
        if (columnInfoCacheMap.containsKey(entityClass)) {
            propertyDbColumnMap = columnInfoCacheMap.get(entityClass);
        } else {
            Map<String, ColumnInfo> map = new HashMap<>();
            Field[] properties = ReflectHelper.getProperties(entityClass);
            for (Field field : properties) {
                field.setAccessible(true);
                ColumnInfo columnInfo = new ColumnInfo();
                columnInfo.setField(field);
                String pName = field.getName();
                boolean updateIfNull = EntityHelper.isPropertyUpdateIfNull(pName, properties);
                String columnName = EntityHelper.getColumnNameByProperty(pName, properties);
                columnInfo.setUpdateIfNull(updateIfNull);
                columnInfo.setColumnName(columnName);

                Column column = EntityHelper.getColumnByProperty(pName, properties);
                String tableOrAlias = column == null ? "" : column.tableOrAlias();
                columnInfo.setTableOrAlias(tableOrAlias);
                map.put(pName, columnInfo);
            }
            columnInfoCacheMap.put(entityClass, map);
            propertyDbColumnMap = map;
        }

        return propertyDbColumnMap;
    }
}
