package com.github.wz2cool.dynamic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DynamicQuery<T> implements Serializable {
    private static final long serialVersionUID = -4044703018297658438L;

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