package com.github.wz2cool.dynamic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.wz2cool.dynamic.builder.direction.ISortDirection;
import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.lambda.GetCommonPropertyFunction;
import com.github.wz2cool.dynamic.lambda.GetPropertyFunction;
import com.github.wz2cool.dynamic.mybatis.ParamExpression;
import com.github.wz2cool.dynamic.mybatis.QueryHelper;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Map;

/**
 * @author Frank
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
public class DynamicQuery<T> extends BaseDynamicQuery<T, DynamicQuery<T>> {

    private static final long serialVersionUID = -4044703018297658438L;

    public DynamicQuery() {
        // for json
    }

    public DynamicQuery(Class<T> entityClass) {
        this.setEntityClass(entityClass);
    }

    public static <T> DynamicQuery<T> createQuery(Class<T> entityClass) {
        return new DynamicQuery<>(entityClass);
    }
}
