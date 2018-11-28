package com.github.wz2cool.dynamic.lambda;

import java.io.Serializable;
import java.util.function.Function;

// https://zhuanlan.zhihu.com/p/37335673
@FunctionalInterface
public interface GetPropertyFunction<T> extends Function<T, Object>, Serializable {

}