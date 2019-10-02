package com.github.wz2cool.dynamic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.wz2cool.dynamic.builder.direction.ISortDirection;
import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.lambda.GetCommonPropertyFunction;
import com.github.wz2cool.dynamic.lambda.GetPropertyFunction;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Frank
 * @date 2019/10/01
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
public class DynamicQuery<T> extends BaseFilterGroup<T, DynamicQuery<T>> {

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

    public DynamicQuery() {
        // for json
    }

    public DynamicQuery(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public static <T> DynamicQuery<T> createQuery(Class<T> entityClass) {
        return new DynamicQuery<>(entityClass);
    }

    public void addSorts(BaseSortDescriptor... newSorts) {
        setSorts(ArrayUtils.addAll(sorts, newSorts));
    }

    public void removeSorts(BaseSortDescriptor... newSorts) {
        for (BaseSortDescriptor newSort : newSorts) {
            ArrayUtils.removeAllOccurences(sorts, newSort);
        }
    }

    public void addSelectedProperties(String... newSelectedProperties) {
        setSelectedProperties(ArrayUtils.addAll(selectedProperties, newSelectedProperties));
    }

    public void ignoreSelectedProperties(String... newIgnoreProperties) {
        setIgnoredProperties(ArrayUtils.addAll(ignoredProperties, newIgnoreProperties));
    }

    public DynamicQuery<T> orderBy(GetPropertyFunction<T, Comparable> getPropertyFunc, ISortDirection sortDirection) {
        String propertyName = CommonsHelper.getPropertyName(getPropertyFunc);
        SortDirection direction = sortDirection.getDirection();
        SortDescriptor sortDescriptor = new SortDescriptor();
        sortDescriptor.setPropertyName(propertyName);
        sortDescriptor.setDirection(direction);
        addSorts(sortDescriptor);
        return this;
    }

    @SafeVarargs
    public final DynamicQuery<T> select(GetCommonPropertyFunction<T>... getPropertyFunctions) {
        String[] newSelectProperties = new String[getPropertyFunctions.length];
        for (int i = 0; i < getPropertyFunctions.length; i++) {
            newSelectProperties[i] = CommonsHelper.getPropertyName(getPropertyFunctions[i]);
        }
        this.addSelectedProperties(newSelectProperties);
        return this;
    }

    @SafeVarargs
    public final DynamicQuery<T> ignore(GetCommonPropertyFunction<T>... getPropertyFunctions) {
        String[] newIgnoreProperties = new String[getPropertyFunctions.length];
        for (int i = 0; i < getPropertyFunctions.length; i++) {
            newIgnoreProperties[i] = CommonsHelper.getPropertyName(getPropertyFunctions[i]);
        }
        this.ignoreSelectedProperties(newIgnoreProperties);
        return this;
    }
}
