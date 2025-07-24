package com.github.wz2cool.dynamic.mybatis.db.mapper;

import com.github.wz2cool.dynamic.mybatis.db.model.entity.table.User;
import com.github.wz2cool.dynamic.mybatis.mapper.DynamicQueryMapper;
import com.github.wz2cool.dynamic.mybatis.mapper.mysql.InsertIgnoreMapper;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Frank
 * \* Date: 8/7/2017
 * \* Time: 5:39 PM
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public interface UserDao extends DynamicQueryMapper<User>, InsertIgnoreMapper<User> {
}