package com.github.wz2cool.dynamic.mybatis.mapper.provider.factory;

import com.github.wz2cool.dynamic.mybatis.QueryHelper;
import com.github.wz2cool.dynamic.mybatis.View;
import org.apache.ibatis.builder.annotation.ProviderContext;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author wangjin
 */
public class ProviderFactory {
    private static final QueryHelper QUERY_HELPER = new QueryHelper();
    private static final Map<String, ProviderTable> cache = new ConcurrentHashMap<>(256);

    public static String genKey(ProviderContext providerContext) {
        return (providerContext.getMapperType().getName().intern() + "#" + providerContext.getMapperMethod().getName()).intern();
    }

    public static ProviderTable create(ProviderContext providerContext) {
        final String key = genKey(providerContext);
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        final ProviderTable providerTable = ProviderFactory.createProviderTable(providerContext);
        providerTable.key = key;
        cache.put(key, providerTable);
        return providerTable;
    }


    private static ProviderTable createProviderTable(ProviderContext providerContext) {
        final ProviderTable providerTable = new ProviderTable();
        final Class<?> mapperType = providerContext.getMapperType();
        final Method mapperMethod = providerContext.getMapperMethod();
        final String databaseId = providerContext.getDatabaseId();

        final Class<?> entityClass = (Class<?>) ((ParameterizedTypeImpl) mapperType.getGenericInterfaces()[0]).getActualTypeArguments()[0];

        final String tableName = Optional.ofNullable(entityClass.getAnnotation(Table.class)).map(Table::name)
                .orElse(entityClass.getSimpleName());

        //取字段
        final Field[] declaredFields = entityClass.getDeclaredFields();

        List<ProviderColumn> columnList = new ArrayList<>(declaredFields.length);
        List<ProviderColumn> transientColumnList = new ArrayList<>(declaredFields.length);

        ProviderColumn pk = null;
        for (Field declaredField : declaredFields) {
            final ProviderColumn col = new ProviderColumn();
            col.javaColumn = declaredField.getName();
            col.dbColumn = Optional.ofNullable(declaredField.getAnnotation(Column.class))
                    .map(Column::name)
                    .orElse(ProviderFactory.underline(declaredField.getName()));
            col.columnType = declaredField.getType();
            if (declaredField.getAnnotation(Id.class) != null) {
                col.isPrimaryKey = true;
                pk = col;
            }
            if (declaredField.getAnnotation(Transient.class) == null) {
                columnList.add(col);
            } else {
                transientColumnList.add(col);
            }
        }
        //VIEW 注解的
        providerTable.tableName = Optional.ofNullable(entityClass.getAnnotation(View.class)).map(View::value).orElse(tableName);
        providerTable.entityClass = entityClass;
        providerTable.fields = declaredFields;
        providerTable.transientColumns = transientColumnList.toArray(new ProviderColumn[0]);
        providerTable.columns = columnList.toArray(new ProviderColumn[0]);
        providerTable.primaryKey = pk;
        return providerTable;
    }

    public static void main(String[] args) {
        System.out.println(underline("aaaa"));
        System.out.println(underline("userInfo"));
        System.out.println(underline("userIIIfino"));
    }

    /**
     * 驼峰转下划线
     *
     * @param name 驼峰单词
     * @return 下划线单词
     */
    private static String underline(String name) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < name.length(); ++i) {
            char ch = name.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                char ch_ucase = (char) (ch + 32);
                if (i > 0) {
                    buf.append('_');
                }
                buf.append(ch_ucase);
            } else {
                buf.append(ch);
            }
        }
        return buf.toString();
    }
}
