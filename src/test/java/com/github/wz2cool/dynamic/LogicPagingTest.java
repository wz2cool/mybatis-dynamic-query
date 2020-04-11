package com.github.wz2cool.dynamic;

import com.github.wz2cool.dynamic.model.LogicPagingResult;
import com.github.wz2cool.dynamic.mybatis.db.mapper.ProductDao;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.table.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

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
        LogicPagingQuery<Product> logicPagingQuery =
                LogicPagingQuery.createQuery(Product.class, Product::getProductId, SortDirection.DESC, UpDown.DOWN);
        logicPagingQuery.setPageSize(2);
        logicPagingQuery.setLastStartPageId(4L);
        logicPagingQuery.setLastEndPageId(3L);
        LogicPagingResult<Product> result = productDao.selectByLogicPaging(logicPagingQuery);
        System.out.println(result);
    }
}
