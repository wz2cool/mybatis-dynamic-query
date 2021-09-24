package com.github.wz2cool.dynamic;

import com.github.wz2cool.dynamic.builder.DynamicQueryBuilderHelper;
import com.github.wz2cool.dynamic.mybatis.db.mapper.TeacherDao;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.table.Teacher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TestApplication.class)
public class DynamicLogicDeleteMapperTest {
    @Autowired
    private TeacherDao teacherDao;

    @Test
    public void testInsert() {
        Teacher teacher = new Teacher();
//        teacher.setDeleted(0);
        teacher.setId(0L);
        teacher.setName("111");
        int result = teacherDao.insert(teacher);
        assertEquals(1, result);
    }

    @Test
    public void testDelete() {
        teacherDao.deleteByPrimaryKey(100L);
    }


    @Test
    public void testDelete2() {
        DynamicQuery<Teacher> query = DynamicQuery.createQuery(Teacher.class);
        query.and(Teacher::getId, DynamicQueryBuilderHelper.isEqual(1L));
        query.and(Teacher::getName, DynamicQueryBuilderHelper.isEqual("1L"));
        teacherDao.deleteByDynamicQuery(query);
    }

    @Test
    public void testDelete3() {
        DynamicQuery<Teacher> query = DynamicQuery.createQuery(Teacher.class);
        query.and(Teacher::getId, DynamicQueryBuilderHelper.isEqual(1L));
        query.and(Teacher::getName,DynamicQueryBuilderHelper.isEqual(" or  \\'  or '1'= '1"));
        teacherDao.selectByDynamicQuery(query);
    }

    @Test
    public void testDelete4() {
        teacherDao.deleteByPrimaryKey(1L);
    }
}