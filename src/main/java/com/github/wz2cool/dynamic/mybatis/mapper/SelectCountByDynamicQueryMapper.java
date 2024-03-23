package com.github.wz2cool.dynamic.mybatis.mapper;

import com.github.wz2cool.dynamic.DynamicQuery;
import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.lambda.GetPropertyFunction;
import com.github.wz2cool.dynamic.mybatis.QueryHelper;
import com.github.wz2cool.dynamic.mybatis.TypeHelper;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicQueryProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * @author Frank
 */
@RegisterMapper
public interface SelectCountByDynamicQueryMapper<T> {

    QueryHelper QUERY_HELPER = new QueryHelper();

    /**
     * select count by dynamic query.
     *
     * @param dynamicQuery dynamic query
     * @return the count of items
     */
    @SelectProvider(type = DynamicQueryProvider.class, method = "dynamicSQL")
    int selectCountByDynamicQuery(@Param(MapperConstants.DYNAMIC_QUERY) DynamicQuery<T> dynamicQuery);

    @SelectProvider(type = DynamicQueryProvider.class, method = "dynamicSQL")
    Integer selectCountPropertyByDynamicQuery(@Param(MapperConstants.COLUMN) String column, @Param(MapperConstants.DYNAMIC_QUERY) DynamicQuery<T> dynamicQuery);

    default <R extends Comparable<?>> Integer selectCountPropertyByDynamicQuery(GetPropertyFunction<T, R> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        Object result = selectCountPropertyByDynamicQueryInternal(getPropertyFunction, dynamicQuery);
        return TypeHelper.getInteger(result);
    }

    default <R extends Comparable<?>> Object selectCountPropertyByDynamicQueryInternal(
            GetPropertyFunction<T, R> getPropertyFunction, DynamicQuery<T> dynamicQuery) {
        String propertyName = CommonsHelper.getPropertyName(getPropertyFunction);
        Class entityClass = dynamicQuery.getEntityClass();
        String queryColumn = QUERY_HELPER.getQueryColumnByProperty(entityClass, propertyName);
        return selectCountPropertyByDynamicQuery(queryColumn, dynamicQuery);
    }
}