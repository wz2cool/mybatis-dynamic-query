package com.github.wz2cool.dynamic;

/**
 * @author Frank
 */
public class NormPagingQuery<T> extends BaseDynamicQuery<T, NormPagingQuery<T>> {

    private final int pageNum;
    private final int pageSize;
    private final boolean calcTotal;
    private boolean autoBackIfEmpty;

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

    public void setAutoBackIfEmpty(boolean autoBackIfEmpty) {
        this.autoBackIfEmpty = autoBackIfEmpty;
    }

    private NormPagingQuery(Class<T> clazz, int pageNum, int pageSize, boolean autoBackIfEmpty, boolean calcTotal) {
        this.setEntityClass(clazz);
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.autoBackIfEmpty = autoBackIfEmpty;
        this.calcTotal = calcTotal;
    }

    public static <T> NormPagingQuery<T> createQuery(
            Class<T> clazz, int pageNum, int pageSize, boolean autoBackIfEmpty, boolean calcTotal) {
        return new NormPagingQuery<>(clazz, pageNum, pageSize, autoBackIfEmpty, calcTotal);
    }

    public static <T> NormPagingQuery<T> createQuery(
            Class<T> clazz, int pageNum, int pageSize, boolean autoBackIfEmpty) {
        return new NormPagingQuery<>(clazz, pageNum, pageSize, autoBackIfEmpty, true);
    }

    public static <T> NormPagingQuery<T> createQuery(Class<T> clazz, int pageNum, int pageSize) {
        return new NormPagingQuery<>(clazz, pageNum, pageSize, false, true);
    }

    public DynamicQuery<T> getDynamicQuery() {
        DynamicQuery<T> dynamicQuery = DynamicQuery.createQuery(getEntityClass());
        dynamicQuery.addFilters(this.getFilters());
        dynamicQuery.addSorts(this.getSorts());
        dynamicQuery.setDistinct(this.isDistinct());
        dynamicQuery.setSelectedProperties(this.getSelectedProperties());
        dynamicQuery.setIgnoredProperties(this.getIgnoredProperties());
        dynamicQuery.customDynamicQueryParams.putAll(this.customDynamicQueryParams);
        return dynamicQuery;
    }
}
