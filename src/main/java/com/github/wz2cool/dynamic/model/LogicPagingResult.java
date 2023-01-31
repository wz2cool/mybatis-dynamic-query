package com.github.wz2cool.dynamic.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Frank
 **/
public class LogicPagingResult<T> {
    private boolean hasPreviousPage;
    private boolean hasNextPage;
    private int pageSize;
    private long startPageId;
    private long endPageId;
    private List<T> list = new ArrayList<>();

    private static final LogicPagingResult EMPTY_LOGIC_PAGING_RESULT = new LogicPagingResult<>();

    /**
     * 构建空的LogicPagingResult
     *
     * @param <T> 分页泛型
     * @return 空的LogicPagingResult
     */
    public static <T> LogicPagingResult<T> empty() {
        return EMPTY_LOGIC_PAGING_RESULT;
    }

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

    public long getStartPageId() {
        return startPageId;
    }

    public void setStartPageId(long startPageId) {
        this.startPageId = startPageId;
    }

    public long getEndPageId() {
        return endPageId;
    }

    public void setEndPageId(long endPageId) {
        this.endPageId = endPageId;
    }

    public List<T> getList() {
        return list == null ? new ArrayList<>() : new ArrayList<>(list);
    }

    public void setList(List<T> list) {
        this.list = list == null ? new ArrayList<>() : new ArrayList<>(list);
    }
}
