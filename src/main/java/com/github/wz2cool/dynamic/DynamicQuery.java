package com.github.wz2cool.dynamic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("squid:S1948")
public class DynamicQuery<T> implements Serializable {
    private static final long serialVersionUID = -4044703018297658438L;

    private boolean distinct;
    private Class<T> entityClass;
    private FilterDescriptorBase[] filters = new FilterDescriptorBase[]{};
    private SortDescriptor[] sorts = new SortDescriptor[]{};

    public DynamicQuery() {
        // for json
    }

    public DynamicQuery(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public FilterDescriptorBase[] getFilters() {
        return filters;
    }

    public void setFilters(FilterDescriptorBase[] filters) {
        this.filters = filters;
    }

    public SortDescriptor[] getSorts() {
        return sorts;
    }

    public void setSorts(SortDescriptor[] sorts) {
        this.sorts = sorts;
    }

    public boolean addFilter(FilterDescriptorBase newFilter) {
        return addFilters(newFilter);
    }

    @SuppressWarnings("Duplicates")
    public boolean addFilters(FilterDescriptorBase... newFilters) {
        List<FilterDescriptorBase> filtersCopy =
                filters == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(filters));
        List<FilterDescriptorBase> newFilterList = Arrays.asList(newFilters);
        boolean result = filtersCopy.addAll(newFilterList);
        this.setFilters(filtersCopy.toArray(new FilterDescriptorBase[filtersCopy.size()]));
        return result;
    }

    @SuppressWarnings("Duplicates")
    public boolean removeFilter(FilterDescriptorBase removeFilter) {
        List<FilterDescriptorBase> filtersCopy =
                filters == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(filters));
        boolean result = filtersCopy.remove(removeFilter);
        this.setFilters(filtersCopy.toArray(new FilterDescriptorBase[filtersCopy.size()]));
        return result;
    }


    public boolean addSorts(SortDescriptor... newFilters) {
        List<SortDescriptor> sortsCopy =
                sorts == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(sorts));
        List<SortDescriptor> newSortLists = Arrays.asList(newFilters);
        boolean result = sortsCopy.addAll(newSortLists);
        if (result) {
            this.setSorts(sortsCopy.toArray(new SortDescriptor[sortsCopy.size()]));
        }
        return result;
    }

    public boolean removeSort(SortDescriptor sort) {
        List<SortDescriptor> sortsCopy =
                sorts == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(sorts));
        boolean result = sortsCopy.remove(sort);
        if (result) {
            this.setSorts(sortsCopy.toArray(new SortDescriptor[sortsCopy.size()]));
        }
        return result;
    }
}