package com.github.wz2cool.dynamic.mybatis.annotation;

import com.github.wz2cool.dynamic.mybatis.JdbcType;

import java.lang.annotation.*;

/**
 * Created by Frank on 2017/6/23.
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String name() default "";

    boolean insertIgnore() default false;

    boolean updateIfNull() default false;

    boolean insertIfNull() default false;

    String tableOrAlias() default "";

    JdbcType jdbcType() default JdbcType.NONE;
}
