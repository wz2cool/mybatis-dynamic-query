package com.github.wz2cool.dynamic.lambda;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author Frank
 */
@FunctionalInterface
public interface GetPropertyFunction<T, R> extends Function<T, R>, Serializable {

}