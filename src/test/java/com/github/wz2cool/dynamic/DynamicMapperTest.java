package com.github.wz2cool.dynamic;

import com.github.pagehelper.PageRowBounds;
import com.github.wz2cool.dynamic.mybatis.db.mapper.UserDao;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.table.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.github.wz2cool.dynamic.builder.DynamicQueryBuilderHelper.isEqual;
import static org.junit.Assert.*;

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
        user.setUserName("frank");
        user.setPassword("frank");

        int result = userDao.insert(user);
        assertEquals(1, result);
    }

    @Test
    public void testInsertSelective() {
        User user = new User();
        user.setId(11);
        user.setUserName("frank");

        int result = userDao.insertSelective(user);
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByPrimaryKey() {
        User user = new User();
        user.setId(12);
        user.setUserName("frank");
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
        user.setUserName("frank");
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
        user.setUserName("frank14");
        user.setPassword("frank");

        int result = userDao.insert(user);
        assertEquals(1, result);

        DynamicQuery<User> dynamicQuery = new DynamicQuery<>(User.class);
        FilterDescriptor nameFilter = new FilterDescriptor(
                FilterCondition.AND, "userName",
                FilterOperator.CONTAINS, "14");
        dynamicQuery.addFilters(nameFilter);

        result = userDao.deleteByDynamicQuery(dynamicQuery);
        assertEquals(1, result);
    }

    @Test
    public void testUpdateByPrimaryKey() {
        User user = new User();
        user.setId(15);
        user.setUserName("frank");
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
        user.setUserName("frank");
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
        user.setUserName("frank17");
        user.setPassword("frank");

        int result = userDao.insert(user);
        assertEquals(1, result);

        User updateUser = new User();
        updateUser.setId(17);
        updateUser.setUserName("Marry");

        DynamicQuery<User> dynamicQuery = new DynamicQuery<>(User.class);
        FilterDescriptor nameFilter = new FilterDescriptor(
                FilterCondition.AND, "userName",
                FilterOperator.CONTAINS, "17");
        dynamicQuery.addFilters(nameFilter);

        result = userDao.updateByDynamicQuery(updateUser, dynamicQuery);
        assertEquals(1, result);
    }

    @Test
    public void testUpdate() {
        User user = new User();
        user.setId(18);
        user.setUserName("frank18");
        user.setPassword("frank");

        int result = userDao.insert(user);
        assertEquals(1, result);

        User updateUser = new User();
        updateUser.setUserName("Marry");

        DynamicQuery<User> dynamicQuery = new DynamicQuery<>(User.class);
        FilterDescriptor nameFilter = new FilterDescriptor(
                FilterCondition.AND, "userName",
                FilterOperator.CONTAINS, "18");
        dynamicQuery.addFilters(nameFilter);

        result = userDao.updateSelectiveByDynamicQuery(updateUser, dynamicQuery);
        assertEquals(1, result);
    }

    @Test
    public void testUpdateByUpdateQuery() {
        User user = new User();
        user.setId(19);
        user.setUserName("frank19");
        user.setPassword("frank");

        int result = userDao.insert(user);
        assertEquals(1, result);

        UpdateQuery<User> userUpdateQuery = UpdateQuery.createQuery(User.class)
                .set(User::getUserName, "Marry")
                .set(User::getPassword, null)
                .and(User::getUserName, isEqual("frank19"));
        result = userDao.updateByUpdateQuery(userUpdateQuery);
        assertEquals(1, result);

        final User user1 = userDao.selectByPrimaryKey(19);
        assertEquals("Marry", user1.getUserName());
        assertNull(user1.getPassword());

        userDao.deleteByPrimaryKey(19);
    }

    @Test
    public void testDeleteByDynamicQueryWithLimit() {
        User user = new User();
        user.setId(20);
        user.setUserName("frank20");
        user.setPassword("frank");

        User user2 = new User();
        user2.setId(21);
        user2.setUserName("frank21");
        user2.setPassword("frank");

        userDao.insert(user);
        userDao.insert(user2);

        DynamicQuery<User> query = DynamicQuery.createQuery(User.class)
                .and(User::getId, o -> o.greaterThanOrEqual(20));
        int count = userDao.selectCountByDynamicQuery(query);
        assertEquals(2, count);

        DynamicQuery<User> deleteQuery = DynamicQuery.createQuery(User.class)
                .and(User::getId, o -> o.greaterThanOrEqual(20))
                .last("limit #{number}")
                .queryParam("number", 1);
        int effectRow = userDao.deleteByDynamicQuery(deleteQuery);
        assertEquals(1, effectRow);
        count = userDao.selectCountByDynamicQuery(query);
        assertEquals(1, count);
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
        user.setUserName("frank19");
        user.setPassword("frank");

        int result = userDao.insert(user);
        assertEquals(1, result);

        User findUser = new User();
        user.setId(19);
        result = userDao.selectCount(findUser);
        assertEquals(true, result > 0);
    }

    @Test
    public void testSelectRowBoundsByDynamicQuery() {
        DynamicQuery<User> dynamicQuery = new DynamicQuery<>(User.class);
        dynamicQuery.setDistinct(true);
        FilterDescriptor idFilter =
                new FilterDescriptor("id", FilterOperator.LESS_THAN, 100);
        dynamicQuery.addFilters(idFilter);

        SortDescriptor idSort =
                new SortDescriptor(User::getId, SortDirection.DESC);
        dynamicQuery.addSorts(idSort);

        PageRowBounds pageRowBounds = new PageRowBounds(1, 2);
        List<User> users = userDao.selectRowBoundsByDynamicQuery(dynamicQuery, pageRowBounds);
        assertEquals(true, users.size() > 0);
    }

    @Test
    public void testSelectCountPropertyByDynamicQuery() {
        User user = new User();
        user.setId(19);
        user.setUserName("frank19");
        user.setPassword("frank");

        userDao.insert(user);
        DynamicQuery<User> query = DynamicQuery.createQuery(User.class)
                .and(User::getUserName, o -> o.isEqual("frank19"));
        query.setDistinct(true);
        Integer result = userDao.selectCountPropertyByDynamicQuery(User::getUserName, query);
        assertEquals(true, result == 1);

        User user2 = new User();
        user2.setId(20);
        user2.setUserName("frank19");
        user2.setPassword("frank");
        userDao.insert(user2);
        query = DynamicQuery.createQuery(User.class)
                .and(User::getUserName, o -> o.isEqual("frank19"));
        result = userDao.selectCountPropertyByDynamicQuery(User::getUserName, query);
        assertTrue(result > 1);
    }
}