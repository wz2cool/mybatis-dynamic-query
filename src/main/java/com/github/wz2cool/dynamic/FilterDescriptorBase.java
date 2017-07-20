package com.github.wz2cool.dynamic;

/**
 * Created by Frank on 2017/4/14.
 */
public abstract class FilterDescriptorBase {
    private FilterCondition condition = FilterCondition.AND;

    public FilterCondition getCondition() {
        return condition;
    }

    public void setCondition(FilterCondition condition) {
        this.condition = condition;
    }
}
