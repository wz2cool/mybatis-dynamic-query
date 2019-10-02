package com.github.wz2cool.dynamic;

import com.github.pagehelper.PageHelper;
import com.github.wz2cool.dynamic.model.Bug;
import com.github.wz2cool.dynamic.mybatis.db.mapper.BugDao;
import com.github.wz2cool.dynamic.mybatis.db.mapper.NorthwindDao;
import com.github.wz2cool.dynamic.mybatis.db.mapper.ProductDao;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.table.Product;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.view.ProductView;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.github.wz2cool.dynamic.builder.DynamicQueryBuilderHelper.*;
import static com.github.wz2cool.dynamic.builder.DynamicQueryBuilderHelper.lessThan;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TestApplication.class)
public class DemoTest {

    @Resource
    private ProductDao productDao;
    @Resource
    private NorthwindDao northwindDao;
    @Resource
    private BugDao bugDao;

    @Test
    public void testSelectFields() {
        DynamicQuery<Product> dynamicQuery = DynamicQuery.createQuery(Product.class)
                .select(Product::getProductID, Product::getProductName, Product::getPrice);
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
                .select(Product::getProductID, Product::getProductName, Product::getPrice)
                .ignore(Product::getProductID)
                .and(Product::getPrice, greaterThan(BigDecimal.valueOf(16)))
                .sort(Product::getPrice, desc())
                .sort(Product::getProductID, desc());
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
                .ignore(Product::getProductID)
                .and(Product::getPrice, greaterThan(BigDecimal.valueOf(16)))
                .sort(Product::getPrice, desc())
                .sort(Product::getProductID, desc());
        List<Product> products = PageHelper.startPage(0, 100, false)
                .doSelectPage(() -> productDao.selectByDynamicQuery(dynamicQuery));

        for (Product p : products) {
            // categoryID ignore to select
            assertEquals(null, p.getCategoryID());
            assertEquals(true, StringUtils.isNotBlank(p.getProductName()));
        }
    }

    @Test
    public void testSelectByView() {
        DynamicQuery<ProductView> dynamicQuery = DynamicQuery.createQuery(ProductView.class)
                .ignore(ProductView::getCategoryID)
                .and(ProductView::getPrice, greaterThan(BigDecimal.valueOf(16)))
                .sort(ProductView::getPrice, desc())
                .sort(ProductView::getProductID, desc());
        Map<String, Object> queryParamMap = dynamicQuery.toQueryParamMap();

        List<ProductView> productViews = PageHelper.startPage(0, 2, false)
                .doSelectPage(() -> northwindDao.getProductViewsByDynamic2(queryParamMap));

        for (ProductView p : productViews) {
            // categoryID ignore to select
            assertEquals(null, p.getCategoryID());
            assertEquals(true, StringUtils.isNotBlank(p.getProductName()));
            assertEquals(true, StringUtils.isNotBlank(p.getCategoryName()));
        }
    }

    @Test
    public void testSelectByViewWithoutFilters() {
        DynamicQuery<ProductView> dynamicQuery = DynamicQuery.createQuery(ProductView.class)
                .ignore(ProductView::getCategoryID)
                .sort(ProductView::getPrice, desc())
                .sort(ProductView::getProductID, desc());
        Map<String, Object> queryParamMap = dynamicQuery.toQueryParamMap();

        List<ProductView> productViews = PageHelper.startPage(0, 2, false)
                .doSelectPage(() -> northwindDao.getProductViewsByDynamic2(queryParamMap));

        for (ProductView p : productViews) {
            // categoryID ignore to select
            assertEquals(null, p.getCategoryID());
            assertEquals(true, StringUtils.isNotBlank(p.getProductName()));
            assertEquals(true, StringUtils.isNotBlank(p.getCategoryName()));
        }
    }

    @Test
    public void testSelectByViewWithoutSorts() {
        DynamicQuery<ProductView> dynamicQuery = DynamicQuery.createQuery(ProductView.class)
                .ignore(ProductView::getCategoryID)
                .and(ProductView::getPrice, in(BigDecimal.valueOf(16), BigDecimal.valueOf(18)));
        Map<String, Object> queryParamMap = dynamicQuery.toQueryParamMap();

        List<ProductView> productViews = PageHelper.startPage(0, 2, false)
                .doSelectPage(() -> northwindDao.getProductViewsByDynamic2(queryParamMap));

        for (ProductView p : productViews) {
            // categoryID ignore to select
            assertEquals(null, p.getCategoryID());
            assertEquals(true, StringUtils.isNotBlank(p.getProductName()));
            assertEquals(true, StringUtils.isNotBlank(p.getCategoryName()));
        }
    }

    @Test
    public void testGetBug() {
        DynamicQuery<Bug> query = DynamicQuery.createQuery(Bug.class)
                .and(Bug::getId, notIn(1));

        List<Bug> bugs = bugDao.selectByDynamicQuery(query);
        for (Bug bug : bugs) {
            assertEquals(true, StringUtils.isNotBlank(bug.getAssignTo()));
        }
    }
}
