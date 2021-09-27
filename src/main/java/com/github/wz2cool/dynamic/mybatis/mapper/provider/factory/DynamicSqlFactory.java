package com.github.wz2cool.dynamic.mybatis.mapper.provider.factory;

import com.github.wz2cool.dynamic.helper.CommonsHelper;

/**
 * 通过本类可以获取动态sql
 */
public class DynamicSqlFactory {

    private DynamicSqlFactory() {

    }

    public static SqlFactory getSqlSqlFactory(ProviderTable providerTable) {
        return SqlFactory.create(providerTable);
    }


    public static class SqlFactory {
        private final ProviderTable providerTable;

        private SqlFactory(ProviderTable providerTable) {
            this.providerTable = providerTable;
        }

        private static SqlFactory create(ProviderTable providerTable) {
            return new SqlFactory(providerTable);
        }


        /**
         * @param deleteByPrimaryKey 如果是deleteByPrimaryKey, 则说明只通过主键删除
         * @return
         */
        public String getDeleteSql(boolean deleteByPrimaryKey) {
            if (deleteByPrimaryKey) {
                if (providerTable.getPrimaryKey() == null) {
                    throw new IllegalArgumentException(CommonsHelper.format("该类[%s]没有发现主键", providerTable.getTableName()));
                }
            }
            return ";";
        }
    }
}
