package com.github.wz2cool.dynamic;

import com.github.wz2cool.dynamic.builder.DynamicQueryBuilder;
import com.github.wz2cool.dynamic.mybatis.db.mapper.NorthwindDao;
import com.github.wz2cool.dynamic.mybatis.db.mapper.ProductDao;
import com.github.wz2cool.dynamic.mybatis.db.mapper.UserDao;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.table.Category;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.table.Product;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.table.User;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.github.wz2cool.dynamic.builder.DynamicQueryBuilderHelper.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

    @Autowired
    private ProductDao productDao;

    @Test
    public void testSelectFirst() {
        DynamicQuery<Product> query = DynamicQuery.createQuery(Product.class)
                .and(Product::getProductId, isEqual(1L));
        Optional<Product> productOptional = productDao.selectFirstByDynamicQuery(query);
        assertTrue(productOptional.isPresent());

        DynamicQuery<Product> query2 = DynamicQuery.createQuery(Product.class)
                .and(Product::getProductId, isEqual(100000L));
        Optional<Product> productOptional2 = productDao.selectFirstByDynamicQuery(query2);
        Assert.assertFalse(productOptional2.isPresent());

        // fetch max price
        DynamicQuery<Product> query3 = DynamicQuery.createQuery(Product.class)
                .select(Product::getPrice)
                .orderBy(Product::getPrice, desc());
        Optional<Product> productOptional3 = productDao.selectFirstByDynamicQuery(query3);
        if (productOptional3.isPresent()) {
            BigDecimal maxPrice = productOptional3.get().getPrice();
            Assert.assertNotNull(maxPrice);
        }
    }

    @Test
    public void testBuilderSearch() throws Exception {
        DynamicQuery<Product> query = DynamicQueryBuilder.create(Product.class)
                .selectAll()
                .where(Product::getPrice, greaterThan(BigDecimal.valueOf(1)))
                .build();
        List<Product> productList = productDao.selectByDynamicQuery(query);
        Assert.assertFalse(productList.isEmpty());
    }

    @Test
    public void testDbInit() throws Exception {
        List<Category> categories = northwindDao.getCategories();
        assertTrue(categories.size() > 0);

        List<Product> products = northwindDao.getProducts();
        assertTrue(products.size() > 0);
    }

    @Test
    public void testSelectColumns() {
        DynamicQuery<User> dynamicQuery = DynamicQuery.createQuery(User.class)
                .select(User::getUserName, User::getId)
                .and(User::getId, isEqual(2));
        List<User> userNames = userDao.selectByDynamicQuery(dynamicQuery);

        for (User user : userNames) {
            assertEquals(true, StringUtils.isNotBlank(user.getUserName()));
            // password should be empty bse only select 'Username' and 'Id'.
            assertEquals(true, StringUtils.isBlank(user.getPassword()));
        }
    }
}
