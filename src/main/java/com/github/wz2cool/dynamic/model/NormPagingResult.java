package com.github.wz2cool.dynamic.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Frank
 **/
public class NormPagingResult<T> {
    /**
     * 是否有上一页
     */
    private boolean hasPreviousPage;
    /**
     * 是否有下一页
     */
    private boolean hasNextPage;
    /**
     * 每页数量
     */
    private int pageSize;
    /**
     * 页码
     */
    private int pageNum;
    /**
     * 总页数
     */
    private int pages;
    /**
     * 总记录数
     */
    private long total;
    /**
     * 结果集
     */
    private List<T> list = new ArrayList<>();

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getList() {
        return list == null ? new ArrayList<>() : new ArrayList<>(list);
    }

    public void setList(List<T> list) {
        this.list = list == null ? new ArrayList<>() : new ArrayList<>(list);
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
