package com.frwan.query.dynamic.mybatis;

import com.frwan.query.constants.ParamConstants;
import com.frwan.query.exception.PropertyNotFoundInternalException;
import com.frwan.query.helper.ReflectHelper;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class EntityCache {
    private static EntityCache instance = new EntityCache();
    private final Map<Class, String[]> propertyNameCacheMap = new HashMap<>();
    private final Map<Class, Map<String, QueryColumnInfo>> queryColumnInfoCacheMap = new HashMap<>();
    private final Map<Class, Map<String, DbColumnInfo>> dbColumnInfoCacheMap = new HashMap<>();

    // region implement singleton.
    private EntityCache() {
    }

    static EntityCache getInstance() {
        return instance;
    }

    // endregion

    synchronized String[] getPropertyNames(final Class entityClass) {
        if (entityClass == null) {
            throw new NullPointerException(ParamConstants.ENTITY_CLASS);
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

    synchronized QueryColumnInfo getQueryColumnInfo(Class entityClass, String propertyName) {
        if (entityClass == null) {
            throw new NullPointerException(ParamConstants.ENTITY_CLASS);
        }

        if (propertyName == null) {
            throw new NullPointerException("propertyName");
        }

        Map<String, QueryColumnInfo> propertyQueryColumnMap;
        if (queryColumnInfoCacheMap.containsKey(entityClass)) {
            propertyQueryColumnMap = queryColumnInfoCacheMap.get(entityClass);
        } else {
            Field[] properties = ReflectHelper.getProperties(entityClass);
            Map<String, QueryColumnInfo> map = new HashMap<>();
            for (Field p : properties) {
                String pName = p.getName();
                String queryColumn = EntityHelper.getQueryColumnByProperty(pName, properties);
                QueryColumnInfo queryColumnInfo = new QueryColumnInfo();
                queryColumnInfo.setQueryColumn(queryColumn);
                queryColumnInfo.setField(p);

                map.put(pName, queryColumnInfo);
            }

            queryColumnInfoCacheMap.put(entityClass, map);
            propertyQueryColumnMap = map;
        }

        if (!propertyQueryColumnMap.containsKey(propertyName)) {
            throw new PropertyNotFoundInternalException(String.format("Can't found property: %s", propertyName));
        }

        return propertyQueryColumnMap.get(propertyName);
    }

    synchronized DbColumnInfo getDbColumnInfo(Class entityClass, String propertyName) {
        if (propertyName == null) {
            throw new NullPointerException("propertyName");
        }

        Map<String, DbColumnInfo> propertyDbColumnMap = getPropertyDbColumnInfoMap(entityClass);
        if (!propertyDbColumnMap.containsKey(propertyName)) {
            throw new PropertyNotFoundInternalException(String.format("Can't found property: %s", propertyName));
        }

        return propertyDbColumnMap.get(propertyName);
    }

    synchronized DbColumnInfo[] getDbColumnInfos(Class entityClass) {
        Map<String, DbColumnInfo> propertyDbColumnMap = getPropertyDbColumnInfoMap(entityClass);
        Collection<DbColumnInfo> dbColumnInfoCollection = propertyDbColumnMap.values();
        return dbColumnInfoCollection.toArray(new DbColumnInfo[dbColumnInfoCollection.size()]);
    }

    private synchronized Map<String, DbColumnInfo> getPropertyDbColumnInfoMap(Class entityClass) {
        if (entityClass == null) {
            throw new NullPointerException(ParamConstants.ENTITY_CLASS);
        }

        Map<String, DbColumnInfo> propertyDbColumnMap;
        if (dbColumnInfoCacheMap.containsKey(entityClass)) {
            propertyDbColumnMap = dbColumnInfoCacheMap.get(entityClass);
        } else {
            Map<String, DbColumnInfo> map = new HashMap<>();
            Field[] properties = ReflectHelper.getProperties(entityClass);
            for (Field field : properties) {
                field.setAccessible(true);
                DbColumnInfo dbColumnInfo = new DbColumnInfo();
                dbColumnInfo.setField(field);
                String pName = field.getName();
                boolean updateIfNull = EntityHelper.isPropertyUpdateIfNull(pName, properties);
                String dbColumnName = EntityHelper.getDBColumnNameByProperty(pName, properties);
                dbColumnInfo.setUpdateIfNull(updateIfNull);
                dbColumnInfo.setDbColumnName(dbColumnName);
                map.put(pName, dbColumnInfo);
            }
            dbColumnInfoCacheMap.put(entityClass, map);
            propertyDbColumnMap = map;
        }

        return propertyDbColumnMap;
    }
}
