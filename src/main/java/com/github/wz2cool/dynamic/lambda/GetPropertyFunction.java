package com.github.wz2cool.dynamic.lambda;

import java.io.Serializable;
import java.util.function.Function;

@FunctionalInterface
public interface GetPropertyFunction<T> extends Function<T, Object>, Serializable {
}