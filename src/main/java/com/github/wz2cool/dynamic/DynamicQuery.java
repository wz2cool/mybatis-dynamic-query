package com.github.wz2cool.dynamic;

import java.util.ArrayList;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Frank
 * \* Date: 8/9/2017
 * \* Time: 2:43 PM
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class DynamicQuery<T> {
    private boolean distinct;
    private final Class<T> entityClass;
    private final List<FilterDescriptorBase> filters = new ArrayList<>();
    private final List<SortDescriptor> sorts = new ArrayList<>();

    public DynamicQuery(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    public FilterDescriptorBase[] getFilters() {
        return filters.toArray(new FilterDescriptorBase[filters.size()]);
    }

    public SortDescriptor[] getSorts() {
        return sorts.toArray(new SortDescriptor[sorts.size()]);
    }

    public boolean addFilter(FilterDescriptorBase filter) {
        return this.filters.add(filter);
    }

    public boolean removeFilter(FilterDescriptorBase filter) {
        return this.filters.remove(filter);
    }

    public boolean addSort(SortDescriptor sort) {
        return this.sorts.add(sort);
    }

    public boolean removeSort(SortDescriptor sort) {
        return this.sorts.remove(sort);
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }
}