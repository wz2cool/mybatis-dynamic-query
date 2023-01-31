package com.github.wz2cool.dynamic.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

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

    private static final NormPagingResult EMPTY_NORM_PAGING_RESULT = new NormPagingResult<>();

    /**
     * 构建空的normPagingResult
     *
     * @param <T> 分页泛型
     * @return 空的normPagingResult
     */
    public static <T> NormPagingResult<T> empty() {
        return EMPTY_NORM_PAGING_RESULT;
    }


    /**
     * 转换 NormPagingResult 的数据泛型
     *
     * @param mapper 数据类型映射关系
     * @param <O>    目标数据泛型
     * @return 目标数据泛型的 {@link NormPagingResult}
     */
    public <O> NormPagingResult<O> convert(Function<? super T, ? extends O> mapper) {
        List<O> collect = this.getList().stream().map(mapper).collect(toList());
        return copyPageResult(this, collect);
    }

    /**
     * copy page result property and set list is collect
     *
     * @param sourcePageResult 源 {@link NormPagingResult}
     * @param collect          重新设置的数据内容
     * @param <O>              目标数据泛型
     * @return copy 后的目标 {@link NormPagingResult}
     */
    private <O> NormPagingResult<O> copyPageResult(NormPagingResult<T> sourcePageResult, List<O> collect) {
        NormPagingResult<O> objectPageResult = new NormPagingResult<>();
        objectPageResult.setPageNum(sourcePageResult.getPageNum());
        objectPageResult.setPageSize(sourcePageResult.getPageSize());
        objectPageResult.setHasPreviousPage(sourcePageResult.isHasPreviousPage());
        objectPageResult.setHasNextPage(sourcePageResult.isHasNextPage());
        objectPageResult.setPages(sourcePageResult.getPages());
        objectPageResult.setTotal(sourcePageResult.getTotal());
        objectPageResult.setList(collect);
        return objectPageResult;
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
