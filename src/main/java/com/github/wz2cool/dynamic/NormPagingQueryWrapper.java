package com.github.wz2cool.dynamic;

/**
 * @author Frank
 **/
public class NormPagingQueryWrapper<T, S extends BaseFilterGroup<T, S>> {

    private final S searchQuery;
    private int pageNum = 1;
    private int pageSize = 10;
    private boolean calcTotal = true;
    private boolean autoBackIfEmpty = false;

    public S getSearchQuery() {
        return searchQuery;
    }

    public int getPageNum() {
        return pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public boolean isCalcTotal() {
        return calcTotal;
    }

    public boolean isAutoBackIfEmpty() {
        return autoBackIfEmpty;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setCalcTotal(boolean calcTotal) {
        this.calcTotal = calcTotal;
    }

    public void setAutoBackIfEmpty(boolean autoBackIfEmpty) {
        this.autoBackIfEmpty = autoBackIfEmpty;
    }

    public NormPagingQueryWrapper(S searchQuery) {
        this.searchQuery = searchQuery;
    }

    public static <T, S extends BaseFilterGroup<T, S>> NormPagingQueryWrapper<T, S> create(S searchQuery) {
        return new NormPagingQueryWrapper<>(searchQuery);
    }

    public static <T, S extends BaseFilterGroup<T, S>> NormPagingQueryWrapper<T, S> create(S searchQuery, int pageNum, int pageSize) {
        return create(searchQuery, pageNum, pageSize, true);
    }

    public static <T, S extends BaseFilterGroup<T, S>> NormPagingQueryWrapper<T, S> create(
            S searchQuery, int pageNum, int pageSize, boolean calcTotal) {
        return create(searchQuery, pageNum, pageSize, calcTotal, false);
    }

    public static <T, S extends BaseFilterGroup<T, S>> NormPagingQueryWrapper<T, S> create(
            S searchQuery, int pageNum, int pageSize, boolean calcTotal, boolean autoBackIfEmpty) {
        final NormPagingQueryWrapper<T, S> normPagingQueryWrapper = new NormPagingQueryWrapper<>(searchQuery);
        normPagingQueryWrapper.setPageNum(pageNum);
        normPagingQueryWrapper.setPageSize(pageSize);
        normPagingQueryWrapper.setCalcTotal(calcTotal);
        normPagingQueryWrapper.setAutoBackIfEmpty(autoBackIfEmpty);
        return normPagingQueryWrapper;
    }
}
