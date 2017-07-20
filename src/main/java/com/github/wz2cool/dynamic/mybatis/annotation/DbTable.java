package com.github.wz2cool.dynamic.mybatis.annotation;

import java.lang.annotation.*;

/**
 * Created by Frank on 2017/6/18.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DbTable {
    String name() default "";
}
