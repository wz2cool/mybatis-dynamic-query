package com.github.wz2cool.helper;

import java.io.Serializable;
import java.util.function.Function;

@FunctionalInterface
public interface GetPropertyNameFunction<T> extends Function<T, Object>, Serializable {

}