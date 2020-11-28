package com.github.wz2cool.dynamic;

import com.github.pagehelper.PageHelper;
import com.github.wz2cool.dynamic.model.Bug;
import com.github.wz2cool.dynamic.mybatis.db.mapper.BugDao;
import com.github.wz2cool.dynamic.mybatis.db.mapper.CategoryGroupCountMapper;
import com.github.wz2cool.dynamic.mybatis.db.mapper.NorthwindDao;
import com.github.wz2cool.dynamic.mybatis.db.mapper.ProductDao;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.group.CategoryGroupCount;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.table.Product;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.view.ProductView;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.github.wz2cool.dynamic.builder.DynamicQueryBuilderHelper.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TestApplication.class)
public class DemoTest {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private NorthwindDao northwindDao;
    @Autowired
    private BugDao bugDao;
    @Resource
    private CategoryGroupCountMapper categoryGroupCountMapper;

    @Test
    public void testGroupBy() {
        GroupedQuery<Product, CategoryGroupCount> groupedQuery = GroupByQuery.createQuery(Product.class, CategoryGroupCount.class)
                .select(CategoryGroupCount::getCategoryId, CategoryGroupCount::getCount)
                // 这里是Where 对数据筛选
                .and(Product::getProductId, greaterThan(0L))
                .groupBy(Product::getCategoryId)
                // 这里是having 对分组筛选
                .and(CategoryGroupCount::getCount, greaterThan(1))
                .orderByNull();

        List<CategoryGroupCount> categoryGroupCountList = categoryGroupCountMapper.selectByGroupedQuery(groupedQuery);
        for (CategoryGroupCount categoryGroupCount : categoryGroupCountList) {
            assertTrue(categoryGroupCount.getCount() > 1);
        }
    }

    @Test
    public void testSelectMax() {
        DynamicQuery<Product> query1 = DynamicQuery.createQuery(Product.class);
        Optional<BigDecimal> maxOptional = productDao.selectMaxByDynamicQuery(Product::getPrice, query1);
        DynamicQuery<Product> query2 = DynamicQuery.createQuery(Product.class)
                .orderBy(Product::getPrice, desc());
        Optional<Product> maxItem = productDao.selectFirstByDynamicQuery(query2);
        assertEquals(maxItem.get().getPrice(), maxOptional.get());
    }

    @Test
    public void testSelectMin() {
        DynamicQuery<Product> query1 = DynamicQuery.createQuery(Product.class);
        Optional<BigDecimal> minOptional = productDao.selectMinByDynamicQuery(Product::getPrice, query1);
        DynamicQuery<Product> query2 = DynamicQuery.createQuery(Product.class)
                .orderBy(Product::getPrice, asc());
        Optional<Product> minItem = productDao.selectFirstByDynamicQuery(query2);
        assertEquals(minItem.get().getPrice(), minOptional.get());
    }

    @Test
    public void testSelectSum() {
        DynamicQuery<Product> query1 = DynamicQuery.createQuery(Product.class);
        Optional<BigDecimal> sumOptional = productDao.selectSumByDynamicQuery(Product::getPrice, query1);
        BigDecimal sum = sumOptional.get();
        List<Product> productList = productDao.selectAll();
        BigDecimal expectedValue = new BigDecimal(productList.stream().mapToDouble(x -> x.getPrice().doubleValue()).sum());
        assertEquals(0, expectedValue.compareTo(sum));
    }

    @Test
    public void testSelectAvg() {
        DynamicQuery<Product> query1 = DynamicQuery.createQuery(Product.class);
        Optional<BigDecimal> avgOptional = productDao.selectAvgByDynamicQuery(Product::getPrice, query1);
        BigDecimal sum = avgOptional.get();
        List<Product> productList = productDao.selectAll();
        BigDecimal expectedValue = new BigDecimal(productList.stream().mapToDouble(x -> x.getPrice().doubleValue()).average().getAsDouble());
        assertEquals(0, expectedValue.compareTo(sum));
    }


    @Test
    public void testSelectFields() {
        DynamicQuery<Product> dynamicQuery = DynamicQuery.createQuery(Product.class)
                .select(Product::getProductId, Product::getProductName, Product::getPrice);
        List<Product> products = PageHelper.startPage(0, 3, false)
                .doSelectPage(() -> productDao.selectByDynamicQuery(dynamicQuery));

        for (Product p : products) {
            // categoryID ignore to select
            assertEquals(null, p.getCategoryId());
            assertEquals(true, StringUtils.isNotBlank(p.getProductName()));
        }
    }

    @Test
    public void testLinkOperation() {
        DynamicQuery<Product> dynamicQuery = DynamicQuery.createQuery(Product.class)
                .select(Product::getProductId, Product::getProductName, Product::getPrice)
                .ignore(Product::getProductId)
                .and(Product::getPrice, greaterThan(BigDecimal.valueOf(16)))
                .orderBy(Product::getPrice, desc())
                .orderBy(Product::getProductId, desc());
        List<Product> products = PageHelper.startPage(0, 100, false)
                .doSelectPage(() -> productDao.selectByDynamicQuery(dynamicQuery));

        for (Product p : products) {
            // categoryID ignore to select
            assertEquals(null, p.getCategoryId());
            assertEquals(true, StringUtils.isNotBlank(p.getProductName()));
            // price > 16
            assertEquals(1, p.getPrice().compareTo(BigDecimal.valueOf(16)));
        }
    }

    @Test
    public void testIgnoreFieldOperation() {
        DynamicQuery<Product> dynamicQuery = DynamicQuery.createQuery(Product.class)
                .ignore(Product::getProductId)
                .and(Product::getPrice, greaterThan(BigDecimal.valueOf(16)))
                .orderBy(Product::getPrice, desc())
                .orderBy(Product::getProductId, desc());
        List<Product> products = PageHelper.startPage(0, 100, false)
                .doSelectPage(() -> productDao.selectByDynamicQuery(dynamicQuery));

        for (Product p : products) {
            // categoryID ignore to select
            assertEquals(true, StringUtils.isNotBlank(p.getProductName()));
        }
    }

    @Test
    public void testSelectByView() {
        DynamicQuery<ProductView> dynamicQuery = DynamicQuery.createQuery(ProductView.class)
                .ignore(ProductView::getCategoryID)
                .orderBy(ProductView::getPrice, desc())
                .orderBy(ProductView::getProductID, desc());
        Map<String, Object> queryParamMap = dynamicQuery.toQueryParamMap(false);

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
                .orderBy(ProductView::getPrice, desc())
                .orderBy(ProductView::getProductID, desc());
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
                .ignore(ProductView::getCategoryID);
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
