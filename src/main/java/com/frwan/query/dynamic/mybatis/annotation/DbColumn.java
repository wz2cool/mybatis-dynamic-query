package com.frwan.query.dynamic.mybatis.annotation;

import java.lang.annotation.*;

/**
 * Created by Frank on 2017/6/23.
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DbColumn {
    String name() default "";

    boolean updateIfNull() default false;
}
