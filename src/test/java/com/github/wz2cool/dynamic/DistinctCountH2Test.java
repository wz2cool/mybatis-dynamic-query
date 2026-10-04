package com.github.wz2cool.dynamic;

import com.github.wz2cool.dynamic.mybatis.db.mapper.ProductDao;
import com.github.wz2cool.dynamic.mybatis.db.mapper.ProductViewQueryDao;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.table.Product;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.view.ProductView;
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
import static org.junit.Assert.assertEquals;

/**
 * H2 数值用例：多列 distinct count（openspec fix-multi-column-distinct-count 3.4 / 3.6）。
 * <p>
 * 数据隔离说明：测试无事务且共享同一个内存 H2，本用例只插入 product_id &gt;= 10000 的数据，
 * 所有查询与清理都按同一范围过滤，预置数据与其它用例不受影响。
 * 插入走 JDBC 显式主键：{@link Product#getProductId()} 在实体上标注了 {@code insertable = false}
 * （product_id 为自增主键），mapper 自动插入无法控制主键；且 H2 2.0.206 对显式主键插入不推进
 * 自增计数，预置数据占用了 id 1-5，自动插入会主键冲突。
 *
 * @author Frank
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TestApplication.class)
public class DistinctCountH2Test {

    private static final long MIN_TEST_PRODUCT_ID = 10000L;
    private static final String TEST_PRODUCT_NAME = "mdc-distinct-count-test";

    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductViewQueryDao productViewQueryDao;
    @Autowired
    private DataSource dataSource;

    @Test
    public void multiColumnDistinctCount_matchesDistinctListRowCount() throws SQLException {
        try {
            this.insertFixtureProducts();
            DynamicQuery<Product> query = DynamicQuery.createQuery(Product.class)
                    .select(Product::getCategoryId, Product::getPrice)
                    .and(Product::getProductId, greaterThanOrEqual(MIN_TEST_PRODUCT_ID));
            query.setDistinct(true);

            List<Product> list = productDao.selectByDynamicQuery(query);
            int count = productDao.selectCountByDynamicQuery(query);

            // distinct combos: (1, 10.0), (1, NULL), (2, NULL), (2, 20.0) - NULL combos collapse to one row each
            assertEquals(4, list.size());
            // spec invariant: distinct count == SELECT DISTINCT list row count
            assertEquals(list.size(), count);
        } finally {
            cleanupFixtureProducts();
        }
    }

    @Test
    public void fullColumnsDistinctCount_noExplicitSelect() throws SQLException {
        try {
            this.insertFixtureProducts();
            // no explicit select -> full column distinct
            DynamicQuery<Product> query = DynamicQuery.createQuery(Product.class)
                    .and(Product::getProductId, greaterThanOrEqual(MIN_TEST_PRODUCT_ID));
            query.setDistinct(true);

            List<Product> list = productDao.selectByDynamicQuery(query);
            int count = productDao.selectCountByDynamicQuery(query);

            // full columns include the unique product_id, so distinct rows == inserted rows (5)
            assertEquals(5, list.size());
            assertEquals(5, count);
        } finally {
            cleanupFixtureProducts();
        }
    }

    @Test
    public void singleColumnDistinct_excludesNull_unchanged() throws SQLException {
        try {
            this.insertFixtureProducts();
            DynamicQuery<Product> query = DynamicQuery.createQuery(Product.class)
                    .select(Product::getPrice)
                    .and(Product::getProductId, greaterThanOrEqual(MIN_TEST_PRODUCT_ID));
            query.setDistinct(true);

            // scalar COUNT(DISTINCT (price)) excludes NULL: only 10.0 and 20.0 remain
            int count = productDao.selectCountByDynamicQuery(query);
            assertEquals(2, count);
        } finally {
            cleanupFixtureProducts();
        }
    }

    @Test
    public void propertyMultiColumnDistinctCount_h2Numeric() throws SQLException {
        try {
            this.insertFixtureProducts();
            DynamicQuery<Product> query = DynamicQuery.createQuery(Product.class)
                    .and(Product::getProductId, greaterThanOrEqual(MIN_TEST_PRODUCT_ID));
            query.setDistinct(true);

            // property path with multi-column string -> wrapped sub-query count
            Integer count = productDao.selectCountPropertyByDynamicQuery("category_id, price", query);
            assertEquals(4, count.intValue());
        } finally {
            cleanupFixtureProducts();
        }
    }

    @Test
    public void viewEntityDuplicateColumns_noError() {
        // NO explicit select -> all view columns, including the two description columns
        // backed by product.description and category.description
        DynamicQuery<ProductView> viewQuery = DynamicQuery.createQuery(ProductView.class)
                .queryParam("spring_env", "");
        viewQuery.setDistinct(true);

        // must not throw H2 "Duplicate column name" - derived table uses AS-aliased columns
        List<ProductView> viewList = productViewQueryDao.selectByDynamicQuery(viewQuery);
        int viewCount = productViewQueryDao.selectCountByDynamicQuery(viewQuery);

        assertEquals(viewList.size(), viewCount);
    }

    @Test
    public void multiColumnDistinctCount_emptyScopeReturnsZero() {
        // 边界：作用域内没有任何行（也不需要插数）→ 列表 0 行、count 0
        DynamicQuery<Product> query = DynamicQuery.createQuery(Product.class)
                .select(Product::getCategoryId, Product::getPrice)
                .and(Product::getProductId, greaterThanOrEqual(900000L));
        query.setDistinct(true);

        List<Product> list = productDao.selectByDynamicQuery(query);
        int count = productDao.selectCountByDynamicQuery(query);

        assertEquals(0, list.size());
        assertEquals(0, count);
    }

    @Test
    public void multiColumnDistinctCount_oneRowAndAllNullCombosBoundaries() throws SQLException {
        // 边界夹具：30001/30002 都是 (cat1, NULL)——全 NULL 组合；30003 恰好一行 (cat2, 5.0)
        try {
            this.insertBoundaryProducts();
            // 边界 1：重复的全 NULL 组合在派生表中坍缩为一行 → count 1（NULL 组合计一行）
            DynamicQuery<Product> allNullCombos = DynamicQuery.createQuery(Product.class)
                    .select(Product::getCategoryId, Product::getPrice)
                    .and(Product::getProductId, greaterThanOrEqual(30001L))
                    .and(Product::getProductId, lessThanOrEqual(30002L));
            allNullCombos.setDistinct(true);
            assertEquals(1, productDao.selectCountByDynamicQuery(allNullCombos));
            assertEquals(1, productDao.selectByDynamicQuery(allNullCombos).size());

            // 边界 2：单列全 NULL——标量 distinct 全排除 → count 0（与多列的 1 形成对照）
            DynamicQuery<Product> priceOnly = DynamicQuery.createQuery(Product.class)
                    .select(Product::getPrice)
                    .and(Product::getProductId, greaterThanOrEqual(30001L))
                    .and(Product::getProductId, lessThanOrEqual(30002L));
            priceOnly.setDistinct(true);
            assertEquals(0, productDao.selectCountByDynamicQuery(priceOnly));

            // 边界 3：作用域内恰好一行 → count 1
            DynamicQuery<Product> singleRow = DynamicQuery.createQuery(Product.class)
                    .select(Product::getCategoryId, Product::getPrice)
                    .and(Product::getProductId, greaterThanOrEqual(30003L));
            singleRow.setDistinct(true);
            assertEquals(1, productDao.selectCountByDynamicQuery(singleRow));
        } finally {
            this.cleanupBoundaryProducts();
        }
    }

    @Test
    public void propertyPath_singleExpressionWithCommas_staysScalar() throws SQLException {
        // 顶层逗号解析修复后：单个表达式（逗号位于括号/引号内）不再被误判为多列，
        // 含逗号与不含逗号的 CASE 变体都走标量 distinct，NULL 排除语义与升级前一致 = 2。
        // （修复前含逗号变体会被包裹，NULL 组合计一行 = 3 —— 该披露限制已随本修复移除；
        //   单列内联形态也无需别名，SQL Server 的派生表命名要求不再适用于该场景。）
        try {
            this.insertFixtureProducts();
            DynamicQuery<Product> query = DynamicQuery.createQuery(Product.class)
                    .and(Product::getProductId, greaterThanOrEqual(MIN_TEST_PRODUCT_ID));
            query.setDistinct(true);

            Integer withCommas = productDao.selectCountPropertyByDynamicQuery(
                    "CASE WHEN price IS NULL THEN NULL ELSE CONCAT(category_id, ',') END", query);
            assertEquals(2, withCommas.intValue());

            Integer withoutCommas = productDao.selectCountPropertyByDynamicQuery(
                    "CASE WHEN price IS NULL THEN NULL ELSE category_id END", query);
            assertEquals(2, withoutCommas.intValue());
        } finally {
            this.cleanupFixtureProducts();
        }
    }

    private void insertFixtureProducts() throws SQLException {
        this.cleanupFixtureProducts();
        // (category, price) combos: (1, 10.0) twice, (1, NULL), (2, NULL), (2, 20.0)
        try (Connection connection = this.dataSource.getConnection()) {
            allowIdentityInsertIfSqlServer(connection);
            this.insertProduct(connection, 10001L, 1, new BigDecimal("10.0"));
            this.insertProduct(connection, 10002L, 1, new BigDecimal("10.0"));
            this.insertProduct(connection, 10003L, 1, null);
            this.insertProduct(connection, 10004L, 2, null);
            this.insertProduct(connection, 10005L, 2, new BigDecimal("20.0"));
        }
    }

    /**
     * SQL Server 的 IDENTITY 列默认拒绝显式主键插入（H2/MySQL/PG 允许），
     * 仅在该方言下会话内临时开启，使同一套夹具可在四库运行。
     */
    private void allowIdentityInsertIfSqlServer(Connection connection) throws SQLException {
        if (connection.getMetaData().getDatabaseProductName().contains("Microsoft SQL Server")) {
            try (java.sql.Statement statement = connection.createStatement()) {
                statement.execute("SET IDENTITY_INSERT product ON");
            }
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

    private void cleanupFixtureProducts() {
        productDao.deleteByDynamicQuery(DynamicQuery.createQuery(Product.class)
                .and(Product::getProductId, greaterThanOrEqual(MIN_TEST_PRODUCT_ID)));
    }

    /** 30000 段边界夹具：30001/30002 全 NULL 价格组合，30003 单行。 */
    private void insertBoundaryProducts() throws SQLException {
        this.cleanupBoundaryProducts();
        try (Connection connection = this.dataSource.getConnection()) {
            allowIdentityInsertIfSqlServer(connection);
            this.insertProduct(connection, 30001L, 1, null);
            this.insertProduct(connection, 30002L, 1, null);
            this.insertProduct(connection, 30003L, 2, new BigDecimal("5.0"));
        }
    }

    private void cleanupBoundaryProducts() {
        productDao.deleteByDynamicQuery(DynamicQuery.createQuery(Product.class)
                .and(Product::getProductId, greaterThanOrEqual(30000L))
                .and(Product::getProductId, lessThan(40000L)));
    }
}
