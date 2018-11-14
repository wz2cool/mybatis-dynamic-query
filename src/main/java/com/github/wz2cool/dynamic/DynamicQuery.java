package com.github.wz2cool.dynamic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings("squid:S1948")
public class DynamicQuery<T> implements Serializable {
    private static final long serialVersionUID = -4044703018297658438L;

    private boolean distinct;
    private Class<T> entityClass;
    private FilterDescriptorBase[] filters = new FilterDescriptorBase[]{};
    private SortDescriptorBase[] sorts = new SortDescriptor[]{};

    public DynamicQuery() {
        // for json
    }

    public DynamicQuery(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public static <T> DynamicQuery<T> createQuery(Class<T> entityClass) {
        return new DynamicQuery<>(entityClass);
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

    public SortDescriptorBase[] getSorts() {
        return sorts;
    }

    public void setSorts(SortDescriptorBase[] sorts) {
        this.sorts = sorts;
    }

    public DynamicQuery<T> addFilter(FilterDescriptorBase newFilter) {
        return addFilters(newFilter);
    }

    @SuppressWarnings("Duplicates")
    public DynamicQuery<T> addFilters(FilterDescriptorBase... newFilters) {
        List<FilterDescriptorBase> filtersCopy =
                filters == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(filters));
        List<FilterDescriptorBase> newFilterList = Arrays.asList(newFilters);
        boolean result = filtersCopy.addAll(newFilterList);
        if (result) {
            this.setFilters(filtersCopy.toArray(new FilterDescriptorBase[filtersCopy.size()]));
        }
        return this;
    }

    @SuppressWarnings("Duplicates")
    public DynamicQuery<T> removeFilter(FilterDescriptorBase removeFilter) {
        List<FilterDescriptorBase> filtersCopy =
                filters == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(filters));
        boolean result = filtersCopy.remove(removeFilter);
        if (result) {
            this.setFilters(filtersCopy.toArray(new FilterDescriptorBase[filtersCopy.size()]));
        }
        return this;
    }


    public DynamicQuery<T> addSorts(SortDescriptorBase... newFilters) {
        List<SortDescriptorBase> sortsCopy =
                sorts == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(sorts));
        List<SortDescriptorBase> newSortLists = Arrays.asList(newFilters);
        boolean result = sortsCopy.addAll(newSortLists);
        if (result) {
            this.setSorts(sortsCopy.toArray(new SortDescriptorBase[sortsCopy.size()]));
        }
        return this;
    }

    public DynamicQuery<T> removeSort(SortDescriptorBase sort) {
        List<SortDescriptorBase> sortsCopy =
                sorts == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(sorts));
        boolean result = sortsCopy.remove(sort);
        if (result) {
            this.setSorts(sortsCopy.toArray(new SortDescriptorBase[sortsCopy.size()]));
        }
        return this;
    }

    public DynamicQuery<T> addFilterDescriptor(
            FilterCondition condition, Function<T, Object> getFieldFunc,
            FilterOperator operator, Object value) {
        FilterDescriptor filterDescriptor = new FilterDescriptor(condition, this.entityClass, getFieldFunc, operator, value);
        return addFilter(filterDescriptor);
    }

    public DynamicQuery<T> addFilterDescriptor(
            Function<T, Object> getFieldFunc,
            FilterOperator operator, Object value) {
        FilterDescriptor filterDescriptor = new FilterDescriptor(FilterCondition.AND, this.entityClass, getFieldFunc, operator, value);
        return addFilter(filterDescriptor);
    }

    public DynamicQuery<T> addSortDescriptor(Function<T, Object> getFieldFunc, SortDirection sortDirection) {
        SortDescriptor sortDescriptor = new SortDescriptor(this.entityClass, getFieldFunc, sortDirection);
        return addSorts(sortDescriptor);
    }
}