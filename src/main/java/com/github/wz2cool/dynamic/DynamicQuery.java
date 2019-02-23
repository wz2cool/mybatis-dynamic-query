package com.github.wz2cool.dynamic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.lambda.GetPropertyFunction;
import com.github.wz2cool.dynamic.mybatis.ParamExpression;
import com.github.wz2cool.dynamic.mybatis.QueryHelper;
import org.apache.commons.lang3.ArrayUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("squid:S1948")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DynamicQuery<T> implements Serializable {
    private static final long serialVersionUID = -4044703018297658438L;
    private static final QueryHelper queryHelper = new QueryHelper();
    private static final String COLUMN_EXPRESSION_PLACEHOLDER = "columnsExpression";
    private static final String WHERE_EXPRESSION_PLACEHOLDER = "whereExpression";
    private static final String SORT_EXPRESSION_PLACEHOLDER = "orderByExpression";

    private boolean distinct;
    private boolean mapUnderscoreToCamelCase;
    private Class<T> entityClass;
    private FilterDescriptorBase[] filters = new FilterDescriptorBase[]{};
    private SortDescriptorBase[] sorts = new SortDescriptor[]{};
    private String[] selectedProperties = new String[]{};
    private String[] ignoredProperties = new String[]{};

    public DynamicQuery() {
        // for json
    }

    public DynamicQuery(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public static <T> DynamicQuery<T> createQuery(Class<T> entityClass) {
        return new DynamicQuery<>(entityClass);
    }

    public boolean isMapUnderscoreToCamelCase() {
        return mapUnderscoreToCamelCase;
    }

    public void setMapUnderscoreToCamelCase(boolean mapUnderscoreToCamelCase) {
        this.mapUnderscoreToCamelCase = mapUnderscoreToCamelCase;
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

    public String[] getSelectedProperties() {
        return selectedProperties;
    }

    public void setSelectedProperties(String[] selectedProperties) {
        this.selectedProperties = selectedProperties;
    }

    public String[] getIgnoredProperties() {
        return ignoredProperties;
    }

    public void setIgnoredProperties(String[] ignoredProperties) {
        this.ignoredProperties = ignoredProperties;
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
            FilterCondition condition, GetPropertyFunction<T> getPropertyFunc,
            FilterOperator operator, Object value) {
        FilterDescriptor filterDescriptor = new FilterDescriptor(condition, getPropertyFunc, operator, value);
        return addFilter(filterDescriptor);
    }

    public DynamicQuery<T> addFilterDescriptor(
            GetPropertyFunction<T> getPropertyFunc,
            FilterOperator operator, Object value) {
        FilterDescriptor filterDescriptor = new FilterDescriptor(FilterCondition.AND, getPropertyFunc, operator, value);
        return addFilter(filterDescriptor);
    }

    public DynamicQuery<T> addSortDescriptor(GetPropertyFunction<T> getPropertyFunc, SortDirection sortDirection) {
        SortDescriptor sortDescriptor = new SortDescriptor(getPropertyFunc, sortDirection);
        return addSorts(sortDescriptor);
    }

    public DynamicQuery<T> selectProperty(GetPropertyFunction<T> getPropertyFunc) {
        String propertyPath = CommonsHelper.getPropertyInfo(getPropertyFunc).getPropertyName();
        String[] newSelectProperties = ArrayUtils.add(this.selectedProperties, propertyPath);
        this.setSelectedProperties(newSelectProperties);
        return this;
    }

    public DynamicQuery<T> ignoreProperty(GetPropertyFunction<T> getPropertyFunc) {
        String propertyPath = CommonsHelper.getPropertyInfo(getPropertyFunc).getPropertyName();
        String[] newIgnoreProperties = ArrayUtils.add(this.ignoredProperties, propertyPath);
        this.setIgnoredProperties(newIgnoreProperties);
        return this;
    }

    public String getSelectColumnsExpression() {
        return queryHelper.toSelectColumnsExpression(
                this.entityClass, this.selectedProperties, this.ignoredProperties, this.mapUnderscoreToCamelCase);
    }

    public ParamExpression getWhereExpression() {
        return queryHelper.toWhereExpression(this.entityClass, this.filters);
    }

    public ParamExpression getSortExpression() {
        return queryHelper.toSortExpression(this.entityClass, this.sorts);
    }

    public Map<String, Object> toQueryParamMap() {
        Map<String, Object> result = new HashMap<>();
        String selectColumnsExpression = getSelectColumnsExpression();
        result.put(COLUMN_EXPRESSION_PLACEHOLDER, selectColumnsExpression);

        if (ArrayUtils.isNotEmpty(this.filters)) {
            ParamExpression whereExpression = getWhereExpression();
            String whereString = String.format("WHERE %s ", whereExpression.getExpression());
            result.put(WHERE_EXPRESSION_PLACEHOLDER, whereString);
            result.putAll(whereExpression.getParamMap());
        } else {
            result.put(WHERE_EXPRESSION_PLACEHOLDER, "");
        }

        if (ArrayUtils.isNotEmpty(this.sorts)) {
            ParamExpression sortExpression = getSortExpression();
            String sortString = String.format("ORDER BY %s ", sortExpression.getExpression());
            result.put(SORT_EXPRESSION_PLACEHOLDER, sortString);
            result.putAll(sortExpression.getParamMap());
        } else {
            result.put(SORT_EXPRESSION_PLACEHOLDER, "");
        }

        return result;
    }
}