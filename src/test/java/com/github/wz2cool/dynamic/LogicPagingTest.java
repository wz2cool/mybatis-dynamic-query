package com.github.wz2cool.dynamic;

import com.github.wz2cool.dynamic.model.LogicPagingResult;
import com.github.wz2cool.dynamic.mybatis.db.mapper.ProductDao;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.table.Product;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Frank
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TestApplication.class)
public class LogicPagingTest {
    @Autowired
    private ProductDao productDao;

    @Test
    public void testGetDataAscUp() {
        LogicPagingQuery<Product> logicPagingQuery =
                LogicPagingQuery.createQuery(Product.class, Product::getProductId, SortDirection.ASC, UpDown.UP);
        logicPagingQuery.setPageSize(2);
        logicPagingQuery.setLastStartPageId(3L);
        logicPagingQuery.setLastEndPageId(4L);
        LogicPagingResult<Product> result = productDao.selectByLogicPaging(logicPagingQuery);
        Assert.assertTrue(Long.valueOf(1).equals(result.getList().get(0).getProductId()));
        Assert.assertTrue(Long.valueOf(2).equals(result.getList().get(1).getProductId()));
        Assert.assertTrue(Long.valueOf(1).equals(result.getStartPageId()));
        Assert.assertTrue(Long.valueOf(2).equals(result.getEndPageId()));
    }

    @Test
    public void testGetDataAscDown() {
        LogicPagingQuery<Product> logicPagingQuery =
                LogicPagingQuery.createQuery(Product.class, Product::getProductId, SortDirection.ASC, UpDown.DOWN);
        logicPagingQuery.setPageSize(2);
        logicPagingQuery.setLastStartPageId(1L);
        logicPagingQuery.setLastEndPageId(2L);
        LogicPagingResult<Product> result = productDao.selectByLogicPaging(logicPagingQuery);
        Assert.assertTrue(Long.valueOf(3).equals(result.getList().get(0).getProductId()));
        Assert.assertTrue(Long.valueOf(4).equals(result.getList().get(1).getProductId()));
        Assert.assertTrue(Long.valueOf(3).equals(result.getStartPageId()));
        Assert.assertTrue(Long.valueOf(4).equals(result.getEndPageId()));
    }

    @Test
    public void testGetDataDescUp() {
        LogicPagingQuery<Product> logicPagingQuery =
                LogicPagingQuery.createQuery(Product.class, Product::getProductId, SortDirection.DESC, UpDown.UP);
        logicPagingQuery.setPageSize(2);
        logicPagingQuery.setLastStartPageId(2L);
        logicPagingQuery.setLastEndPageId(1L);
        LogicPagingResult<Product> result = productDao.selectByLogicPaging(logicPagingQuery);
        Assert.assertTrue(Long.valueOf(4).equals(result.getList().get(0).getProductId()));
        Assert.assertTrue(Long.valueOf(3).equals(result.getList().get(1).getProductId()));
        Assert.assertTrue(Long.valueOf(4).equals(result.getStartPageId()));
        Assert.assertTrue(Long.valueOf(3).equals(result.getEndPageId()));
    }

    @Test
    public void testGetDataDescDown() {
        LogicPagingQuery<Product> logicPagingQuery =
                LogicPagingQuery.createQuery(Product.class, Product::getProductId, SortDirection.DESC, UpDown.DOWN);
        logicPagingQuery.setPageSize(2);
        logicPagingQuery.setLastStartPageId(5L);
        logicPagingQuery.setLastEndPageId(4L);
        LogicPagingResult<Product> result = productDao.selectByLogicPaging(logicPagingQuery);
        Assert.assertTrue(Long.valueOf(3).equals(result.getList().get(0).getProductId()));
        Assert.assertTrue(Long.valueOf(2).equals(result.getList().get(1).getProductId()));
        Assert.assertTrue(Long.valueOf(3).equals(result.getStartPageId()));
        Assert.assertTrue(Long.valueOf(2).equals(result.getEndPageId()));
    }
}
