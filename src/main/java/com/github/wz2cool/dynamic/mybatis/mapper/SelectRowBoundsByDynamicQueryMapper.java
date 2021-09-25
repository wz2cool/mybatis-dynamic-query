package com.github.wz2cool.dynamic.mybatis.mapper;

import com.github.wz2cool.dynamic.*;
import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.model.LogicPagingResult;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import com.github.wz2cool.dynamic.mybatis.mapper.helper.LogicPagingHelper;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.DynamicQueryProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.session.RowBounds;


import java.util.*;

/**
 * @author Frank
 */
public interface SelectRowBoundsByDynamicQueryMapper<T> {

    /**
     * select row rounds by dynamic query.
     *
     * @param dynamicQuery dynamic query
     * @param rowBounds    row bounds
     * @return the list of items
     */
    @SelectProvider(type = DynamicQueryProvider.class, method = "dynamicSQL")
    List<T> selectRowBoundsByDynamicQuery(
            @Param(MapperConstants.DYNAMIC_QUERY) DynamicQuery<T> dynamicQuery,
            RowBounds rowBounds);

    /**
     * select first record by dynamic query
     *
     * @param dynamicQuery dynamic query
     * @return matched first record
     */
    default Optional<T> selectFirstByDynamicQuery(DynamicQuery<T> dynamicQuery) {
        RowBounds rowBounds = new RowBounds(0, 1);
        List<T> result = selectRowBoundsByDynamicQuery(dynamicQuery, rowBounds);
        if (result == null || result.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(result.get(0));
        }
    }

    /**
     * select by logic paging
     *
     * @param logicPagingQuery logic paging query
     * @return logic paging result
     */
    default LogicPagingResult<T> selectByLogicPaging(LogicPagingQuery<T> logicPagingQuery) {
        int pageSize = logicPagingQuery.getPageSize();
        int queryPageSize = pageSize + 1;
        DynamicQuery<T> dynamicQuery = DynamicQuery.createQuery(logicPagingQuery.getClazz());
        dynamicQuery.addFilters(logicPagingQuery.getFilters());
        dynamicQuery.setDistinct(logicPagingQuery.isDistinct());
        dynamicQuery.setSelectedProperties(logicPagingQuery.getSelectedProperties());
        dynamicQuery.setIgnoredProperties(logicPagingQuery.getIgnoredProperties());
        Map.Entry<SortDescriptor, FilterDescriptor> mapEntry = LogicPagingHelper.getPagingSortFilterMap(
                logicPagingQuery.getPagingPropertyFunc(),
                logicPagingQuery.getSortDescriptor().getDirection(),
                logicPagingQuery.getLastStartPageId(),
                logicPagingQuery.getLastEndPageId(),
                logicPagingQuery.getUpDown());
        // 加上主要排序
        dynamicQuery.addSorts(mapEntry.getKey());
        // 紧跟上次要排序
        dynamicQuery.addSorts(logicPagingQuery.getSorts());
        if (Objects.nonNull(mapEntry.getValue())) {
            dynamicQuery.addFilters(mapEntry.getValue());
        }
        List<T> dataList = selectRowBoundsByDynamicQuery(dynamicQuery, new RowBounds(0, queryPageSize));
        if (!logicPagingQuery.getSortDirection().equals(mapEntry.getKey().getDirection())) {
            Collections.reverse(dataList);
        }
        Optional<LogicPagingResult<T>> logicPagingResultOptional = LogicPagingHelper.getPagingResult(
                logicPagingQuery.getPagingPropertyFunc(),
                dataList, logicPagingQuery.getPageSize(), logicPagingQuery.getUpDown());
        if (logicPagingResultOptional.isPresent()) {
            return logicPagingResultOptional.get();
        }
        LogicPagingQuery<T> resetPagingQuery = LogicPagingQuery.createQuery(
                logicPagingQuery.getClazz(),
                logicPagingQuery.getPagingPropertyFunc(),
                logicPagingQuery.getSortDirection(),
                UpDown.NONE);
        resetPagingQuery.setPageSize(logicPagingQuery.getPageSize());
        resetPagingQuery.setFilters(logicPagingQuery.getFilters());
        return selectByLogicPaging(resetPagingQuery);
    }
}