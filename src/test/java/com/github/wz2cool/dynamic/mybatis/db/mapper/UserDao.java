package com.github.wz2cool.dynamic.mybatis.db.mapper;

import com.github.wz2cool.dynamic.DynamicQuery;
import com.github.wz2cool.dynamic.mybatis.db.model.entity.table.User;
import com.github.wz2cool.dynamic.mybatis.mapper.DynamicQueryMapper;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Frank
 * \* Date: 8/7/2017
 * \* Time: 5:39 PM
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public interface UserDao extends DynamicQueryMapper<User> {

    List<User> getAllAsBind(@Param(MapperConstants.DYNAMIC_QUERY) DynamicQuery<User> dynamicQuery);

    List<User> getAllAsMap(@Param(MapperConstants.DYNAMIC_QUERY) Map<String, Object> params);
}