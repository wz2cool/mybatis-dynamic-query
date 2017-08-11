package com.github.wz2cool.dynamic;

import com.github.wz2cool.dynamic.mybatis.db.mapper.UserDao;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.table.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Frank
 * \* Date: 8/11/2017
 * \* Time: 3:11 PM
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TestApplication.class)
public class DynamicMapperTest {
    @Autowired
    private UserDao userDao;

    @Test
    public void testInsert() {
        User user = new User();
        user.setId(10);
        user.setUsername("frank");
        user.setPassword("frank");

        int result = userDao.insert(user);
        assertEquals(1, result);
    }

    @Test
    public void testInsertSelective() {
        User user = new User();
        user.setId(11);
        user.setUsername("frank");

        int result = userDao.insertSelective(user);
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByPrimaryKey() {
        User user = new User();
        user.setId(12);
        user.setUsername("frank");
        user.setPassword("frank");

        int result = userDao.insert(user);
        assertEquals(1, result);

        result = userDao.deleteByPrimaryKey(12);
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByT() {
        User user = new User();
        user.setId(13);
        user.setUsername("frank");
        user.setPassword("frank");

        int result = userDao.insert(user);
        assertEquals(1, result);

        result = userDao.delete(user);
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByDynamicQuery() {
        User user = new User();
        user.setId(14);
        user.setUsername("frank14");
        user.setPassword("frank");

        int result = userDao.insert(user);
        assertEquals(1, result);

        DynamicQuery<User> dynamicQuery = new DynamicQuery<>(User.class);
        FilterDescriptor nameFilter = new FilterDescriptor(
                FilterCondition.AND,
                User.class, User::getUsername,
                FilterOperator.CONTAINS, "14");
        dynamicQuery.addFilter(nameFilter);

        result = userDao.deleteByDynamicQuery(dynamicQuery);
        assertEquals(1, result);
    }

    @Test
    public void testUpdateByPrimaryKey() {
        User user = new User();
        user.setId(15);
        user.setUsername("frank");
        user.setPassword("frank");

        int result = userDao.insert(user);
        assertEquals(1, result);

        user.setPassword("test12345");
        result = userDao.updateByPrimaryKey(user);
        assertEquals(1, result);
    }

    @Test
    public void testUpdateByPrimaryKeySelective() {
        User user = new User();
        user.setId(16);
        user.setUsername("frank");
        user.setPassword("frank");

        int result = userDao.insert(user);
        assertEquals(1, result);

        User updateUser = new User();
        updateUser.setId(16);
        updateUser.setPassword("test123");
        result = userDao.updateByPrimaryKeySelective(updateUser);
        assertEquals(1, result);
    }

    @Test
    public void testUpdateByDynamicQuery() {
        User user = new User();
        user.setId(17);
        user.setUsername("frank17");
        user.setPassword("frank");

        int result = userDao.insert(user);
        assertEquals(1, result);

        User updateUser = new User();
        updateUser.setId(17);
        updateUser.setUsername("Marry");

        DynamicQuery<User> dynamicQuery = new DynamicQuery<>(User.class);
        FilterDescriptor nameFilter = new FilterDescriptor(
                FilterCondition.AND,
                User.class, User::getUsername,
                FilterOperator.CONTAINS, "17");
        dynamicQuery.addFilter(nameFilter);

        result = userDao.updateByDynamicQuery(updateUser, dynamicQuery);
        assertEquals(1, result);
    }

    @Test
    public void testUpdate() {
        User user = new User();
        user.setId(18);
        user.setUsername("frank18");
        user.setPassword("frank");

        int result = userDao.insert(user);
        assertEquals(1, result);

        User updateUser = new User();
        updateUser.setUsername("Marry");

        DynamicQuery<User> dynamicQuery = new DynamicQuery<>(User.class);
        FilterDescriptor nameFilter = new FilterDescriptor(
                FilterCondition.AND,
                User.class, User::getUsername,
                FilterOperator.CONTAINS, "18");
        dynamicQuery.addFilter(nameFilter);

        result = userDao.updateSelectiveByDynamicQuery(updateUser, dynamicQuery);
        assertEquals(1, result);
    }

    @Test
    public void testSelectAll() {
        List<User> users = userDao.selectAll();
        assertEquals(true, users.size() > 0);
    }

    @Test
    public void testSelectByT() {
        User user = new User();
        user.setId(1);

        List<User> users = userDao.select(user);
        assertEquals(1, users.size());
    }

    @Test
    public void testSelectOne() {
        User user = new User();
        user.setId(1);

        User matchedUser = userDao.selectOne(user);
        assertEquals(Integer.valueOf(1), matchedUser.getId());
    }

    @Test
    public void testSelectCount() {
        User user = new User();
        user.setId(19);
        user.setUsername("frank19");
        user.setPassword("frank");

        int result = userDao.insert(user);
        assertEquals(1, result);

        User findUser = new User();
        user.setId(19);
        result = userDao.selectCount(findUser);
        assertEquals(true, result > 0);
    }

    @Test
    public void testSelectCountByDynamicQuery() {

    }
}