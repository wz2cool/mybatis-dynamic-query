package com.github.wz2cool.dynamic;

import com.github.wz2cool.dynamic.mybatis.MybatisQueryProvider;
import com.github.wz2cool.dynamic.mybatis.ParamExpression;
import com.github.wz2cool.dynamic.mybatis.db.mapper.NorthwindDao;
import com.github.wz2cool.dynamic.mybatis.db.mapper.UserDao;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.table.Category;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.table.Product;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.table.User;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.view.ProductView;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * Created by Frank on 2017/7/15.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TestApplication.class)
public class DbFilterTest {

    @Value("${spring.profiles.active}")
    private String active;

    @Autowired
    private NorthwindDao northwindDao;

    @Autowired
    private UserDao userDao;

    @Test
    public void testDbInit() throws Exception {
        List<Category> categories = northwindDao.getCategories();
        assertEquals(true, categories.size() > 0);

        List<Product> products = northwindDao.getProducts();
        assertEquals(true, products.size() > 0);
    }

    @Test
    public void testLessThan() throws Exception {
        FilterDescriptor idFilter =
                new FilterDescriptor(FilterCondition.AND, "productID", FilterOperator.LESS_THAN, 3);
        ParamExpression paramExpression = MybatisQueryProvider.getWhereExpression(ProductView.class, idFilter);
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.putAll(paramExpression.getParamMap());
        queryParams.put("whereExpression", paramExpression.getExpression());
        List<ProductView> productViews = northwindDao.getProductViewsByDynamic(queryParams);
        assertEquals(true, productViews.stream().allMatch(x -> x.getProductID() < 3));
    }

    @Test
    public void testLessThanOrEqual() throws Exception {
        FilterDescriptor idFilter =
                new FilterDescriptor(FilterCondition.AND, "productID", FilterOperator.LESS_THAN_OR_EQUAL, 3);
        ParamExpression paramExpression = MybatisQueryProvider.getWhereExpression(ProductView.class, idFilter);
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.putAll(paramExpression.getParamMap());
        queryParams.put("whereExpression", paramExpression.getExpression());
        List<ProductView> productViews = northwindDao.getProductViewsByDynamic(queryParams);
        assertEquals(true, productViews.stream().allMatch(x -> x.getProductID() <= 3));
    }

    @Test
    public void testEqual() throws Exception {
        FilterDescriptor idFilter =
                new FilterDescriptor(FilterCondition.AND, "productID", FilterOperator.EQUAL, 2);

        ParamExpression paramExpression = MybatisQueryProvider.getWhereExpression(ProductView.class, idFilter);
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.putAll(paramExpression.getParamMap());
        queryParams.put("whereExpression", paramExpression.getExpression());

        ProductView productView =
                northwindDao.getProductViewsByDynamic(queryParams).stream().findFirst().orElse(null);
        assertEquals(Long.valueOf(2), productView.getProductID());
    }

    @Test
    public void testEqualNull() throws Exception {
        FilterDescriptor idFilter =
                new FilterDescriptor(FilterCondition.AND, "productID", FilterOperator.EQUAL, null);

        ParamExpression paramExpression = MybatisQueryProvider.getWhereExpression(ProductView.class, idFilter);
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.putAll(paramExpression.getParamMap());
        queryParams.put("whereExpression", paramExpression.getExpression());

        ProductView productView =
                northwindDao.getProductViewsByDynamic(queryParams).stream().findFirst().orElse(null);
        assertEquals(null, productView);
    }

    @Test
    public void testNotEqual() throws Exception {
        FilterDescriptor idFilter =
                new FilterDescriptor(FilterCondition.AND, "productID", FilterOperator.NOT_EQUAL, 2);

        ParamExpression paramExpression = MybatisQueryProvider.getWhereExpression(ProductView.class, idFilter);
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.putAll(paramExpression.getParamMap());
        queryParams.put("whereExpression", paramExpression.getExpression());

        List<ProductView> productViews = northwindDao.getProductViewsByDynamic(queryParams);
        assertEquals(true, productViews.stream().noneMatch(x -> x.getProductID() == 2));
    }

    @Test
    public void testNotEqualNull() throws Exception {
        FilterDescriptor idFilter =
                new FilterDescriptor(FilterCondition.AND, "productID", FilterOperator.NOT_EQUAL, null);

        ParamExpression paramExpression = MybatisQueryProvider.getWhereExpression(ProductView.class, idFilter);
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.putAll(paramExpression.getParamMap());
        queryParams.put("whereExpression", paramExpression.getExpression());

        List<ProductView> productViews = northwindDao.getProductViewsByDynamic(queryParams);
        assertEquals(true, productViews.stream().noneMatch(x -> x.getProductID() == null));
    }


    @Test
    public void testGreaterThanOrEqual() throws Exception {
        FilterDescriptor idFilter =
                new FilterDescriptor(FilterCondition.AND, "productID", FilterOperator.GREATER_THAN_OR_EQUAL, 2);
        ParamExpression paramExpression = MybatisQueryProvider.getWhereExpression(ProductView.class, idFilter);
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.putAll(paramExpression.getParamMap());
        queryParams.put("whereExpression", paramExpression.getExpression());
        List<ProductView> productViews = northwindDao.getProductViewsByDynamic(queryParams);
        assertEquals(true, productViews.stream().allMatch(x -> x.getProductID() >= 2));
    }

    @Test
    public void testGreaterThan() throws Exception {
        FilterDescriptor idFilter =
                new FilterDescriptor(FilterCondition.AND, "productID", FilterOperator.GREATER_THAN, 2);
        ParamExpression paramExpression = MybatisQueryProvider.getWhereExpression(ProductView.class, idFilter);
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.putAll(paramExpression.getParamMap());
        queryParams.put("whereExpression", paramExpression.getExpression());
        List<ProductView> productViews = northwindDao.getProductViewsByDynamic(queryParams);
        assertEquals(true, productViews.stream().allMatch(x -> x.getProductID() > 2));
    }

    @Test
    public void testStartWith() throws Exception {
        FilterDescriptor nameFilter =
                new FilterDescriptor(FilterCondition.AND, "categoryName", FilterOperator.START_WITH, "Be");
        Map<String, Object> queryParams =
                MybatisQueryProvider.getWhereQueryParamMap(
                        ProductView.class, "whereExpression", nameFilter);
        List<ProductView> productViews = northwindDao.getProductViewsByDynamic(queryParams);
        assertEquals("Beverages", productViews.get(0).getCategoryName());
    }

    @Test
    public void testEndWith() throws Exception {
        FilterDescriptor nameFilter =
                new FilterDescriptor(FilterCondition.AND, "categoryName", FilterOperator.END_WITH, "l");
        Map<String, Object> queryParams =
                MybatisQueryProvider.getWhereQueryParamMap(
                        ProductView.class, "whereExpression", nameFilter);
        List<ProductView> productViews = northwindDao.getProductViewsByDynamic(queryParams);
        assertEquals("Oil", productViews.get(0).getCategoryName());
    }

    @Test
    public void testIn() throws Exception {
        FilterDescriptor idFilter =
                new FilterDescriptor(FilterCondition.AND, "productID", FilterOperator.IN, new int[]{2, 4});
        Map<String, Object> queryParams =
                MybatisQueryProvider.getWhereQueryParamMap(
                        ProductView.class, "whereExpression", idFilter);
        List<ProductView> productViews = northwindDao.getProductViewsByDynamic(queryParams);
        assertEquals(Long.valueOf(2), productViews.get(0).getProductID());
        assertEquals(Long.valueOf(4), productViews.get(1).getProductID());
    }

    @Test
    public void testNotIn() throws Exception {
        FilterDescriptor idFilter =
                new FilterDescriptor(FilterCondition.AND, "productID", FilterOperator.NOT_IN, new int[]{2, 4});
        Map<String, Object> queryParams =
                MybatisQueryProvider.getWhereQueryParamMap(
                        ProductView.class, "whereExpression", idFilter);
        List<ProductView> productViews = northwindDao.getProductViewsByDynamic(queryParams);
        assertEquals(Long.valueOf(1), productViews.get(0).getProductID());
        assertEquals(Long.valueOf(3), productViews.get(1).getProductID());
    }

    @Test
    public void testBetween() throws Exception {
        FilterDescriptor idFilter =
                new FilterDescriptor(FilterCondition.AND, "productID", FilterOperator.BETWEEN, new int[]{2, 4});
        Map<String, Object> queryParams =
                MybatisQueryProvider.getWhereQueryParamMap(
                        ProductView.class, "whereExpression", idFilter);
        List<ProductView> productViews = northwindDao.getProductViewsByDynamic(queryParams)
                .stream().sorted(Comparator.comparing(ProductView::getProductID)).collect(Collectors.toList());
        assertEquals(Long.valueOf(2), productViews.get(0).getProductID());
        assertEquals(Long.valueOf(3), productViews.get(1).getProductID());
        assertEquals(Long.valueOf(4), productViews.get(2).getProductID());
    }

    @Test
    public void simpleDemo() throws Exception {
        FilterDescriptor idFilter =
                new FilterDescriptor(FilterCondition.AND, "productID", FilterOperator.GREATER_THAN_OR_EQUAL, 2);
        Map<String, Object> queryParams =
                MybatisQueryProvider.getWhereQueryParamMap(
                        Product.class, "whereExpression", idFilter);
        northwindDao.getProductByDynamic(queryParams);
    }

    @Test
    public void multiFilterDemo() throws Exception {
        FilterDescriptor idFilter =
                new FilterDescriptor(
                        "productID", FilterOperator.GREATER_THAN_OR_EQUAL, 2);
        FilterDescriptor priceFilter =
                new FilterDescriptor(
                        "price", FilterOperator.LESS_THAN, 15);

        Map<String, Object> queryParams =
                MybatisQueryProvider.getWhereQueryParamMap(
                        Product.class, "whereExpression", idFilter, priceFilter);
        Product productView =
                northwindDao.getProductByDynamic(queryParams).stream().findFirst().orElse(null);
        assertEquals(Integer.valueOf(2), productView.getProductID());
    }

    @Test
    public void testGroupFilter() throws Exception {
        FilterGroupDescriptor groupIdFilter = new FilterGroupDescriptor();
        FilterDescriptor idFilter1 =
                new FilterDescriptor(FilterCondition.AND,
                        "productID", FilterOperator.GREATER_THAN, 1);
        FilterDescriptor idFilter2 =
                new FilterDescriptor(FilterCondition.AND,
                        "productID", FilterOperator.LESS_THAN, 4);
        // 把两个 id 筛选当成一个放到同一个组
        groupIdFilter.addFilters(idFilter1, idFilter2);
        // 单独一个简单筛选。
        FilterDescriptor priceFilter =
                new FilterDescriptor(FilterCondition.AND,
                        "price", FilterOperator.GREATER_THAN, 10);

        Map<String, Object> queryParams =
                MybatisQueryProvider.getWhereQueryParamMap(
                        Product.class, "whereExpression", groupIdFilter, priceFilter);

        northwindDao.getProductByDynamic(queryParams);
    }

    @Test
    public void testSort() throws Exception {
        SortDescriptor priceSort =
                new SortDescriptor("price", SortDirection.DESC);

        Map<String, Object> queryParams =
                MybatisQueryProvider.getSortQueryParamMap(
                        Product.class, "orderExpression", priceSort);
        northwindDao.getProductByDynamic(queryParams);
    }

    @Test
    public void testMultiSort() throws Exception {
        SortDescriptor priceSort =
                new SortDescriptor("price", SortDirection.DESC);
        SortDescriptor idSort =
                new SortDescriptor("productID", SortDirection.DESC);

        Map<String, Object> queryParams =
                MybatisQueryProvider.getSortQueryParamMap(
                        Product.class, "orderExpression", priceSort, idSort);
        northwindDao.getProductByDynamic(queryParams);
    }

    @Test
    public void testFilterSort() throws Exception {
        FilterDescriptor idFilter =
                new FilterDescriptor(FilterCondition.AND,
                        "productID", FilterOperator.NOT_EQUAL, 4);
        SortDescriptor priceSort =
                new SortDescriptor("price", SortDirection.ASC);

        Map<String, Object> filterParams = MybatisQueryProvider.getWhereQueryParamMap(
                Product.class, "whereExpression", idFilter);
        Map<String, Object> sortParams =
                MybatisQueryProvider.getSortQueryParamMap(
                        Product.class, "orderExpression", priceSort);

        Map<String, Object> queryParams = new HashMap<>();
        // 放入到同一个map中去。
        queryParams.putAll(filterParams);
        queryParams.putAll(sortParams);
        northwindDao.getProductByDynamic(queryParams);
    }

    @Test
    public void testAgeCustomFilter() throws Exception {
        CustomFilterDescriptor ageFilter =
                new CustomFilterDescriptor(FilterCondition.AND,
                        "price > {0} AND price < {1}", 7, 17);
        Map<String, Object> filterParams = MybatisQueryProvider.getWhereQueryParamMap(
                Product.class, "whereExpression", ageFilter);
        northwindDao.getProductByDynamic(filterParams);
    }

    @Test
    public void testBitand() throws Exception {
        if ("h2".equalsIgnoreCase(active)) {
            CustomFilterDescriptor bitFilter =
                    new CustomFilterDescriptor(FilterCondition.AND,
                            "Bitand(product_id, {0}) > {1}", 2, 0);
            Map<String, Object> filterParams = MybatisQueryProvider.getWhereQueryParamMap(
                    Product.class, "whereExpression", bitFilter);
            List<Product> products = northwindDao.getProductByDynamic(filterParams);
            assertEquals(true, products.size() > 0);
        } else {
            CustomFilterDescriptor bitFilter =
                    new CustomFilterDescriptor(FilterCondition.AND,
                            "product_id & {0} > {1}", 2, 0);
            Map<String, Object> filterParams = MybatisQueryProvider.getWhereQueryParamMap(
                    Product.class, "whereExpression", bitFilter);
            List<Product> products = northwindDao.getProductByDynamic(filterParams);
            assertEquals(true, products.size() > 0);
        }
    }

    @Test
    public void testMultiTablesFilter() throws Exception {
        FilterDescriptor priceFilter1 =
                new FilterDescriptor(ProductView::getPrice,
                        FilterOperator.GREATER_THAN_OR_EQUAL, 6);
        FilterDescriptor priceFilter2 =
                new FilterDescriptor(ProductView::getPrice,
                        FilterOperator.LESS_THAN, 10);
        FilterDescriptor categoryNameFilter =
                new FilterDescriptor(ProductView::getCategoryName,
                        FilterOperator.START_WITH, "Co");

        Map<String, Object> params =
                // NOTE: we recommend you to set "columnsExpressionPlaceholder"
                // in case of duplicated column name in two tables.
                // 这里你也可以不给列的站位，但是推荐使用，防止两个表有重复的名字
                MybatisQueryProvider.createInstance(ProductView.class, "columnsExpression")
                        .addFilters("whereExpression",
                                priceFilter1, priceFilter2, categoryNameFilter)
                        .toQueryParam();

        List<ProductView> result = northwindDao.getProductViewsByDynamic(params);
        assertEquals(true, result.size() > 0);
    }

    @Test
    public void testSelectColumns() {
        DynamicQuery<User> dynamicQuery = DynamicQuery.createQuery(User.class)
                .addFilterDescriptor(User::getId, FilterOperator.EQUAL, 2)
                // select 'Username' field(column)
                .selectProperty(User::getUsername)
                // select 'Id' field(column)
                .selectProperty(User::getId);
        List<User> userNames = userDao.selectByDynamicQuery(dynamicQuery);

        for (User user : userNames) {
            assertEquals(true, StringUtils.isNotBlank(user.getUsername()));
            // password should be empty bse only select 'Username' and 'Id'.
            assertEquals(true, StringUtils.isBlank(user.getPassword()));
        }
    }
}
