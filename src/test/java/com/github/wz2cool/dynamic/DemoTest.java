package com.github.wz2cool.dynamic;

import com.github.pagehelper.PageHelper;
import com.github.wz2cool.dynamic.mybatis.db.mapper.ProductDao;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.table.Product;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TestApplication.class)
public class DemoTest {

    @Resource
    private ProductDao productDao;

    @Test
    public void testSelectFields() {
        DynamicQuery<Product> dynamicQuery = DynamicQuery.createQuery(Product.class)
                .selectProperty(Product::getProductName)
                .selectProperty(Product::getPrice);

        List<Product> products = PageHelper.startPage(0, 3, false)
                .doSelectPage(() -> productDao.selectByDynamicQuery(dynamicQuery));

        for (Product p : products) {
            // categoryID ignore to select
            assertEquals(null, p.getCategoryID());
            assertEquals(true, StringUtils.isNotBlank(p.getProductName()));
        }
    }

    @Test
    public void testLinkOperation() {
        DynamicQuery<Product> dynamicQuery = DynamicQuery.createQuery(Product.class)
                .selectProperty(Product::getProductID)
                .selectProperty(Product::getProductName)
                .selectProperty(Product::getPrice)
                .ignoreProperty(Product::getProductID) // set will not effect bse we already set selectProperty
                .addFilterDescriptor(Product::getPrice, FilterOperator.GREATER_THAN, 16)
                .addSortDescriptor(Product::getPrice, SortDirection.DESC)
                .addSortDescriptor(Product::getProductID, SortDirection.DESC);

        List<Product> products = PageHelper.startPage(0, 100, false)
                .doSelectPage(() -> productDao.selectByDynamicQuery(dynamicQuery));

        for (Product p : products) {
            // categoryID ignore to select
            assertEquals(null, p.getCategoryID());
            assertEquals(true, StringUtils.isNotBlank(p.getProductName()));
            // price > 16
            assertEquals(1, p.getPrice().compareTo(BigDecimal.valueOf(16)));
        }
    }

    @Test
    public void testIgnoreFieldOperation() {
        DynamicQuery<Product> dynamicQuery = DynamicQuery.createQuery(Product.class)
                .ignoreProperty(Product::getProductID)
                .addFilterDescriptor(Product::getPrice, FilterOperator.GREATER_THAN, 16)
                .addSortDescriptor(Product::getPrice, SortDirection.DESC)
                .addSortDescriptor(Product::getProductID, SortDirection.DESC);

        List<Product> products = PageHelper.startPage(0, 100, false)
                .doSelectPage(() -> productDao.selectByDynamicQuery(dynamicQuery));

        for (Product p : products) {
            // categoryID ignore to select
            assertEquals(null, p.getCategoryID());
            assertEquals(true, StringUtils.isNotBlank(p.getProductName()));
        }
    }
}
