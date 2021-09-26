package com.github.wz2cool.dynamic;

import com.github.pagehelper.PageRowBounds;
import com.github.wz2cool.dynamic.builder.DynamicQueryBuilderHelper;
import com.github.wz2cool.dynamic.mybatis.db.mapper.UserDao;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.table.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.github.wz2cool.dynamic.builder.DynamicQueryBuilderHelper.isEqual;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
    public void selectByDynamicQuery() {
        DynamicQuery<User> query = DynamicQuery.createQuery(User.class);
        query.select(User::getId);
        List<User> users = userDao.selectByDynamicQuery(query);
        List<User> users2 = userDao.selectByDynamicQuery(query);
        System.out.println(users);
        System.out.println(users2);
    }


    @Test
    public void selectByPrimaryKey() {
        System.out.println(userDao.selectByPrimaryKey(2));
    }


    @Test
    public void insert() {
        User user = new User();
//        user.setId(1);
        user.setUserName("aaa");
        user.setPassword("pasworddddddddd");
        System.out.println(userDao.insert(user));
    }


    @Test
    public void selectAll() {
        System.out.println(userDao.selectAll());
    }

    @Test
    public void selecttest() {
        User user = new User();
        user.setId(1);
        System.out.println(userDao.test(user));
    }

    @Test
    public void select() {
        User user = new User();
        user.setId(1);
        System.out.println(userDao.select(user));
    }

    @Test
    public void selectOne() {
        User user = new User();
        user.setId(1);
        System.out.println(userDao.selectOne(user));
    }

    @Test
    public void getAllAsBind() {
        DynamicQuery<User> query = DynamicQuery.createQuery(User.class);
        query.select(User::getId);
        System.out.println(userDao.getAllAsBind(query));
    }


    @Test
    public void getAllAsMap() {
        DynamicQuery<User> query = DynamicQuery.createQuery(User.class);
        query.and(User::getId, DynamicQueryBuilderHelper.notIn(1));
        Map<String, Object> stringObjectMap = query.toQueryParamMap();
        System.out.println(userDao.getAllAsMap(stringObjectMap));
    }


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

        final Optional<User> user1 = userDao.selectByPrimaryKey(19);
        assertEquals("Marry", user1.get().getUserName());
        assertNull(user1.get().getPassword());

        userDao.deleteByPrimaryKey(19);
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

        Optional<User> matchedUser = userDao.selectOne(user);
        assertEquals(Integer.valueOf(1), matchedUser.get().getId());
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
}