package com.frwan.query.dynamic.mybatis;

import com.frwan.query.dynamic.mybatis.annotation.DbColumn;
import com.frwan.query.dynamic.mybatis.annotation.DbTable;
import com.frwan.query.dynamic.mybatis.annotation.QueryColumn;
import com.frwan.query.exception.PropertyNotFoundInternalException;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by Frank on 7/10/2017.
 */
class EntityHelper {
    private EntityHelper() {
        throw new UnsupportedOperationException();
    }

    static String getTableName(final Class tableClass) {
        if (tableClass == null) {
            throw new NullPointerException("tableClass");
        }

        Annotation[] annotations = tableClass.getAnnotations();
        if (annotations != null && annotations.length > 0) {
            Annotation dbTableAnnotation = null;
            for (Annotation annotation : annotations) {
                if (annotation != null && annotation instanceof DbTable) {
                    dbTableAnnotation = annotation;
                    break;
                }
            }

            if (dbTableAnnotation != null) {
                DbTable dbTable = (DbTable) dbTableAnnotation;
                String dbTableName = dbTable.name();
                if (StringUtils.isNotBlank(dbTableName)) {
                    return dbTableName;
                }
            }
        }

        String useTableName = tableClass.getSimpleName();
        return camelCaseToDashCase(useTableName);
    }

    static String getQueryColumnByProperty(final String propertyName, final Field[] properties) {
        Field matchProperty = getPropertyField(propertyName, properties);
        Annotation[] annotations = matchProperty.getAnnotations();
        if (annotations != null && annotations.length > 0) {
            Annotation queryColumnAnnotation = null;
            for (Annotation annotation : annotations) {
                if (annotation != null && annotation instanceof QueryColumn) {
                    queryColumnAnnotation = annotation;
                    break;
                }
            }

            if (queryColumnAnnotation != null) {
                QueryColumn queryColumn = (QueryColumn) queryColumnAnnotation;
                return queryColumn.name();
            }
        }

        String usePropertyName = matchProperty.getName();
        return camelCaseToDashCase(usePropertyName);
    }

    static String getDBColumnNameByProperty(final String propertyName, final Field[] properties) {
        Field matchProperty = getPropertyField(propertyName, properties);
        DbColumn dbColumn = getDbColumnByProperty(propertyName, properties);
        if (dbColumn != null && StringUtils.isNotBlank(dbColumn.name())) {
            return dbColumn.name();
        }


        String usePropertyName = matchProperty.getName();
        return camelCaseToDashCase(usePropertyName);
    }

    static boolean isPropertyUpdateIfNull(final String propertyName, final Field[] properties) {
        DbColumn dbColumn = getDbColumnByProperty(propertyName, properties);
        if (dbColumn == null) {
            return false;
        }
        return dbColumn.updateIfNull();
    }

    static DbColumn getDbColumnByProperty(final String propertyName, final Field[] properties) {
        Field matchProperty = getPropertyField(propertyName, properties);
        Annotation[] annotations = matchProperty.getAnnotations();
        if (annotations != null && annotations.length > 0) {
            Annotation dbColumnAnnotation = null;
            for (Annotation annotation : annotations) {
                if (annotation != null && annotation instanceof DbColumn) {
                    dbColumnAnnotation = annotation;
                    break;
                }
            }

            return (DbColumn) dbColumnAnnotation;
        }

        return null;
    }

    static Field getPropertyField(final String propertyName, final Field[] properties) {
        if (StringUtils.isBlank(propertyName) || properties == null || properties.length == 0) {
            throw new PropertyNotFoundInternalException(String.format("Can't find property: %s", propertyName));
        }

        Field result = null;
        for (Field property : properties) {
            if (propertyName.trim().equalsIgnoreCase(property.getName())) {
                result = property;
                break;
            }
        }

        if (result != null) {
            return result;
        } else {
            throw new PropertyNotFoundInternalException(String.format("Can't find property: %s", propertyName));
        }
    }

    private static String camelCaseToDashCase(String str) {
        return str.replaceAll("(.)(\\p{Lu})", "$1_$2").toLowerCase();
    }
}
