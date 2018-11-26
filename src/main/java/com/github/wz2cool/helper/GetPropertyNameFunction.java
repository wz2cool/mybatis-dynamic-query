package com.github.wz2cool.helper;

import java.io.Serializable;
import java.util.function.Function;

// https://zhuanlan.zhihu.com/p/37335673
@FunctionalInterface
public interface GetPropertyNameFunction<T> extends Function<T, Object>, Serializable {

}