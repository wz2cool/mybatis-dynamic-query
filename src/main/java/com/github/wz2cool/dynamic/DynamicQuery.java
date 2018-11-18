package com.github.wz2cool.dynamic;

import com.github.wz2cool.helper.CommonsHelper;
import org.apache.commons.lang3.ArrayUtils;

import java.io.Serializable;
import java.util.function.Function;

@SuppressWarnings("squid:S1948")
public class DynamicQuery<T> implements Serializable {
    private static final long serialVersionUID = -4044703018297658438L;

    private boolean distinct;
    private Class<T> entityClass;
    private FilterDescriptorBase[] filters = new FilterDescriptorBase[]{};
    private SortDescriptorBase[] sorts = new SortDescriptor[]{};
    private String[] selectProperties = new String[]{};

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

    public String[] getSelectProperties() {
        return selectProperties;
    }

    public void setSelectProperties(String[] selectProperties) {
        this.selectProperties = selectProperties;
    }

    public DynamicQuery<T> addFilter(FilterDescriptorBase newFilter) {
        return addFilters(newFilter);
    }

    @SuppressWarnings("Duplicates")
    public DynamicQuery<T> addFilters(FilterDescriptorBase... newFilters) {
        FilterDescriptorBase[] newAllFilters = ArrayUtils.addAll(this.filters, newFilters);
        this.setFilters(newAllFilters);
        return this;
    }

    @SuppressWarnings("Duplicates")
    public DynamicQuery<T> removeFilter(FilterDescriptorBase removeFilter) {
        FilterDescriptorBase[] newAllFilters = ArrayUtils.removeAllOccurences(this.filters, removeFilter);
        this.setFilters(newAllFilters);
        return this;
    }


    public DynamicQuery<T> addSorts(SortDescriptorBase... newSorts) {
        SortDescriptorBase[] newAllSorts = ArrayUtils.addAll(this.sorts, newSorts);
        this.setSorts(newAllSorts);
        return this;
    }

    public DynamicQuery<T> removeSort(SortDescriptorBase sort) {
        SortDescriptorBase[] newAllSorts = ArrayUtils.removeAllOccurences(this.sorts, sort);
        this.setSorts(newAllSorts);
        return this;
    }

    public DynamicQuery<T> addFilterDescriptor(
            FilterCondition condition, Function<T, Object> getPropertyFunc,
            FilterOperator operator, Object value) {
        FilterDescriptor filterDescriptor = new FilterDescriptor(condition, this.entityClass, getPropertyFunc, operator, value);
        return addFilter(filterDescriptor);
    }

    public DynamicQuery<T> addFilterDescriptor(
            Function<T, Object> getPropertyFunc,
            FilterOperator operator, Object value) {
        FilterDescriptor filterDescriptor = new FilterDescriptor(FilterCondition.AND, this.entityClass, getPropertyFunc, operator, value);
        return addFilter(filterDescriptor);
    }

    public DynamicQuery<T> addSortDescriptor(Function<T, Object> getPropertyFunc, SortDirection sortDirection) {
        SortDescriptor sortDescriptor = new SortDescriptor(this.entityClass, getPropertyFunc, sortDirection);
        return addSorts(sortDescriptor);
    }

    public DynamicQuery<T> selectProperty(Function<T, Object> getPropertyFunc) {
        String propertyPath = CommonsHelper.getPropertyName(entityClass, getPropertyFunc);
        String[] newSelectFields = ArrayUtils.add(this.selectProperties, propertyPath);
        this.setSelectProperties(newSelectFields);
        return this;
    }
}