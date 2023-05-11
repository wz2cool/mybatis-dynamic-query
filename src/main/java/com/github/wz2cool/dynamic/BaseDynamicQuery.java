package com.github.wz2cool.dynamic;

import com.github.wz2cool.dynamic.builder.direction.ISortDirection;
import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.lambda.GetCommonPropertyFunction;
import com.github.wz2cool.dynamic.lambda.GetPropertyFunction;
import com.github.wz2cool.dynamic.mybatis.ParamExpression;
import com.github.wz2cool.dynamic.mybatis.QueryHelper;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Map;
import java.util.function.Function;

public abstract class BaseDynamicQuery<T, S extends BaseFilterGroup<T, S>> extends BaseFilterGroup<T, S> {

    private static final QueryHelper QUERY_HELPER = new QueryHelper();

    private String[] selectedProperties = new String[]{};
    private String[] ignoredProperties = new String[]{};
    private BaseSortDescriptor[] sorts = new BaseSortDescriptor[]{};

    private boolean distinct;
    private Class<T> entityClass;

    public String[] getSelectedProperties() {
        return selectedProperties;
    }

    public void setSelectedProperties(String[] selectedProperties) {
        this.selectedProperties = selectedProperties;
    }

    public BaseSortDescriptor[] getSorts() {
        return sorts;
    }

    public void setSorts(BaseSortDescriptor[] sorts) {
        this.sorts = sorts;
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

    public String[] getIgnoredProperties() {
        return ignoredProperties;
    }

    public void setIgnoredProperties(String[] ignoredProperties) {
        this.ignoredProperties = ignoredProperties;
    }

    public void addSorts(BaseSortDescriptor... newSorts) {
        setSorts(ArrayUtils.addAll(sorts, newSorts));
    }

    public void removeSorts(BaseSortDescriptor... newSorts) {
        for (BaseSortDescriptor newSort : newSorts) {
            setSorts(ArrayUtils.removeAllOccurences(sorts, newSort));
        }
    }

    public void addSelectedProperties(String... newSelectedProperties) {
        setSelectedProperties(ArrayUtils.addAll(selectedProperties, newSelectedProperties));
    }

    public void ignoreSelectedProperties(String... newIgnoreProperties) {
        setIgnoredProperties(ArrayUtils.addAll(ignoredProperties, newIgnoreProperties));
    }

    public S orderBy(GetPropertyFunction<T, Comparable> getPropertyFunc, ISortDirection sortDirection) {
        return orderBy(true, getPropertyFunc, sortDirection);
    }

    public S orderBy(boolean enable, GetPropertyFunction<T, Comparable> getPropertyFunc, ISortDirection sortDirection) {
        if (enable) {
            String propertyName = CommonsHelper.getPropertyName(getPropertyFunc);
            SortDirection direction = sortDirection.getDirection();
            SortDescriptor sortDescriptor = new SortDescriptor();
            sortDescriptor.setPropertyName(propertyName);
            sortDescriptor.setDirection(direction);
            addSorts(sortDescriptor);
        }
        return (S) this;
    }

    private static final SortDirections SORT_DIRECTIONS = new SortDirections();

    public S orderBy(GetPropertyFunction<T, Comparable> getPropertyFunc, Function<SortDirections, ISortDirection> sortDirectionFunc) {
        return orderBy(true, getPropertyFunc, sortDirectionFunc);
    }

    public S orderBy(boolean enable, GetPropertyFunction<T, Comparable> getPropertyFunc,
                     Function<SortDirections, ISortDirection> sortDirectionFunc) {
        final ISortDirection sortDirection = sortDirectionFunc.apply(SORT_DIRECTIONS);
        if (enable) {
            String propertyName = CommonsHelper.getPropertyName(getPropertyFunc);
            SortDirection direction = sortDirection.getDirection();
            SortDescriptor sortDescriptor = new SortDescriptor();
            sortDescriptor.setPropertyName(propertyName);
            sortDescriptor.setDirection(direction);
            addSorts(sortDescriptor);
        }
        return (S) this;
    }

    public final S selectDistinct(GetCommonPropertyFunction<T> getPropertyFunction) {
        final String propertyName = CommonsHelper.getPropertyName(getPropertyFunction);
        this.addSelectedProperties(propertyName);
        this.setDistinct(true);
        return (S) this;
    }

    @SafeVarargs
    public final S select(GetCommonPropertyFunction<T>... getPropertyFunctions) {
        String[] newSelectProperties = new String[getPropertyFunctions.length];
        for (int i = 0; i < getPropertyFunctions.length; i++) {
            newSelectProperties[i] = CommonsHelper.getPropertyName(getPropertyFunctions[i]);
        }
        this.addSelectedProperties(newSelectProperties);
        return (S) this;
    }

    @SafeVarargs
    public final S ignore(GetCommonPropertyFunction<T>... getPropertyFunctions) {
        String[] newIgnoreProperties = new String[getPropertyFunctions.length];
        for (int i = 0; i < getPropertyFunctions.length; i++) {
            newIgnoreProperties[i] = CommonsHelper.getPropertyName(getPropertyFunctions[i]);
        }
        this.ignoreSelectedProperties(newIgnoreProperties);
        return (S) this;
    }

    public Map<String, Object> toQueryParamMap() {
        return toQueryParamMap(false);
    }

    public Map<String, Object> toQueryParamMap(boolean isMapUnderscoreToCamelCase) {
        Class<?> entityClass = this.getEntityClass();
        BaseFilterDescriptor[] filters = this.getFilters();
        BaseSortDescriptor[] sorts = this.getSorts();
        String[] selectedProperties = this.getSelectedProperties();
        String[] ignoredProperties = this.getIgnoredProperties();

        ParamExpression whereParamExpression = QUERY_HELPER.toWhereExpression(entityClass, filters);
        String whereExpression = whereParamExpression.getExpression();
        Map<String, Object> paramMap = whereParamExpression.getParamMap();
        for (Map.Entry<String, Object> param : paramMap.entrySet()) {
            String key = param.getKey();
            String newKey = String.format("%s.%s", MapperConstants.DYNAMIC_QUERY_PARAMS, key);
            whereExpression = whereExpression.replace(key, newKey);
        }
        paramMap.put(MapperConstants.WHERE_EXPRESSION, whereExpression);

        ParamExpression sortExpression = QUERY_HELPER.toSortExpression(entityClass, sorts);
        paramMap.put(MapperConstants.SORT_EXPRESSION, sortExpression.getExpression());
        paramMap.put(MapperConstants.DISTINCT, this.isDistinct());
        String selectColumnExpression = QUERY_HELPER.toSelectColumnsExpression(
                entityClass, selectedProperties, ignoredProperties, isMapUnderscoreToCamelCase,false);
        String unAsColumnsExpression = QUERY_HELPER.toSelectColumnsExpression(
                entityClass, selectedProperties, ignoredProperties, isMapUnderscoreToCamelCase,true);
        paramMap.put(MapperConstants.SELECT_COLUMNS_EXPRESSION, selectColumnExpression);
        paramMap.put(MapperConstants.UN_AS_COLUMNS_EXPRESSION, unAsColumnsExpression);
        return paramMap;
    }
}
