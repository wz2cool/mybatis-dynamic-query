package com.github.wz2cool.dynamic;

import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.helper.ParamResolverHelper;
import com.github.wz2cool.dynamic.lambda.GetCommonPropertyFunction;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Frank
 **/
public class GroupByQuery<TQuery, TSelect> extends BaseFilterGroup<TQuery, GroupByQuery<TQuery, TSelect>> {

    final Class<TQuery> tQueryClass;
    final Class<TSelect> tSelectClass;

    /// region select properties

    private static final String FIRST_SQL_KEY = "mdq_first_sql";
    private static final String LAST_SQL_KEY = "mdq_last_sql";
    private static final String HINT_SQL_KEY = "mdq_hint_sql";

    private String[] selectedProperties = new String[]{};
    private String[] ignoredProperties = new String[]{};

    private Map<String, Object> customDynamicQueryParams = new HashMap<>();

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

    @SafeVarargs
    public final GroupByQuery<TQuery, TSelect> select(GetCommonPropertyFunction<TSelect>... getPropertyFunctions) {
        String[] newSelectProperties = new String[getPropertyFunctions.length];
        for (int i = 0; i < getPropertyFunctions.length; i++) {
            newSelectProperties[i] = CommonsHelper.getPropertyName(getPropertyFunctions[i]);
        }
        this.addSelectedProperties(newSelectProperties);
        return this;
    }

    @SafeVarargs
    public final GroupByQuery<TQuery, TSelect> ignore(GetCommonPropertyFunction<TSelect>... getPropertyFunctions) {
        String[] newIgnoreProperties = new String[getPropertyFunctions.length];
        for (int i = 0; i < getPropertyFunctions.length; i++) {
            newIgnoreProperties[i] = CommonsHelper.getPropertyName(getPropertyFunctions[i]);
        }
        this.ignoreSelectedProperties(newIgnoreProperties);
        return this;
    }

    public final GroupByQuery<TQuery, TSelect> first(String firstSql) {
        return first(true, firstSql);
    }

    public final GroupByQuery<TQuery, TSelect> first(boolean enable, String firstSql) {
        if (enable) {
            String useFirstSql = ParamResolverHelper.resolveExpression(firstSql);
            this.customDynamicQueryParams.put(FIRST_SQL_KEY, useFirstSql);
        }
        return this;
    }

    public final GroupByQuery<TQuery, TSelect> hint(String hintSql) {
        return hint(true, hintSql);
    }

    /**
     * https://docs.oracle.com/cd/B13789_01/server.101/b10759/sql_elements006.htm#i35922
     *
     * @return
     */
    public final GroupByQuery<TQuery, TSelect> hint(boolean enable, String hintSql) {
        if (enable) {
            String useHintSql = ParamResolverHelper.resolveExpression(hintSql);
            this.customDynamicQueryParams.put(HINT_SQL_KEY, useHintSql);
        }
        return this;
    }

    public final GroupByQuery<TQuery, TSelect> queryParam(String key, Object value) {
        return queryParam(true, key, value);
    }

    public final GroupByQuery<TQuery, TSelect> queryParam(boolean enable, String key, Object value) {
        if (enable) {
            this.customDynamicQueryParams.put(key, value);
        }
        return this;
    }


    public void addSelectedProperties(String... newSelectedProperties) {
        setSelectedProperties(ArrayUtils.addAll(selectedProperties, newSelectedProperties));
    }

    public void ignoreSelectedProperties(String... newIgnoreProperties) {
        setIgnoredProperties(ArrayUtils.addAll(ignoredProperties, newIgnoreProperties));
    }

    /// endregion

    public GroupByQuery(Class<TQuery> tQueryClass, Class<TSelect> tSelectClass) {
        this.tQueryClass = tQueryClass;
        this.tSelectClass = tSelectClass;
    }

    public static <TQuery, TSelect> GroupByQuery<TQuery, TSelect> createQuery(
            Class<TQuery> tQueryClass, Class<TSelect> tSelectClass) {
        return new GroupByQuery<>(tQueryClass, tSelectClass);
    }

    /// region group

    private String[] groupedProperties = new String[]{};

    public String[] getGroupedProperties() {
        return groupedProperties;
    }

    public void setGroupedProperties(String[] groupedProperties) {
        this.groupedProperties = groupedProperties;
    }

    @SafeVarargs
    public final GroupedQuery<TQuery, TSelect> groupBy(GetCommonPropertyFunction<TQuery>... getPropertyFunctions) {
        String[] groupByProperties = new String[getPropertyFunctions.length];
        for (int i = 0; i < getPropertyFunctions.length; i++) {
            groupByProperties[i] = CommonsHelper.getPropertyName(getPropertyFunctions[i]);
        }
        this.groupedProperties = ArrayUtils.addAll(this.groupedProperties, groupByProperties);
        return new GroupedQuery<>(this);
    }

    /// endregion

    public void initDefaultQueryParams() {
        this.customDynamicQueryParams.putIfAbsent(LAST_SQL_KEY, "");
        this.customDynamicQueryParams.putIfAbsent(FIRST_SQL_KEY, "");
        this.customDynamicQueryParams.putIfAbsent(HINT_SQL_KEY, "");
    }

    public Map<String, Object> getCustomDynamicQueryParams() {
        return customDynamicQueryParams;
    }
}
