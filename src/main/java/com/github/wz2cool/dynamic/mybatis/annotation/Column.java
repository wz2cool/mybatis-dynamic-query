package com.github.wz2cool.dynamic.mybatis.annotation;

import java.lang.annotation.*;

/**
 * Created by Frank on 2017/6/23.
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String name() default "";

    boolean updateIfNull() default false;

    String tableOrAlias() default "";
}
