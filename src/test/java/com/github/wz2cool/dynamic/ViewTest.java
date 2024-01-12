package com.github.wz2cool.dynamic;

import com.github.wz2cool.dynamic.mybatis.db.mapper.ProductViewMapper;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.view.ProductView;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Frank
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TestApplication.class)
public class ViewTest {

    @Resource
    private ProductViewMapper productViewMapper;


    @Test
    public void testSelect() {
        DynamicQuery<ProductView> query = DynamicQuery.createQuery(ProductView.class);
        query.queryParam("spring_env", "");
        List<ProductView> productViewList = productViewMapper.selectByDynamicQuery(query);
        Assert.assertTrue(!productViewList.isEmpty());
    }
}
