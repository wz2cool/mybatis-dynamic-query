package com.github.wz2cool.dynamic.mybatis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Frank
 * @see <a href="https://wz2cool.github.io/2020/04/19/view-annotation/">View 注解</a>
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface View {
    String value() default "";
}
