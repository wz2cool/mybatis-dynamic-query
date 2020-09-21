package com.github.wz2cool.dynamic;

import com.github.wz2cool.dynamic.builder.direction.ISortDirection;
import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.lambda.GetCommonPropertyFunction;
import com.github.wz2cool.dynamic.lambda.GetLongPropertyFunction;
import com.github.wz2cool.dynamic.lambda.GetPropertyFunction;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Frank
 **/
public class LogicPagingQuery<T> extends BaseFilterGroup<T, LogicPagingQuery<T>> {

    private final SortDescriptor sortDescriptor;
    private final Class<T> clazz;
    private final GetLongPropertyFunction<T> pagingPropertyFunc;
    private final SortDirection sortDirection;
    private final UpDown upDown;
    private int pageSize = 10;
    private Long lastStartPageId;
    private Long lastEndPageId;

    private String[] selectedProperties = new String[]{};
    private String[] ignoredProperties = new String[]{};
    private BaseSortDescriptor[] sorts = new BaseSortDescriptor[]{};
    private boolean distinct;

    private LogicPagingQuery(Class<T> clazz, GetLongPropertyFunction<T> pagingPropertyFunc, SortDirection sortDirection, UpDown upDown) {
        this.clazz = clazz;
        this.upDown = upDown;
        this.pagingPropertyFunc = pagingPropertyFunc;
        this.sortDirection = sortDirection;
        String propertyName = CommonsHelper.getPropertyName(pagingPropertyFunc);
        sortDescriptor = new SortDescriptor();
        sortDescriptor.setPropertyName(propertyName);
        sortDescriptor.setDirection(sortDirection);
    }

    public static <T> LogicPagingQuery<T> createQuery(
            Class<T> clazz, GetLongPropertyFunction<T> pagingPropertyFunc, SortDirection sortDirection, UpDown upDown) {
        return new LogicPagingQuery<>(clazz, pagingPropertyFunc, sortDirection, upDown);
    }

    public LogicPagingQuery<T> thenBy(GetPropertyFunction<T, Comparable> getPropertyFunc, ISortDirection sortDirection) {
        return thenBy(true, getPropertyFunc, sortDirection);
    }

    public LogicPagingQuery<T> thenBy(boolean enable, GetPropertyFunction<T, Comparable> getPropertyFunc, ISortDirection sortDirection) {
        if (enable) {
            String propertyName = CommonsHelper.getPropertyName(getPropertyFunc);
            SortDirection direction = sortDirection.getDirection();
            SortDescriptor sortDescriptor = new SortDescriptor();
            sortDescriptor.setPropertyName(propertyName);
            sortDescriptor.setDirection(direction);
            addSorts(sortDescriptor);
        }
        return this;
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

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public SortDescriptor getSortDescriptor() {
        return sortDescriptor;
    }

    public BaseSortDescriptor[] getSorts() {
        return sorts;
    }

    public void setSorts(BaseSortDescriptor[] sorts) {
        this.sorts = sorts;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public GetLongPropertyFunction<T> getPagingPropertyFunc() {
        return pagingPropertyFunc;
    }

    public SortDirection getSortDirection() {
        return sortDirection;
    }

    public UpDown getUpDown() {
        return upDown;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Long getLastStartPageId() {
        return lastStartPageId;
    }

    public void setLastStartPageId(Long lastStartPageId) {
        this.lastStartPageId = lastStartPageId;
    }

    public Long getLastEndPageId() {
        return lastEndPageId;
    }

    public void setLastEndPageId(Long lastEndPageId) {
        this.lastEndPageId = lastEndPageId;
    }

    public void addSelectedProperties(String... newSelectedProperties) {
        setSelectedProperties(ArrayUtils.addAll(selectedProperties, newSelectedProperties));
    }

    public void ignoreSelectedProperties(String... newIgnoreProperties) {
        setIgnoredProperties(ArrayUtils.addAll(ignoredProperties, newIgnoreProperties));
    }

    @SafeVarargs
    public final LogicPagingQuery<T> select(GetCommonPropertyFunction<T>... getPropertyFunctions) {
        String[] newSelectProperties = new String[getPropertyFunctions.length];
        for (int i = 0; i < getPropertyFunctions.length; i++) {
            newSelectProperties[i] = CommonsHelper.getPropertyName(getPropertyFunctions[i]);
        }
        this.addSelectedProperties(newSelectProperties);
        return this;
    }

    @SafeVarargs
    public final LogicPagingQuery<T> ignore(GetCommonPropertyFunction<T>... getPropertyFunctions) {
        String[] newIgnoreProperties = new String[getPropertyFunctions.length];
        for (int i = 0; i < getPropertyFunctions.length; i++) {
            newIgnoreProperties[i] = CommonsHelper.getPropertyName(getPropertyFunctions[i]);
        }
        this.ignoreSelectedProperties(newIgnoreProperties);
        return this;
    }

    public void addSorts(BaseSortDescriptor... newSorts) {
        setSorts(ArrayUtils.addAll(sorts, newSorts));
    }
}
