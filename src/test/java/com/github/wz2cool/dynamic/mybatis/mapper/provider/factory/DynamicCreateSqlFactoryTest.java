package com.github.wz2cool.dynamic.mybatis.mapper.provider.factory;

import com.github.wz2cool.dynamic.mybatis.db.model.entity.table.User;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.view.ProductView;
import org.junit.Before;
import org.junit.Test;

public class DynamicCreateSqlFactoryTest {

    DynamicCreateSqlFactory sqlFactory;

    @Before
    public void getSqlFactory() {
        sqlFactory = DynamicCreateSqlFactory.getSqlFactory(ProviderTableHelper.getProviderTable(User.class));
    }

    @Test
    public void getInsertSql() {
        System.out.println(sqlFactory.getInsertSql(true));
        System.out.println(sqlFactory.getInsertSql(false));
    }

    @Test
    public void getInsertSql2222() {
        DynamicCreateSqlFactory sqlFactory = DynamicCreateSqlFactory.getSqlFactory(ProviderTableHelper.getProviderTable(User.class));
        DynamicCreateSqlFactory sqlFactory2 = DynamicCreateSqlFactory.getSqlFactory(ProviderTableHelper.getProviderTable(ProductView.class));
    }

    @Test
    public void getDeleteSql() {
        System.out.println(sqlFactory.getDeleteSql(true));
        System.out.println(sqlFactory.getDeleteSql(false));
    }

    @Test
    public void getUpdateByPrimaryKeySql() {
        System.out.println(sqlFactory.getUpdateByPrimaryKeySql(true));
        System.out.println(sqlFactory.getUpdateByPrimaryKeySql(false));
    }

    @Test
    public void getDynamicDelete() {
        System.out.println(sqlFactory.getDynamicDelete());
    }

    @Test
    public void getDynamicQuery() {
        System.out.println(sqlFactory.getDynamicQuery());
    }

    @Test
    public void getDynamicCount() {
        System.out.println(sqlFactory.getDynamicCount());
    }

}