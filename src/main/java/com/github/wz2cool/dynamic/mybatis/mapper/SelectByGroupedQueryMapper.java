package com.github.wz2cool.dynamic.mybatis.mapper;

import com.github.wz2cool.dynamic.GroupedQuery;
import com.github.wz2cool.dynamic.NormPagingQueryWrapper;
import com.github.wz2cool.dynamic.model.NormPagingResult;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.GroupedQueryProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;
import java.util.Optional;

/**
 * @author Frank
 **/
@RegisterMapper
@SuppressWarnings("java:S119")
public interface SelectByGroupedQueryMapper<TQuery, TSelect> {

    /**
     * select by grouped query
     *
     * @param groupedQuery grouped query
     * @return list of item
     */
    @SelectProvider(type = GroupedQueryProvider.class, method = "dynamicSQL")
    List<TSelect> selectByGroupedQuery(@Param(MapperConstants.GROUPED_QUERY) GroupedQuery<TQuery, TSelect> groupedQuery);


    /**
     * select count by grouped query.
     *
     * @param groupedQuery grouped query
     * @return the count of items
     */
    @SelectProvider(type = GroupedQueryProvider.class, method = "dynamicSQL")
    int selectCountByGroupedQuery(@Param(MapperConstants.GROUPED_QUERY) GroupedQuery<TQuery, TSelect> groupedQuery);

    /**
     * select row rounds by grouped query.
     *
     * @param groupedQuery grouped query
     * @param rowBounds    row bounds
     * @return the list of items
     */
    @SelectProvider(type = GroupedQueryProvider.class, method = "dynamicSQL")
    List<TSelect> selectRowBoundsByGroupedQuery(
            @Param(MapperConstants.GROUPED_QUERY) GroupedQuery<TQuery, TSelect> groupedQuery,
            RowBounds rowBounds);

    /**
     * select first record by grouped query
     *
     * @param groupedQuery grouped query
     * @return matched first record
     */
    default Optional<TSelect> selectFirstByGroupedQuery(GroupedQuery<TQuery, TSelect> groupedQuery) {
        RowBounds rowBounds = new RowBounds(0, 1);
        List<TSelect> result = selectRowBoundsByGroupedQuery(groupedQuery, rowBounds);
        if (result == null || result.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(result.get(0));
        }
    }

    default NormPagingResult<TSelect> selectByNormalPaging(
            NormPagingQueryWrapper<TSelect, GroupedQuery<TQuery, TSelect>> normPagingQueryWrapper) {
        NormPagingResult<TSelect> result = new NormPagingResult<>();
        int pageNum = normPagingQueryWrapper.getPageNum() < 1 ? 1 : normPagingQueryWrapper.getPageNum();
        int pageSize = normPagingQueryWrapper.getPageSize();
        int queryPageSize = pageSize + 1;
        int offset = (pageNum - 1) * pageSize;
        List<TSelect> dataList = selectRowBoundsByGroupedQuery(normPagingQueryWrapper.getSearchQuery(), new RowBounds(offset, queryPageSize));
        // 补偿当前页没有需要到上一页
        if (normPagingQueryWrapper.isAutoBackIfEmpty() && dataList.isEmpty() && pageNum > 1) {
            int newPageNum = pageNum - 1;
            NormPagingQueryWrapper<TSelect, GroupedQuery<TQuery, TSelect>> newNormPagingQueryWrapper =
                    new NormPagingQueryWrapper<>(normPagingQueryWrapper.getSearchQuery());
            newNormPagingQueryWrapper.setPageNum(newPageNum);
            newNormPagingQueryWrapper.setPageSize(normPagingQueryWrapper.getPageSize());
            newNormPagingQueryWrapper.setAutoBackIfEmpty(normPagingQueryWrapper.isAutoBackIfEmpty());
            newNormPagingQueryWrapper.setCalcTotal(normPagingQueryWrapper.isCalcTotal());
            return selectByNormalPaging(newNormPagingQueryWrapper);
        }
        if (normPagingQueryWrapper.isCalcTotal()) {
            int totalCount = selectCountByGroupedQuery(normPagingQueryWrapper.getSearchQuery());
            int pages = (int) Math.ceil((double) totalCount / pageSize);
            result.setTotal(totalCount);
            result.setPages(pages);
        }
        boolean hasNext = dataList.size() > pageSize;
        boolean hasPre = pageNum > 1;
        result.setHasNextPage(hasNext);
        result.setHasPreviousPage(hasPre);
        if (dataList.size() > pageSize) {
            result.setList(dataList.subList(0, pageSize));
        } else {
            result.setList(dataList);
        }
        result.setPageNum(pageNum);
        result.setPageSize(normPagingQueryWrapper.getPageSize());
        return result;
    }

}
