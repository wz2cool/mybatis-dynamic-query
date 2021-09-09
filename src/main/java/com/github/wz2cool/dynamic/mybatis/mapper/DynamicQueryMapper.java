package com.github.wz2cool.dynamic.mybatis.mapper;

import com.github.wz2cool.dynamic.NormPagingQuery;
import com.github.wz2cool.dynamic.model.NormPagingResult;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

import java.util.List;


/**
 * @author Frank
 */
@RegisterMapper
public interface DynamicQueryMapper<T> extends
        BaseMapper<T>,
        InsertListMapper<T>,
        SelectCountByDynamicQueryMapper<T>,
        DeleteByDynamicQueryMapper<T>,
        SelectByDynamicQueryMapper<T>,
        SelectRowBoundsByDynamicQueryMapper<T>,
        UpdateSelectiveByDynamicQueryMapper<T>,
        UpdateByDynamicQueryMapper<T>,
        SelectMaxByDynamicQueryMapper<T>,
        SelectMinByDynamicQueryMapper<T>,
        SelectSumByDynamicQueryMapper<T>,
        SelectAvgByDynamicQueryMapper<T>,
        UpdateByUpdateQueryMapper<T> {


    /**
     * select by norm paging
     *
     * @param normPagingQuery norm paging query
     * @return norm paging result
     */
    default NormPagingResult<T> selectByNormalPaging(NormPagingQuery<T> normPagingQuery) {
        NormPagingResult<T> result = new NormPagingResult<>();
        int pageIndex = normPagingQuery.getPageIndex();
        int pageSize = normPagingQuery.getPageSize();
        int queryPageSize = pageSize + 1;
        int offset = (pageIndex - 1) * pageSize;
        List<T> dataList = selectRowBoundsByDynamicQuery(normPagingQuery, new RowBounds(offset, queryPageSize));
        // 补偿当前页没有需要到上一页
        if (normPagingQuery.isAutoFillIfEmpty() && dataList.isEmpty() && pageIndex - 1 > 1) {
            int newPageIndex = pageIndex - 1;
            NormPagingQuery<T> newNormalPagingQuery = NormPagingQuery.createQuery(normPagingQuery.getEntityClass(),
                    newPageIndex, normPagingQuery.getPageSize(),
                    normPagingQuery.isAutoFillIfEmpty(), normPagingQuery.isCalcTotal());
            return selectByNormalPaging(newNormalPagingQuery);
        }
        if (normPagingQuery.isCalcTotal()) {
            int totalCount = selectCountByDynamicQuery(normPagingQuery);
            int pageNum = (int) Math.ceil((double) totalCount / pageSize);
            result.setTotalCount(totalCount);
            result.setPageNum(pageNum);
        }
        boolean hasNext = dataList.size() > pageSize;
        boolean hasPre = pageIndex > 1;
        result.setHasNextPage(hasNext);
        result.setHasPreviousPage(hasPre);
        if (dataList.size() > pageSize) {
            result.setList(dataList.subList(0, pageSize));
        } else {
            result.setList(dataList);
        }
        result.setPageIndex(normPagingQuery.getPageIndex());
        result.setPageSize(normPagingQuery.getPageSize());
        return result;
    }
}