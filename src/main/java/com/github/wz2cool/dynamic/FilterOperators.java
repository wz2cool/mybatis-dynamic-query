package com.github.wz2cool.dynamic;

import com.github.wz2cool.dynamic.builder.opeartor.*;

import java.util.Collection;

public final class FilterOperators {

    /// region and operator

    public <R> LessThan<R> lessThan(R value) {
        return new LessThan<>(value);
    }

    public <R> LessThanOrEqual<R> lessThanOrEqual(R value) {
        return new LessThanOrEqual<>(value);
    }

    public <R> Equal<R> isEqual(R value) {
        return new Equal<>(value);
    }

    public <R> NotEqual<R> notEqual(R value) {
        return new NotEqual<>(value);
    }

    public <R> GreaterThanOrEqual<R> greaterThanOrEqual(R value) {
        return new GreaterThanOrEqual<>(value);
    }

    public <R> GreaterThan<R> greaterThan(R value) {
        return new GreaterThan<>(value);
    }

    public <R> StartWith<R> startWith(R value) {
        return new StartWith<>(value);
    }

    public <R> EndWith<R> endWith(R value) {
        return new EndWith<>(value);
    }

    public <R> Contains<R> contains(R value) {
        return new Contains<>(value);
    }

    public <R> NotContains<R> notContains(R value) {
        return new NotContains<>(value);
    }

    @SafeVarargs
    public final <R> In<R> in(R... values) {
        return new In<>(values);
    }

    public <R> In<R> in(Collection<R> values) {
        return new In<>(values);
    }

    @SafeVarargs
    public final <R> NotIn<R> notIn(R... values) {
        return new NotIn<>(values);
    }

    public <R> NotIn<R> notIn(Collection<R> values) {
        return new NotIn<>(values);
    }

    public <R> Between<R> between(R value1, R value2) {
        return new Between<>(value1, value2);
    }

    /// endregion

}
