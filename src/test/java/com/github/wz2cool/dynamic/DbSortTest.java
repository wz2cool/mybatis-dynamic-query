package com.github.wz2cool.dynamic;

import com.github.wz2cool.dynamic.mybatis.MybatisQueryProvider;
import com.github.wz2cool.dynamic.mybatis.db.mapper.NorthwindDao;
import com.github.wz2cool.dynamic.mybatis.db.mapper.UserDao;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.table.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TestApplication.class)
public class DbSortTest {
    @Value("${spring.profiles.active}")
    private String active;
    @Autowired
    private NorthwindDao northwindDao;
    @Autowired
    private UserDao userDao;

    @Test
    public void testIdDescSort() throws Exception {
        SortDescriptor idSort = new SortDescriptor(Product.class, Product::getProductID, SortDirection.DESC);
        Map<String, Object> queryParam =
                MybatisQueryProvider.createInstance(Product.class)
                        .addSorts("orderExpression", idSort)
                        .toQueryParam();
        List<Product> productList = northwindDao.getProductByDynamic(queryParam);

        for (int i = 0; i < productList.size(); i++) {
            if (i == productList.size() - 1) {
                break;
            }

            Product p1 = productList.get(i);
            Product p2 = productList.get(i + 1);
            assertEquals(1, p1.getProductID().compareTo(p2.getCategoryID()));
        }
    }

    @Test
    public void testCustomSort() throws Exception {
        CustomSortDescriptor id2TopSort = new CustomSortDescriptor();
        id2TopSort.setExpression("CASE product.product_id WHEN {0} THEN {1} ELSE product.product_id END DESC");
        id2TopSort.setParams(2, Integer.MAX_VALUE);
        Map<String, Object> queryParam =
                MybatisQueryProvider.createInstance(Product.class)
                        .addSorts("orderExpression", id2TopSort)
                        .toQueryParam();
        List<Product> productList = northwindDao.getProductByDynamic(queryParam);
        assertEquals(Integer.valueOf(2), productList.get(0).getProductID());
    }
}