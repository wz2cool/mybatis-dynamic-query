package com.github.wz2cool.dynamic.mybatis.annotation;

import java.lang.annotation.*;

/**
 * Created by Frank on 2017/3/23.
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryColumn {
    String name() default "";
}
