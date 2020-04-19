package com.github.wz2cool.dynamic.mybatis.mapper.helper;

import com.github.wz2cool.dynamic.*;
import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.lambda.GetLongPropertyFunction;
import com.github.wz2cool.dynamic.model.LogicPagingResult;

import java.util.*;

/**
 * @author Frank
 **/
public final class LogicPagingHelper {

    private LogicPagingHelper() {
    }

    public static <T> Map.Entry<SortDescriptor, FilterDescriptor> getPagingSortFilterMap(
            GetLongPropertyFunction<T> pagingPropertyFunc, SortDirection sortDirection, Long startPageId, Long endPageId, UpDown upDown) {
        String propertyName = CommonsHelper.getPropertyName(pagingPropertyFunc);
        SortDescriptor sortDescriptor = new SortDescriptor();
        sortDescriptor.setPropertyName(propertyName);
        sortDescriptor.setDirection(sortDirection);
        Map.Entry<SortDescriptor, FilterDescriptor> resultMap = new AbstractMap.SimpleEntry<>(sortDescriptor, null);
        if (Objects.isNull(startPageId) && Objects.isNull(endPageId)) {
            return resultMap;
        }
        UpDown useUpDown = UpDown.NONE.equals(upDown) ? UpDown.DOWN : upDown;
        if (UpDown.DOWN.equals(useUpDown) && SortDirection.ASC.equals(sortDirection)) {
            if (Objects.isNull(endPageId)) {
                return resultMap;
            }
            FilterDescriptor filterDescriptor = new FilterDescriptor();
            filterDescriptor.setPropertyName(propertyName);
            filterDescriptor.setOperator(FilterOperator.GREATER_THAN);
            filterDescriptor.setValue(endPageId);
            resultMap.setValue(filterDescriptor);
            return resultMap;
        }
        if (UpDown.DOWN.equals(useUpDown) && SortDirection.DESC.equals(sortDirection)) {
            if (Objects.isNull(endPageId)) {
                return resultMap;
            }
            FilterDescriptor filterDescriptor = new FilterDescriptor();
            filterDescriptor.setPropertyName(propertyName);
            filterDescriptor.setOperator(FilterOperator.LESS_THAN);
            filterDescriptor.setValue(endPageId);
            resultMap.setValue(filterDescriptor);
            return resultMap;
        }
        if (UpDown.UP.equals(useUpDown) && SortDirection.ASC.equals(sortDirection)) {
            if (Objects.isNull(startPageId)) {
                return resultMap;
            }
            FilterDescriptor filterDescriptor = new FilterDescriptor();
            filterDescriptor.setPropertyName(propertyName);
            filterDescriptor.setOperator(FilterOperator.LESS_THAN);
            filterDescriptor.setValue(startPageId);
            // need change direction
            resultMap.getKey().setDirection(SortDirection.DESC);
            resultMap.setValue(filterDescriptor);
            return resultMap;
        }
        if (UpDown.UP.equals(useUpDown) && SortDirection.DESC.equals(sortDirection)) {
            if (Objects.isNull(startPageId)) {
                return resultMap;
            }
            FilterDescriptor filterDescriptor = new FilterDescriptor();
            filterDescriptor.setPropertyName(propertyName);
            filterDescriptor.setOperator(FilterOperator.GREATER_THAN);
            filterDescriptor.setValue(startPageId);
            // need change direction
            resultMap.getKey().setDirection(SortDirection.ASC);
            resultMap.setValue(filterDescriptor);
            return resultMap;
        }
        return resultMap;
    }

    public static <T> Optional<LogicPagingResult<T>> getPagingResult(
            GetLongPropertyFunction<T> pagingPropertyFunc, List<T> dataList, int pageSize, UpDown upDown) {
        int dataSize = dataList.size();
        boolean hasNextPage;
        boolean hasPreviousPage;
        if (UpDown.NONE.equals(upDown)) {
            hasNextPage = dataSize > pageSize;
            hasPreviousPage = false;
        } else if (UpDown.DOWN.equals(upDown)) {
            hasNextPage = dataSize > pageSize;
            hasPreviousPage = true;
        } else {
            if (dataSize < pageSize) {
                return Optional.empty();
            }
            if (dataSize > pageSize) {
                hasPreviousPage = true;
                hasNextPage = true;
            } else {
                hasPreviousPage = false;
                hasNextPage = true;
            }
        }
        Long startPageId = 0L;
        Long endPageId = 0L;
        List<T> pagingDataList = getLogicPagingData(dataList, pageSize, upDown);
        if (!pagingDataList.isEmpty()) {
            startPageId = pagingPropertyFunc.apply(pagingDataList.get(0));
            endPageId = pagingPropertyFunc.apply(pagingDataList.get(pagingDataList.size() - 1));
        }
        LogicPagingResult<T> logicPagingResult = new LogicPagingResult<>();
        logicPagingResult.setHasNextPage(hasNextPage);
        logicPagingResult.setHasPreviousPage(hasPreviousPage);
        logicPagingResult.setStartPageId(startPageId);
        logicPagingResult.setEndPageId(endPageId);
        logicPagingResult.setPageSize(pageSize);
        logicPagingResult.setList(pagingDataList);
        return Optional.of(logicPagingResult);
    }

    private static <T> List<T> getLogicPagingData(List<T> dataList, int pageSize, UpDown upDown) {
        if (dataList.isEmpty()) {
            return new ArrayList<>();
        }
        List<T> result;
        if (dataList.size() <= pageSize) {
            result = new ArrayList<>(dataList);
        } else {
            if (UpDown.UP == upDown) {
                result = dataList.subList(1, dataList.size());
            } else {
                result = dataList.subList(0, dataList.size() - 1);
            }
        }
        return result;
    }
}
