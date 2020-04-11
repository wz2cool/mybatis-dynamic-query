package com.github.wz2cool.dynamic.mybatis.mapper.helper;

import com.github.wz2cool.dynamic.*;
import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.lambda.GetLongPropertyFunction;
import com.github.wz2cool.dynamic.model.LogicPagingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Frank
 **/
public final class LogicPagingHelper {

    private LogicPagingHelper() {
    }

    public static <T> Optional<FilterDescriptor> getPagingFilter(
            GetLongPropertyFunction<T> pagingPropertyFunc, SortDirection sortDirection, Long startPageId, Long endPageId, UpDown upDown) {
        if (Objects.isNull(startPageId) && Objects.isNull(endPageId)) {
            return Optional.empty();
        }
        String propertyName = CommonsHelper.getPropertyName(pagingPropertyFunc);
        UpDown useUpDown = UpDown.NONE.equals(upDown) ? UpDown.DOWN : upDown;
        if (UpDown.DOWN.equals(useUpDown) && SortDirection.ASC.equals(sortDirection)) {
            if (Objects.isNull(endPageId)) {
                return Optional.empty();
            }
            FilterDescriptor filterDescriptor = new FilterDescriptor();
            filterDescriptor.setPropertyName(propertyName);
            filterDescriptor.setOperator(FilterOperator.GREATER_THAN);
            filterDescriptor.setValue(endPageId);
            return Optional.of(filterDescriptor);
        }
        if (UpDown.UP.equals(useUpDown) && SortDirection.ASC.equals(sortDirection)) {
            if (Objects.isNull(startPageId)) {
                return Optional.empty();
            }
            FilterDescriptor filterDescriptor = new FilterDescriptor();
            filterDescriptor.setPropertyName(propertyName);
            filterDescriptor.setOperator(FilterOperator.LESS_THAN);
            filterDescriptor.setValue(startPageId);
            return Optional.of(filterDescriptor);
        }
        if (UpDown.UP.equals(useUpDown) && SortDirection.DESC.equals(sortDirection)) {
            if (Objects.isNull(startPageId)) {
                return Optional.empty();
            }
            FilterDescriptor filterDescriptor = new FilterDescriptor();
            filterDescriptor.setPropertyName(propertyName);
            filterDescriptor.setOperator(FilterOperator.GREATER_THAN);
            filterDescriptor.setValue(startPageId);
            return Optional.of(filterDescriptor);
        }
        if (UpDown.DOWN.equals(useUpDown) && SortDirection.DESC.equals(sortDirection)) {
            if (Objects.isNull(endPageId)) {
                return Optional.empty();
            }
            FilterDescriptor filterDescriptor = new FilterDescriptor();
            filterDescriptor.setPropertyName(propertyName);
            filterDescriptor.setOperator(FilterOperator.LESS_THAN);
            filterDescriptor.setValue(endPageId);
            return Optional.of(filterDescriptor);
        }
        return Optional.empty();
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
        if (dataList.size() <= pageSize) {
            return new ArrayList<>(dataList);
        }
        List<T> result;
        if (UpDown.UP.equals(upDown)) {
            result = dataList.subList(1, dataList.size());
        } else {
            result = dataList.subList(0, dataList.size() - 1);
        }
        return result;
    }
}
