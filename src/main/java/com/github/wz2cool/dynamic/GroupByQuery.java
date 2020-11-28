package com.github.wz2cool.dynamic;

import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.lambda.GetCommonPropertyFunction;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Frank
 * @date 2020/11/28
 **/
public class GroupByQuery<TQuery, TSelect> extends BaseFilterGroup<TQuery, GroupByQuery<TQuery, TSelect>> {

    final Class<TQuery> tQueryClass;
    final Class<TSelect> tSelectClass;

    /// region select properties

    private String[] selectedProperties = new String[]{};
    private String[] ignoredProperties = new String[]{};

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
}
