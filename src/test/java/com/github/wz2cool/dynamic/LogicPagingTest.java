package com.github.wz2cool.dynamic;

import com.github.wz2cool.dynamic.mybatis.db.mapper.ProductDao;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.table.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author Frank
 * @date 2020/04/11
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TestApplication.class)
public class LogicPagingTest {
    @Autowired
    private ProductDao productDao;

    @Test
    public void testGetData() {
        List<Product> productList = productDao.selectAll();
        System.out.println(productList);
    }
}
