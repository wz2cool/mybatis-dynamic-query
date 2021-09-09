package com.github.wz2cool.dynamic;

/**
 * @param <T>
 */
public class NormPagingQuery<T> extends DynamicQuery<T> {

    private final int pageIndex;
    private final int pageSize;
    private final boolean calcTotal;
    private boolean autoFillIfEmpty;

    public int getPageIndex() {
        return pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public boolean isCalcTotal() {
        return calcTotal;
    }

    public boolean isAutoFillIfEmpty() {
        return autoFillIfEmpty;
    }

    public void setAutoFillIfEmpty(boolean autoFillIfEmpty) {
        this.autoFillIfEmpty = autoFillIfEmpty;
    }

    private NormPagingQuery(Class<T> clazz, int pageIndex, int pageSize, boolean autoFillIfEmpty, boolean calcTotal) {
        super(clazz);
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.autoFillIfEmpty = autoFillIfEmpty;
        this.calcTotal = calcTotal;
    }

    public static <T> NormPagingQuery<T> createQuery(
            Class<T> clazz, int pageIndex, int pageSize, boolean autoBackIfEmpty, boolean calcTotal) {
        return new NormPagingQuery<>(clazz, pageIndex, pageSize, autoBackIfEmpty, calcTotal);
    }

    public static <T> NormPagingQuery<T> createQuery(
            Class<T> clazz, int pageIndex, boolean autoBackIfEmpty, int pageSize) {
        return new NormPagingQuery<>(clazz, pageIndex, pageSize, autoBackIfEmpty, true);
    }
}
