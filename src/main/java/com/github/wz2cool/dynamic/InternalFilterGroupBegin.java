package com.github.wz2cool.dynamic;

/**
 * @author Frank
 **/
public class InternalFilterGroupBegin<T, S extends BaseFilterGroup<T, S>> extends BaseFilterGroup<T, InternalFilterGroupBegin<T, S>> {

    private S owner;

    private FilterCondition condition;

    private boolean enable;

    public InternalFilterGroupBegin(boolean enable, FilterCondition condition, S owner) {
        this.enable = enable;
        this.condition = condition;
        this.owner = owner;
    }

    public S groupEnd() {
        if (enable) {
            FilterGroupDescriptor<T> filterGroupDescriptor = new FilterGroupDescriptor<>();
            filterGroupDescriptor.setCondition(condition);
            filterGroupDescriptor.setFilters(this.getFilters());
            owner.addFilters(filterGroupDescriptor);
        }
        return owner;
    }
}
