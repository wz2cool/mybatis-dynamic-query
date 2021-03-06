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
import com.github.wz2cool.dynamic.mybatis.mapper.batch.MapperBatchAction;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

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
    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @Test
    public void testBatchInsert() {
        MapperBatchAction<BugDao> insertBatchAction = MapperBatchAction.create(BugDao.class, this.sqlSessionFactory, 3);
        for (int i = 0; i < 10; i++) {
            Bug newBug = new Bug();
            newBug.setId(10000 + i);
            newBug.setAssignTo("frank");
            newBug.setTitle("title");
            insertBatchAction.addAction((mapper) -> mapper.insertSelective(newBug));
        }
        final List<BatchResult> batchResults = insertBatchAction.doBatchActionWithResults();
        int effectRows = batchResults.stream().mapToInt(x -> Arrays.stream(x.getUpdateCounts()).sum()).sum();
        assertEquals(10, effectRows);
    }

    @Test
    public void testBatchAction() {
        MapperBatchAction<BugDao> insertBatchAction = MapperBatchAction.create(BugDao.class, this.sqlSessionFactory, 5);
        for (int i = 0; i < 10; i++) {
            Bug newBug = new Bug();
            newBug.setId(10000 + i);
            newBug.setAssignTo("frank");
            newBug.setTitle("title");
            insertBatchAction.addAction((mapper) -> mapper.insertSelective(newBug));
        }
        final List<BatchResult> batchResults = insertBatchAction.doBatchActionWithResults();
        MapperBatchAction<BugDao> updateBatchAction = MapperBatchAction.create(BugDao.class, this.sqlSessionFactory, 3);
        for (int i = 0; i < 10; i++) {
            UpdateQuery<Bug> updateQuery = UpdateQuery.createQuery(Bug.class)
                    .set(Bug::getAssignTo, "Marry")
                    .set(i % 2 == 0, Bug::getTitle, "title2")
                    .and(Bug::getId, isEqual(10000 + i));
            updateBatchAction.addAction((mapper) -> mapper.updateByUpdateQuery(updateQuery));
        }
        int effectRows = updateBatchAction.doBatchActions();
        assertTrue(effectRows > 0);
        DynamicQuery<Bug> dynamicQuery = DynamicQuery.createQuery(Bug.class)
                .and(Bug::getId, greaterThanOrEqual(10000));
        List<Bug> bugs = bugDao.selectByDynamicQuery(dynamicQuery);

        // delete all after verify
        MapperBatchAction<BugDao> deleteBatchAction = MapperBatchAction.create(BugDao.class, this.sqlSessionFactory, 3);
        for (int i = 0; i < bugs.size(); i++) {
            final int id = 10000 + i;
            Bug bug = bugs.get(i);
            assertEquals(10000 + i, (int) bug.getId());
            assertEquals("Marry", bug.getAssignTo());
            if (i % 2 == 0) {
                assertEquals("title2", bug.getTitle());
            } else {
                assertEquals("title", bug.getTitle());
            }
            DynamicQuery<Bug> deleteQuery = DynamicQuery.createQuery(Bug.class)
                    .and(Bug::getId, isEqual(id));
            deleteBatchAction.addAction((mapper) -> mapper.deleteByDynamicQuery(deleteQuery));
        }
        deleteBatchAction.doBatchActions();

        bugs = bugDao.selectByDynamicQuery(dynamicQuery);
        assertEquals(0, bugs.size());
    }

    @Test
    public void testMinGroupBy() {
        GroupedQuery<Product, Long> groupedQuery = GroupByQuery.createQuery(Product.class, Long.class)
                // 这里是Where 对数据筛选
                .and(Product::getProductId, greaterThan(0L))
                .groupBy(Product::getCategoryId);
        List<Long> longs = categoryGroupCountMapper.selectMinByGroupedQuery(Product::getProductId, groupedQuery);
        for (Long aLong : longs) {
            assertTrue(aLong > 0);
        }
    }

    @Test
    public void testMaxGroupBy() {
        GroupedQuery<Product, Long> groupedQuery = GroupByQuery.createQuery(Product.class, Long.class)
                // 这里是Where 对数据筛选
                .and(Product::getProductId, greaterThan(0L))
                .groupBy(Product::getCategoryId);
        List<Long> longs = categoryGroupCountMapper.selectMaxByGroupedQuery(Product::getProductId, groupedQuery, new RowBounds(0, 2));
        for (Long aLong : longs) {
            assertTrue(aLong > 0);
        }
    }

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

        categoryGroupCountMapper.selectByGroupedQuery(groupedQuery);

        List<CategoryGroupCount> categoryGroupCountList =
                categoryGroupCountMapper.selectRowBoundsByGroupedQuery(groupedQuery, new RowBounds(0, 10));
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
