package com.github.wz2cool.dynamic.mybatis.mapper.provider.factory;

import com.github.wz2cool.dynamic.mybatis.View;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 根据class解析实体结构
 *
 * @author wangjin
 */
public final class ProviderTableHelper {

    private static final Map<Class<?>, ProviderTable> cache = new ConcurrentHashMap<>(255);

    public static ProviderTable getProviderTable(Class<?> entityClass) {
        if (cache.containsKey(entityClass)) {
            return cache.get(entityClass);
        }
        final ProviderTable providerTable = initProviderTable(entityClass);
        cache.put(entityClass, providerTable);
        return providerTable;
    }

    private static ProviderTable initProviderTable(Class<?> entityClass) {
        final String defaultTableName = Optional.ofNullable(entityClass.getAnnotation(Table.class)).map(Table::name)
                .orElse(entityClass.getSimpleName());
        //取字段
        final Field[] declaredFields = entityClass.getDeclaredFields();
        List<ProviderColumn> columnList = new ArrayList<>(declaredFields.length);
        List<ProviderColumn> transientColumnList = new ArrayList<>(declaredFields.length);
        ProviderColumn pk = null;
        boolean isAutoIncrement = false;
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            ProviderColumn.ProviderColumnBuilder builder = ProviderColumn.builder();
            builder.withJavaColumn(declaredField.getName());
            Column column = declaredField.getAnnotation(Column.class);
            if (column == null) {
                builder.withDbColumn(ProviderTableHelper.underline(declaredField.getName()))
                        .withDbColumnTable(null);
            } else {
                builder.withDbColumn(column.table() + "." + column.name())
                        .withDbColumnTable(column.table());
            }
            builder.withColumnType(declaredField.getType())
                    .withField(declaredField);
            if (declaredField.getAnnotation(Id.class) != null) {
                pk = builder.withIsPrimaryKey(true).build();
            }
            ProviderColumn col = builder.build();
            GeneratedValue annotation = declaredField.getAnnotation(GeneratedValue.class);
            if (annotation != null) {
                if (annotation.strategy() == GenerationType.IDENTITY ||
                        annotation.strategy() == GenerationType.AUTO) {
                    //说明是自增
                    isAutoIncrement = true;
                }
            }
            if (declaredField.getAnnotation(Transient.class) == null) {
                columnList.add(col);
            } else {
                transientColumnList.add(col);
            }
        }
        ProviderColumn[] columns = columnList.toArray(new ProviderColumn[0]);
        final String tableName = StringUtils.defaultString(StringUtils.defaultString(
                Optional.ofNullable(entityClass.getAnnotation(View.class))
                        .map(View::value).orElse(null),
                Optional.ofNullable(entityClass.getAnnotation(com.github.wz2cool.dynamic.annotation.View.class))
                        .map(com.github.wz2cool.dynamic.annotation.View::value).orElse(null)), defaultTableName);
        return ProviderTable.builder()
                .withTableName(tableName)
                .withEntityClass(entityClass)
                .withFields(declaredFields)
                .withTransientColumns(transientColumnList.toArray(new ProviderColumn[0]))
                .withColumns(columns)
                .withPrimaryKey(pk)
                .withIsAutoIncrement(isAutoIncrement)
                .withColumnHash(Arrays.stream(columns)
                        .collect(Collectors.toMap(ProviderColumn::getJavaColumn, Function.identity())))
                .build();
    }

    /**
     * 驼峰转下划线
     *
     * <code>
     * aBCD         a_b_c_d
     * abcD         abc_d
     * </code>
     *
     * @param name 驼峰单词
     * @return 下划线单词
     */
    private static String underline(String name) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < name.length(); ++i) {
            char ch = name.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                char cause = (char) (ch + 32);
                if (i > 0) {
                    buf.append('_');
                }
                buf.append(cause);
            } else {
                buf.append(ch);
            }
        }
        return buf.toString();
    }

    /**
     * 下划线转驼峰
     *
     * <code>
     * a_b_c____d      aBCD
     * abc_d           abcD
     * </code>
     *
     * @param name 下划线单词
     * @return 驼峰单词
     */
    private static String camelLine(String name) {
        StringBuilder buf = new StringBuilder();
        boolean caseLine = false;
        for (int i = 0; i < name.length(); ++i) {
            char ch = name.charAt(i);
            if (ch == '_') {
                caseLine = true;
                continue;
            }
            if (caseLine) {
                if (ch >= 'A' && ch <= 'Z') {
                    buf.append(ch);
                } else {
                    buf.append((char) (ch - 32));
                }
                caseLine = false;
            } else {
                buf.append(ch);
            }
        }
        return buf.toString();
    }
}
