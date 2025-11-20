package com.github.wz2cool.dynamic;

import com.github.wz2cool.dynamic.builder.direction.ISortDirection;
import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.helper.ParamResolverHelper;
import com.github.wz2cool.dynamic.lambda.GetCommonPropertyFunction;
import com.github.wz2cool.dynamic.lambda.GetPropertyFunction;
import com.github.wz2cool.dynamic.mybatis.ParamExpression;
import com.github.wz2cool.dynamic.mybatis.QueryHelper;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class BaseDynamicQuery<T, S extends BaseFilterGroup<T, S>> extends BaseFilterGroup<T, S> {

    private static final QueryHelper QUERY_HELPER = new QueryHelper();

    private static final String FIRST_SQL_KEY = "mdq_first_sql";
    private static final String LAST_SQL_KEY = "mdq_last_sql";
    private static final String HINT_SQL_KEY = "mdq_hint_sql";

    private String[] selectedProperties = new String[]{};
    private String[] ignoredProperties = new String[]{};
    private BaseSortDescriptor[] sorts = new BaseSortDescriptor[]{};

    protected Map<String, Object> customDynamicQueryParams = new HashMap<>();

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

    public Map<String, Object> getCustomDynamicQueryParams() {
        return customDynamicQueryParams;
    }

    public void setCustomDynamicQueryParams(Map<String, Object> customDynamicQueryParams) {
        this.customDynamicQueryParams = customDynamicQueryParams;
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

    public final S last(String lastSql) {
        return last(true, lastSql);
    }

    public final S last(boolean enable, String lastSql) {
        if (enable) {
            String useLastSql = ParamResolverHelper.resolveExpression(lastSql);
            this.customDynamicQueryParams.put(LAST_SQL_KEY, useLastSql);
        }
        return (S) this;
    }

    public final S first(String firstSql) {
        return first(true, firstSql);
    }

    public final S first(boolean enable, String firstSql) {
        if (enable) {
            String useFirstSql = ParamResolverHelper.resolveExpression(firstSql);
            this.customDynamicQueryParams.put(FIRST_SQL_KEY, useFirstSql);
        }
        return (S) this;
    }

    public final S hint(String hintSql) {
        return hint(true, hintSql);
    }

    /**
     * https://docs.oracle.com/cd/B13789_01/server.101/b10759/sql_elements006.htm#i35922
     *
     * @return
     */
    public final S hint(boolean enable, String hintSql) {
        if (enable) {
            String useHintSql = ParamResolverHelper.resolveExpression(hintSql);
            this.customDynamicQueryParams.put(HINT_SQL_KEY, useHintSql);
        }
        return (S) this;
    }

    public final S queryParam(String key, Object value) {
        return queryParam(true, key, value);
    }

    public final S queryParam(boolean enable, String key, Object value) {
        if (enable) {
            this.customDynamicQueryParams.put(key, value);
        }
        return (S) this;
    }

    public final S queryParam(final Map<String, Object> paramMap) {
        return queryParam(true, paramMap);
    }

    public final S queryParam(boolean enable, final Map<String, Object> paramMap) {
        if (enable) {
            this.customDynamicQueryParams.putAll(paramMap);
        }
        return (S) this;
    }


    public Map<String, Object> toQueryParamMap() {
        return toQueryParamMap(false);
    }

    public Map<String, Object> toQueryParamMap(boolean isMapUnderscoreToCamelCase) {
        Class<?> entityClass = this.getEntityClass();
        String[] selectedProperties = this.getSelectedProperties();
        String[] ignoredProperties = this.getIgnoredProperties();

        ParamExpression whereParamExpression = toWhereExpression();
        String whereExpression = whereParamExpression.getExpression();
        Map<String, Object> paramMap = whereParamExpression.getParamMap();
        paramMap.put(MapperConstants.WHERE_EXPRESSION, whereExpression);

        ParamExpression sortExpression = toSortExpression();
        paramMap.put(MapperConstants.SORT_EXPRESSION, sortExpression.getExpression());
        paramMap.put(MapperConstants.DISTINCT, this.isDistinct());
        String selectColumnExpression = QUERY_HELPER.toSelectColumnsExpression(
                entityClass, selectedProperties, ignoredProperties, isMapUnderscoreToCamelCase, false);
        String unAsSelectColumnsExpression = QUERY_HELPER.toSelectColumnsExpression(
                entityClass, selectedProperties, ignoredProperties, isMapUnderscoreToCamelCase, true);
        paramMap.put(MapperConstants.SELECT_COLUMNS_EXPRESSION, selectColumnExpression);
        paramMap.put(MapperConstants.UN_AS_SELECT_COLUMNS_EXPRESSION, unAsSelectColumnsExpression);
        initDefaultQueryParams();
        paramMap.putAll(this.customDynamicQueryParams);
        return paramMap;
    }

    public ParamExpression toWhereExpression() {
        Class<?> entityClass = this.getEntityClass();
        return QUERY_HELPER.toWhereExpression(
                MapperConstants.DYNAMIC_QUERY_PARAMS, entityClass, this.getFilters());
    }

    public ParamExpression toSortExpression() {
        Class<?> entityClass = this.getEntityClass();
        return QUERY_HELPER.toSortExpression(entityClass, this.getSorts());
    }

    private void initDefaultQueryParams() {
        this.customDynamicQueryParams.putIfAbsent(LAST_SQL_KEY, "");
        this.customDynamicQueryParams.putIfAbsent(FIRST_SQL_KEY, "");
        this.customDynamicQueryParams.putIfAbsent(HINT_SQL_KEY, "");
    }
}
