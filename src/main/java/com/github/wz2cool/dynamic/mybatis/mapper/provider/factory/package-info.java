/**
 * 主要解析{@link org.apache.ibatis.builder.annotation.ProviderContext}
 * 根据Class可以获取表结构信息 {@link com.github.wz2cool.dynamic.mybatis.mapper.provider.factory.ProviderTableHelper#getProviderTable(java.lang.Class)}
 * 可以根据ProviderTable获取动态sql {@link com.github.wz2cool.dynamic.mybatis.mapper.provider.factory.DynamicCreateSqlFactory#getSqlFactory(com.github.wz2cool.dynamic.mybatis.mapper.provider.factory.ProviderTable)}
 *
 * @author wangjin
 */
@ParametersAreNonnullByDefault
package com.github.wz2cool.dynamic.mybatis.mapper.provider.factory;

import javax.annotation.ParametersAreNonnullByDefault;