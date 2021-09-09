package com.github.wz2cool.dynamic;

/**
 * @author Frank
 */
public class NormPagingQuery<T> extends DynamicQuery<T> {

    private final int pageIndex;
    private final int pageSize;
    private final boolean calcTotal;
    private boolean autoBackIfEmpty;

    public int getPageIndex() {
        return pageIndex;
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

    public void setAutoBackIfEmpty(boolean autoBackIfEmpty) {
        this.autoBackIfEmpty = autoBackIfEmpty;
    }

    private NormPagingQuery(Class<T> clazz, int pageIndex, int pageSize, boolean autoBackIfEmpty, boolean calcTotal) {
        super(clazz);
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.autoBackIfEmpty = autoBackIfEmpty;
        this.calcTotal = calcTotal;
    }

    public static <T> NormPagingQuery<T> createQuery(
            Class<T> clazz, int pageIndex, int pageSize, boolean autoBackIfEmpty, boolean calcTotal) {
        return new NormPagingQuery<>(clazz, pageIndex, pageSize, autoBackIfEmpty, calcTotal);
    }

    public static <T> NormPagingQuery<T> createQuery(
            Class<T> clazz, int pageIndex, int pageSize, boolean autoBackIfEmpty) {
        return new NormPagingQuery<>(clazz, pageIndex, pageSize, autoBackIfEmpty, true);
    }
}
