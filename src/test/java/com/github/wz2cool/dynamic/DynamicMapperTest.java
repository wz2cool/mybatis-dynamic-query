package com.github.wz2cool.dynamic;

import com.github.wz2cool.dynamic.builder.DynamicQueryBuilderHelper;
import com.github.wz2cool.dynamic.mybatis.db.mapper.UserDao;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.table.User;
import org.apache.ibatis.session.RowBounds;
import org.junit.Assert;
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
        query.and(User::getId, DynamicQueryBuilderHelper.notIn(1));

        List<User> users = userDao.selectByDynamicQuery(query);
        System.out.println(users);

        User user = new User();
        user.setUserName("setUserName");
        user.setPassword("setPassword");
        user.setUselessProperty("setUselessProperty");
        user.setId(1000010);
        int insert = userDao.insert(user);
        assertEquals(1, insert);

        User user1 = userDao.selectFirstByDynamicQuery(DynamicQuery.createQuery(User.class)
                .and(User::getId, isEqual(1000010))).orElse(null);
        Assert.assertNotNull(user1);

        user.setPassword(null);
        UpdateQuery<User> updateQuery = UpdateQuery.createQuery(User.class);
        updateQuery.set(user, a -> a.ignore(User::getId));
        updateQuery.and(User::getId, isEqual(1000010));
        userDao.updateByUpdateQuery(updateQuery);

        user1.setPassword(null);
        user1.setUserName("newname");
        userDao.updateByDynamicQuery(user1, DynamicQuery.createQuery(User.class).and(User::getId, isEqual(1000010)));
    }


    @Test
    public void selectByPrimaryKey() {
        System.out.println(userDao.selectByPrimaryKey(11111));
    }

    @Test
    public void insert2() {
        System.out.println(userDao.selectByPrimaryKey(11111111));
    }


    @Test
    public void deleteByPrimaryKey() {
        System.out.println(userDao.deleteByPrimaryKey(1));
    }

    @Test
    public void delete() {
        User user = new User();
        user.setId(11111);
        user.setUserName("aaa");
        System.out.println(userDao.delete(user));
    }

    @Test
    public void insert() {
        User user = new User();
        user.setId(11111);
        user.setUserName("aaa");
        user.setPassword("");
        System.out.println(userDao.insert(user));
        user.setId(12331213);
        user.setUserName("1");
        user.setPassword("");
        System.out.println(userDao.insertSelective(user));
        System.out.println(userDao.selectByPrimaryKey(11111));
    }


    @Test
    public void insertSelective() {
        User user = new User();
        user.setId(1188111);
        user.setUserName("aaa");
        System.out.println(userDao.insertSelective(user));

        System.out.println(userDao.selectByPrimaryKey(1188111));
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
        System.out.println(userDao.select(null));
    }

    @Test
    public void selectOne() {
        User user = new User();
        user.setId(1);
        System.out.println(userDao.selectOne(user).getId());
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
        result = userDao.updateByPrimaryKeySelective(user);
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

        final Optional<User> user1 = userDao.selectByPrimaryKeyForOptional(19);
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

        Optional<User> matchedUser = userDao.selectOneForOptional(user);
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

        RowBounds pageRowBounds = new RowBounds(1, 2);
        List<User> users = userDao.selectRowBoundsByDynamicQuery(dynamicQuery, pageRowBounds);
        System.out.println(users);
//        assertEquals(true, users.size() > 0);
    }
}