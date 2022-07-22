package com.github.wz2cool.dynamic.lambda;

/**
 * function 适配 dynamic query(select 中 可以获取需要的列名，同时获取强类型的值)
 *
 * @param <T> 获取对象值
 * @param <R> 返回值
 */
public interface GetExactlyPropertyFunction<T, R extends Comparable<R>> extends GetCommonPropertyFunction<T> {
    R apply(T t);
}

