package com.github.wz2cool.dynamic;

/**
 * @author Frank
 * @date 2019/10/01
 **/
public class InternalFilterGroupBegin<T, S extends BaseFilterGroup<T, S>> extends BaseFilterGroup<T, InternalFilterGroupBegin<T, S>> {

    private S owner;

    private FilterCondition condition;

    public void setOwner(S owner) {
        this.owner = owner;
    }

    public void setCondition(FilterCondition condition) {
        this.condition = condition;
    }

    public S groupEnd() {
        FilterGroupDescriptor<T> filterGroupDescriptor = new FilterGroupDescriptor<>();
        filterGroupDescriptor.setCondition(condition);
        filterGroupDescriptor.setFilters(this.getFilters());
        owner.addFilters(filterGroupDescriptor);
        return owner;
    }
}
