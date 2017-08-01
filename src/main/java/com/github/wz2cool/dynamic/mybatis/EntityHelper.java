package com.github.wz2cool.dynamic.mybatis;

import com.github.wz2cool.dynamic.mybatis.annotation.Column;
import com.github.wz2cool.dynamic.mybatis.annotation.Table;
import com.github.wz2cool.exception.PropertyNotFoundInternalException;
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
                if (annotation != null && annotation instanceof Table) {
                    dbTableAnnotation = annotation;
                    break;
                }
            }

            if (dbTableAnnotation != null) {
                Table table = (Table) dbTableAnnotation;
                String dbTableName = table.name();
                if (StringUtils.isNotBlank(dbTableName)) {
                    return dbTableName;
                }
            }
        }

        String useTableName = tableClass.getSimpleName();
        return camelCaseToDashCase(useTableName);
    }

    static String getColumnNameByProperty(final String propertyName, final Field[] properties) {
        Field matchProperty = getPropertyField(propertyName, properties);
        Column column = getColumnByProperty(propertyName, properties);
        if (column != null && StringUtils.isNotBlank(column.name())) {
            return column.name();
        }


        String usePropertyName = matchProperty.getName();
        return camelCaseToDashCase(usePropertyName);
    }

    static JdbcType getJdbcTypeByProperty(final String propertyName, final Field[] properties) {
        Column column = getColumnByProperty(propertyName, properties);
        if (column == null) {
            return null;
        }
        return column.jdbcType();
    }

    static boolean isPropertyUpdateIfNull(final String propertyName, final Field[] properties) {
        Column column = getColumnByProperty(propertyName, properties);
        if (column == null) {
            // default don't update property.
            return false;
        }
        return column.updateIfNull();
    }

    static boolean isPropertyInsertIfNull(final String propertyName, final Field[] properties) {
        Column column = getColumnByProperty(propertyName, properties);
        if (column == null) {
            // default insert property.
            return false;
        }
        return column.insertIfNull();
    }

    static Column getColumnByProperty(final String propertyName, final Field[] properties) {
        Field matchProperty = getPropertyField(propertyName, properties);
        Annotation[] annotations = matchProperty.getAnnotations();
        if (annotations != null && annotations.length > 0) {
            Annotation dbColumnAnnotation = null;
            for (Annotation annotation : annotations) {
                if (annotation != null && annotation instanceof Column) {
                    dbColumnAnnotation = annotation;
                    break;
                }
            }

            return (Column) dbColumnAnnotation;
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
