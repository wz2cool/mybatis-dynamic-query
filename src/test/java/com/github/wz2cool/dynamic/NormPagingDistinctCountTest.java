package com.github.wz2cool.dynamic;

import com.github.wz2cool.dynamic.model.NormPagingResult;
import com.github.wz2cool.dynamic.mybatis.db.mapper.ProductDao;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.table.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import static com.github.wz2cool.dynamic.builder.DynamicQueryBuilderHelper.*;
import static org.junit.Assert.*;

/**
 * NormPaging distinct calcTotal tests for both overloads:
 * {@link NormPagingQuery} and {@link NormPagingQueryWrapper}.
 * <p>
 * Multi-column distinct count total must not throw and must equal
 * the number of distinct (category_id, price) combos (NULL combo counts as one row).
 * <p>
 * Data isolation: tests share one in-memory H2 without transactions,
 * so rows are inserted with product_id &gt;= 20000, every query is scoped with
 * product_id &gt;= 20000 and rows are deleted in a finally block.
 *
 * @author Frank
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TestApplication.class)
public class NormPagingDistinctCountTest {

    private static final long MIN_TEST_PRODUCT_ID = 20000L;
    private static final String TEST_PRODUCT_NAME = "norm-paging-distinct-test";

    @Autowired
    private ProductDao productDao;
    @Autowired
    private DataSource dataSource;

    @Test
    public void normPagingQueryOverload_distinctCalcTotal() throws SQLException {
        try {
            this.insertDistinctTestData();
            // distinct (category_id, price) combos: (1, 10.0) and (2, null) = 2
            NormPagingQuery<Product> pq = NormPagingQuery.createQuery(Product.class, 1, 10);
            pq.select(Product::getCategoryId, Product::getPrice);
            pq.setDistinct(true);
            pq.and(Product::getProductId, greaterThanOrEqual(MIN_TEST_PRODUCT_ID));

            NormPagingResult<Product> result = productDao.selectByNormalPaging(pq);
            assertEquals(2, result.getTotal());
            assertEquals(1, result.getPages());
            List<Product> productList = result.getList();
            assertNotNull(productList);
            assertEquals(2, productList.size());

            // pages math with a smaller page size: 2 rows / 1 per page = 2 pages
            NormPagingQuery<Product> pqPageSize1 = NormPagingQuery.createQuery(Product.class, 1, 1);
            pqPageSize1.select(Product::getCategoryId, Product::getPrice);
            pqPageSize1.setDistinct(true);
            pqPageSize1.and(Product::getProductId, greaterThanOrEqual(MIN_TEST_PRODUCT_ID));

            NormPagingResult<Product> resultPageSize1 = productDao.selectByNormalPaging(pqPageSize1);
            assertEquals(2, resultPageSize1.getTotal());
            assertEquals(2, resultPageSize1.getPages());
            assertEquals(1, resultPageSize1.getList().size());
        } finally {
            this.deleteTestProducts();
        }
    }

    @Test
    public void normPagingQueryWrapperOverload_distinctCalcTotal() throws SQLException {
        try {
            this.insertDistinctTestData();
            // distinct (category_id, price) combos: (1, 10.0) and (2, null) = 2
            DynamicQuery<Product> q = DynamicQuery.createQuery(Product.class)
                    .select(Product::getCategoryId, Product::getPrice)
                    .and(Product::getProductId, greaterThanOrEqual(MIN_TEST_PRODUCT_ID));
            q.setDistinct(true);

            NormPagingQueryWrapper<Product, DynamicQuery<Product>> wrapper =
                    NormPagingQueryWrapper.create(q, 1, 10);
            // 3-arg create() already defaults calcTotal to true, keep it explicit here
            wrapper.setCalcTotal(true);

            NormPagingResult<Product> result = productDao.selectByNormalPaging(wrapper);
            assertEquals(2, result.getTotal());
            assertEquals(1, result.getPages());
            assertEquals(2, result.getList().size());
        } finally {
            this.deleteTestProducts();
        }
    }

    @Test
    public void normPaging_distinct_pageNumBeyondPagesAutoBacks() throws SQLException {
        // 边界：calcTotal + autoBackIfEmpty 下请求页越界（页码 3 > 总页数 1），
        // distinct 查询逐页回退至第 1 页，total 仍由多列 distinct count 正确计算
        try {
            this.insertDistinctTestData();
            NormPagingQuery<Product> pq = NormPagingQuery.createQuery(Product.class, 3, 10, true, true);
            pq.select(Product::getCategoryId, Product::getPrice);
            pq.setDistinct(true);
            pq.and(Product::getProductId, greaterThanOrEqual(MIN_TEST_PRODUCT_ID));

            NormPagingResult<Product> result = productDao.selectByNormalPaging(pq);
            assertEquals(2, result.getTotal());
            assertEquals(1, result.getPageNum());
            assertEquals(2, result.getList().size());
        } finally {
            this.deleteTestProducts();
        }
    }

    /**
     * Insert test data with explicit product_id &gt;= 20000.
     * <p>
     * {@link Product#getProductId()} is mapped with insertable = false (the column is
     * auto_increment), so {@link ProductDao#insert(Product)} cannot control product_id;
     * rows are inserted through plain JDBC instead. Queries and cleanup below still go
     * through {@link ProductDao}.
     */
    private void insertDistinctTestData() throws SQLException {
        this.deleteTestProducts();
        try (Connection connection = this.dataSource.getConnection()) {
            // SQL Server 的 IDENTITY 列默认拒绝显式主键插入（H2/MySQL/PG 允许），
            // 仅在该方言下会话内临时开启，使同一套夹具可在四库运行
            if (connection.getMetaData().getDatabaseProductName().contains("Microsoft SQL Server")) {
                try (java.sql.Statement identityStatement = connection.createStatement()) {
                    identityStatement.execute("SET IDENTITY_INSERT product ON");
                }
            }
            this.insertProduct(connection, 20001L, 1, BigDecimal.valueOf(10.0));
            this.insertProduct(connection, 20002L, 1, BigDecimal.valueOf(10.0));
            this.insertProduct(connection, 20003L, 2, null);
        }
    }

    private void insertProduct(Connection connection, long productId, int categoryId, BigDecimal price)
            throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO product (product_id, category_id, product_name, price) VALUES (?, ?, ?, ?)")) {
            statement.setLong(1, productId);
            statement.setInt(2, categoryId);
            statement.setString(3, TEST_PRODUCT_NAME);
            if (price == null) {
                statement.setNull(4, Types.DECIMAL);
            } else {
                statement.setBigDecimal(4, price);
            }
            assertEquals(1, statement.executeUpdate());
        }
    }

    private void deleteTestProducts() {
        this.productDao.deleteByDynamicQuery(DynamicQuery.createQuery(Product.class)
                .and(Product::getProductId, greaterThanOrEqual(MIN_TEST_PRODUCT_ID)));
    }
}
