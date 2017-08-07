package com.github.wz2cool.dynamic.mybatis.mapper.provider;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Frank
 * \* Date: 8/7/2017
 * \* Time: 5:54 PM
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class DynamicQuerySqlHelper {
    public static String dynamicWhereClause() {
        return "<if test=\"whereExpression != null and whereExpression != ''\">WHERE ${whereExpression}</if>";
    }
}