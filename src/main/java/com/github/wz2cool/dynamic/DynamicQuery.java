package com.github.wz2cool.dynamic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.lambda.GetPropertyFunction;
import com.github.wz2cool.dynamic.mybatis.ParamExpression;
import com.github.wz2cool.dynamic.mybatis.QueryHelper;
import org.apache.commons.lang3.ArrayUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Frank
 */
@SuppressWarnings("squid:S1948")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DynamicQuery<T> implements Serializable {
    private static final long serialVersionUID = -4044703018297658438L;
    private static final QueryHelper QUERY_HELPER = new QueryHelper();
    private static final String COLUMN_EXPRESSION_PLACEHOLDER = "columnsExpression";
    private static final String WHERE_EXPRESSION_PLACEHOLDER = "whereExpression";
    private static final String SORT_EXPRESSION_PLACEHOLDER = "orderByExpression";

    private boolean distinct;
    private Class<T> entityClass;
    private BaseFilterDescriptor[] filters = new BaseFilterDescriptor[]{};
    private BaseSortDescriptor[] sorts = new SortDescriptor[]{};
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

    public BaseFilterDescriptor[] getFilters() {
        return filters;
    }

    public void setFilters(BaseFilterDescriptor[] filters) {
        this.filters = filters;
    }

    public BaseSortDescriptor[] getSorts() {
        return sorts;
    }

    public void setSorts(BaseSortDescriptor[] sorts) {
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

    public DynamicQuery<T> addFilter(BaseFilterDescriptor newFilter) {
        return addFilters(newFilter);
    }

    @SuppressWarnings("Duplicates")
    public DynamicQuery<T> addFilters(BaseFilterDescriptor... newFilters) {
        BaseFilterDescriptor[] newAllFilters = ArrayUtils.addAll(this.filters, newFilters);
        this.setFilters(newAllFilters);
        return this;
    }

    @SuppressWarnings("Duplicates")
    public DynamicQuery<T> removeFilter(BaseFilterDescriptor removeFilter) {
        BaseFilterDescriptor[] newAllFilters = ArrayUtils.removeAllOccurences(this.filters, removeFilter);
        this.setFilters(newAllFilters);
        return this;
    }


    public DynamicQuery<T> addSorts(BaseSortDescriptor... newSorts) {
        BaseSortDescriptor[] newAllSorts = ArrayUtils.addAll(this.sorts, newSorts);
        this.setSorts(newAllSorts);
        return this;
    }

    public DynamicQuery<T> removeSort(BaseSortDescriptor sort) {
        BaseSortDescriptor[] newAllSorts = ArrayUtils.removeAllOccurences(this.sorts, sort);
        this.setSorts(newAllSorts);
        return this;
    }

    public DynamicQuery<T> filter(
            GetPropertyFunction<T, Long> getPropertyFunc,
            FilterOperator operator, Long filterValue) {
        return filterInternal(getPropertyFunc, operator, filterValue);
    }

    public DynamicQuery<T> filter(
            GetPropertyFunction<T, Byte> getPropertyFunc,
            FilterOperator operator, Byte filterValue) {
        return filterInternal(getPropertyFunc, operator, filterValue);
    }

    public DynamicQuery<T> filter(
            GetPropertyFunction<T, Short> getPropertyFunc,
            FilterOperator operator, Short filterValue) {
        return filterInternal(getPropertyFunc, operator, filterValue);
    }

    public DynamicQuery<T> filter(
            GetPropertyFunction<T, Integer> getPropertyFunc,
            FilterOperator operator, Integer filterValue) {
        return filterInternal(getPropertyFunc, operator, filterValue);
    }

    public DynamicQuery<T> filter(
            GetPropertyFunction<T, Float> getPropertyFunc,
            FilterOperator operator, Float filterValue) {
        return filterInternal(getPropertyFunc, operator, filterValue);
    }

    public DynamicQuery<T> filter(
            GetPropertyFunction<T, Double> getPropertyFunc,
            FilterOperator operator, Double filterValue) {
        return filterInternal(getPropertyFunc, operator, filterValue);
    }

    public DynamicQuery<T> filter(
            GetPropertyFunction<T, BigDecimal> getPropertyFunc,
            FilterOperator operator, BigDecimal filterValue) {
        return filterInternal(getPropertyFunc, operator, filterValue);
    }

    public DynamicQuery<T> filter(
            GetPropertyFunction<T, String> getPropertyFunc,
            FilterOperator operator, String filterValue) {
        return filterInternal(getPropertyFunc, operator, filterValue);
    }

    public DynamicQuery<T> filter(
            GetPropertyFunction<T, java.util.Date> getPropertyFunc,
            FilterOperator operator, java.util.Date filterValue) {
        return filterInternal(getPropertyFunc, operator, filterValue);
    }

    public DynamicQuery<T> filter(
            FilterCondition condition,
            GetPropertyFunction<T, Long> getPropertyFunc,
            FilterOperator operator, Long filterValue) {
        return filterInternal(condition, getPropertyFunc, operator, filterValue);
    }

    public DynamicQuery<T> filter(
            FilterCondition condition,
            GetPropertyFunction<T, Byte> getPropertyFunc,
            FilterOperator operator, Byte filterValue) {
        return filterInternal(condition, getPropertyFunc, operator, filterValue);
    }

    public DynamicQuery<T> filter(
            FilterCondition condition,
            GetPropertyFunction<T, Short> getPropertyFunc,
            FilterOperator operator, Short filterValue) {
        return filterInternal(condition, getPropertyFunc, operator, filterValue);
    }

    public DynamicQuery<T> filter(
            FilterCondition condition,
            GetPropertyFunction<T, Integer> getPropertyFunc,
            FilterOperator operator, Integer filterValue) {
        return filterInternal(condition, getPropertyFunc, operator, filterValue);
    }

    public DynamicQuery<T> filter(
            FilterCondition condition,
            GetPropertyFunction<T, Float> getPropertyFunc,
            FilterOperator operator, Float filterValue) {
        return filterInternal(condition, getPropertyFunc, operator, filterValue);
    }

    public DynamicQuery<T> filter(
            FilterCondition condition,
            GetPropertyFunction<T, Double> getPropertyFunc,
            FilterOperator operator, Double filterValue) {
        return filterInternal(condition, getPropertyFunc, operator, filterValue);
    }

    public DynamicQuery<T> filter(
            FilterCondition condition,
            GetPropertyFunction<T, BigDecimal> getPropertyFunc,
            FilterOperator operator, BigDecimal filterValue) {
        return filterInternal(condition, getPropertyFunc, operator, filterValue);
    }

    public DynamicQuery<T> filter(
            FilterCondition condition,
            GetPropertyFunction<T, String> getPropertyFunc,
            FilterOperator operator, String filterValue) {
        return filterInternal(condition, getPropertyFunc, operator, filterValue);
    }

    public DynamicQuery<T> filter(
            FilterCondition condition,
            GetPropertyFunction<T, java.util.Date> getPropertyFunc,
            FilterOperator operator, java.util.Date filterValue) {
        return filterInternal(condition, getPropertyFunc, operator, filterValue);
    }

    public DynamicQuery<T> filter(
            GetPropertyFunction<T, Long> getPropertyFunc,
            FilterOperator operator, Long[] filterValue) {
        return filterInternal(getPropertyFunc, operator, filterValue);
    }

    public DynamicQuery<T> filter(
            GetPropertyFunction<T, Byte> getPropertyFunc,
            FilterOperator operator, Byte[] filterValue) {
        return filterInternal(getPropertyFunc, operator, filterValue);
    }

    public DynamicQuery<T> filter(
            GetPropertyFunction<T, Short> getPropertyFunc,
            FilterOperator operator, Short[] filterValue) {
        return filterInternal(getPropertyFunc, operator, filterValue);
    }

    public DynamicQuery<T> filter(
            GetPropertyFunction<T, Integer> getPropertyFunc,
            FilterOperator operator, Integer[] filterValue) {
        return filterInternal(getPropertyFunc, operator, filterValue);
    }

    public DynamicQuery<T> filter(
            GetPropertyFunction<T, Float> getPropertyFunc,
            FilterOperator operator, Float[] filterValue) {
        return filterInternal(getPropertyFunc, operator, filterValue);
    }

    public DynamicQuery<T> filter(
            GetPropertyFunction<T, Double> getPropertyFunc,
            FilterOperator operator, Double[] filterValue) {
        return filterInternal(getPropertyFunc, operator, filterValue);
    }

    public DynamicQuery<T> filter(
            GetPropertyFunction<T, BigDecimal> getPropertyFunc,
            FilterOperator operator, BigDecimal[] filterValue) {
        return filterInternal(getPropertyFunc, operator, filterValue);
    }

    public DynamicQuery<T> filter(
            GetPropertyFunction<T, String> getPropertyFunc,
            FilterOperator operator, String[] filterValue) {
        return filterInternal(getPropertyFunc, operator, filterValue);
    }

    public DynamicQuery<T> filter(
            GetPropertyFunction<T, java.util.Date> getPropertyFunc,
            FilterOperator operator, java.util.Date[] filterValue) {
        return filterInternal(getPropertyFunc, operator, filterValue);
    }

    public DynamicQuery<T> filter(
            FilterCondition condition,
            GetPropertyFunction<T, Long> getPropertyFunc,
            FilterOperator operator, Long[] filterValue) {
        return filterInternal(condition, getPropertyFunc, operator, filterValue);
    }

    public DynamicQuery<T> filter(
            FilterCondition condition,
            GetPropertyFunction<T, Byte> getPropertyFunc,
            FilterOperator operator, Byte[] filterValue) {
        return filterInternal(condition, getPropertyFunc, operator, filterValue);
    }

    public DynamicQuery<T> filter(
            FilterCondition condition,
            GetPropertyFunction<T, Short> getPropertyFunc,
            FilterOperator operator, Short[] filterValue) {
        return filterInternal(condition, getPropertyFunc, operator, filterValue);
    }

    public DynamicQuery<T> filter(
            FilterCondition condition,
            GetPropertyFunction<T, Integer> getPropertyFunc,
            FilterOperator operator, Integer[] filterValue) {
        return filterInternal(condition, getPropertyFunc, operator, filterValue);
    }

    public DynamicQuery<T> filter(
            FilterCondition condition,
            GetPropertyFunction<T, Float> getPropertyFunc,
            FilterOperator operator, Float[] filterValue) {
        return filterInternal(condition, getPropertyFunc, operator, filterValue);
    }

    public DynamicQuery<T> filter(
            FilterCondition condition,
            GetPropertyFunction<T, Double> getPropertyFunc,
            FilterOperator operator, Double[] filterValue) {
        return filterInternal(condition, getPropertyFunc, operator, filterValue);
    }

    public DynamicQuery<T> filter(
            FilterCondition condition,
            GetPropertyFunction<T, BigDecimal> getPropertyFunc,
            FilterOperator operator, BigDecimal[] filterValue) {
        return filterInternal(condition, getPropertyFunc, operator, filterValue);
    }

    public DynamicQuery<T> filter(
            FilterCondition condition,
            GetPropertyFunction<T, String> getPropertyFunc,
            FilterOperator operator, String[] filterValue) {
        return filterInternal(condition, getPropertyFunc, operator, filterValue);
    }

    public DynamicQuery<T> filter(
            FilterCondition condition,
            GetPropertyFunction<T, java.util.Date> getPropertyFunc,
            FilterOperator operator, java.util.Date[] filterValue) {
        return filterInternal(condition, getPropertyFunc, operator, filterValue);
    }

    private <R> DynamicQuery<T> filterInternal(
            GetPropertyFunction<T, R> getPropertyFunc,
            FilterOperator operator, R filterValue) {
        FilterDescriptor filterDescriptor = new FilterDescriptor(FilterCondition.AND, getPropertyFunc, operator, filterValue);
        return addFilter(filterDescriptor);
    }

    private <R> DynamicQuery<T> filterInternal(
            FilterCondition condition,
            GetPropertyFunction<T, R> getPropertyFunc,
            FilterOperator operator, R filterValue) {
        FilterDescriptor filterDescriptor = new FilterDescriptor(condition, getPropertyFunc, operator, filterValue);
        return addFilter(filterDescriptor);
    }

    private <R> DynamicQuery<T> filterInternal(
            GetPropertyFunction<T, R> getPropertyFunc,
            FilterOperator operator, R[] filterValue) {
        FilterDescriptor filterDescriptor = new FilterDescriptor(FilterCondition.AND, getPropertyFunc, operator, filterValue);
        return addFilter(filterDescriptor);
    }

    private <R> DynamicQuery<T> filterInternal(
            FilterCondition condition,
            GetPropertyFunction<T, R> getPropertyFunc,
            FilterOperator operator, R[] filterValue) {
        FilterDescriptor filterDescriptor = new FilterDescriptor(condition, getPropertyFunc, operator, filterValue);
        return addFilter(filterDescriptor);
    }

    public DynamicQuery<T> sort(GetPropertyFunction<T, Object> getPropertyFunc, SortDirection sortDirection) {
        SortDescriptor sortDescriptor = new SortDescriptor(getPropertyFunc, sortDirection);
        return addSorts(sortDescriptor);
    }

    public DynamicQuery<T> selectProperty(GetPropertyFunction<T, Object> getPropertyFunc) {
        String propertyPath = CommonsHelper.getPropertyInfo(getPropertyFunc).getPropertyName();
        String[] newSelectProperties = ArrayUtils.add(this.selectedProperties, propertyPath);
        this.setSelectedProperties(newSelectProperties);
        return this;
    }

    public DynamicQuery<T> ignoreProperty(GetPropertyFunction<T, Object> getPropertyFunc) {
        String propertyPath = CommonsHelper.getPropertyInfo(getPropertyFunc).getPropertyName();
        String[] newIgnoreProperties = ArrayUtils.add(this.ignoredProperties, propertyPath);
        this.setIgnoredProperties(newIgnoreProperties);
        return this;
    }

    public Map<String, Object> toQueryParamMap() {
        Map<String, Object> result = new HashMap<>(10);
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

    private String getSelectColumnsExpression() {
        return QUERY_HELPER.toSelectColumnsExpression(
                this.entityClass, this.selectedProperties, this.ignoredProperties,
                false);
    }

    private ParamExpression getWhereExpression() {
        return QUERY_HELPER.toWhereExpression(this.entityClass, this.filters);
    }

    private ParamExpression getSortExpression() {
        return QUERY_HELPER.toSortExpression(this.entityClass, this.sorts);
    }
}