package com.github.wz2cool.dynamic.mybatis.mapper.provider.factory;

import org.apache.ibatis.builder.annotation.ProviderContext;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangjin
 */
public class ProviderFactory {
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
        providerTable.setKey(key);
        cache.put(key, providerTable);
        return providerTable;
    }

    private static ProviderTable createProviderTable(ProviderContext providerContext) {
        final Class<?> mapperType = providerContext.getMapperType();
        final Method mapperMethod = providerContext.getMapperMethod();
        final String databaseId = providerContext.getDatabaseId();
        final Class<?> entityClass = (Class<?>) ((ParameterizedType) mapperType.getGenericInterfaces()[0]).getActualTypeArguments()[0];
        return ProviderTableHelper.getProviderTable(entityClass);
    }

}
