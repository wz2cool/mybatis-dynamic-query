package com.github.wz2cool.dynamic;

import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.lambda.GetLongPropertyFunction;

/**
 * @author Frank
 * @date 2020/04/11
 **/
public class LogicPagingQuery<T> extends BaseFilterGroup<T, DynamicQuery<T>> {

    private final SortDescriptor sortDescriptor;
    private final Class<T> clazz;
    private final GetLongPropertyFunction<T> pagingPropertyFunc;
    private final SortDirection sortDirection;
    private final UpDown upDown;
    private int pageSize = 10;
    private Long lastStartPageId;
    private Long lastEndPageId;

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

    public SortDescriptor getSortDescriptor() {
        return sortDescriptor;
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
}
