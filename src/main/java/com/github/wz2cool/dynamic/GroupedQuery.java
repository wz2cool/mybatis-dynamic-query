package com.github.wz2cool.dynamic;

import com.github.wz2cool.dynamic.builder.direction.ISortDirection;
import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.lambda.GetPropertyFunction;
import com.github.wz2cool.dynamic.mybatis.ParamExpression;
import com.github.wz2cool.dynamic.mybatis.QueryHelper;
import com.github.wz2cool.dynamic.mybatis.mapper.constant.MapperConstants;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Map;

/**
 * @author Frank
 * @date 2020/11/28
 **/
public class GroupedQuery<TQuery, TSelect> extends BaseFilterGroup<TSelect, GroupedQuery<TQuery, TSelect>> {

    private static final QueryHelper QUERY_HELPER = new QueryHelper();
    private final GroupByQuery<TQuery, TSelect> groupByQuery;

    public GroupedQuery(GroupByQuery<TQuery, TSelect> groupByQuery) {
        this.groupByQuery = groupByQuery;
    }

    public Class<TQuery> getQueryClass() {
        return this.groupByQuery.tQueryClass;
    }

    public Class<TSelect> getSelectClass() {
        return this.groupByQuery.tSelectClass;
    }

    /// region sort

    private BaseSortDescriptor[] sorts = new BaseSortDescriptor[]{};

    public BaseSortDescriptor[] getSorts() {
        return sorts;
    }

    public void setSorts(BaseSortDescriptor[] sorts) {
        this.sorts = sorts;
    }

    public void addSorts(BaseSortDescriptor... newSorts) {
        setSorts(ArrayUtils.addAll(sorts, newSorts));
    }

    public void removeSorts(BaseSortDescriptor... newSorts) {
        for (BaseSortDescriptor newSort : newSorts) {
            setSorts(ArrayUtils.removeAllOccurences(sorts, newSort));
        }
    }

    public GroupedQuery<TQuery, TSelect> orderBy(GetPropertyFunction<TSelect, Comparable> getPropertyFunc, ISortDirection sortDirection) {
        return orderBy(true, getPropertyFunc, sortDirection);
    }

    public GroupedQuery<TQuery, TSelect> orderBy(
            boolean enable, GetPropertyFunction<TSelect, Comparable> getPropertyFunc, ISortDirection sortDirection) {
        if (enable) {
            String propertyName = CommonsHelper.getPropertyName(getPropertyFunc);
            SortDirection direction = sortDirection.getDirection();
            SortDescriptor sortDescriptor = new SortDescriptor();
            sortDescriptor.setPropertyName(propertyName);
            sortDescriptor.setDirection(direction);
            addSorts(sortDescriptor);
        }
        return this;
    }

    /// endregion

    public Map<String, Object> toQueryParamMap(boolean isMapUnderscoreToCamelCase) {
        // 筛选
        ParamExpression whereParamExpression = QUERY_HELPER.toWhereExpression(
                this.getQueryClass(), this.groupByQuery.getFilters());
        String whereExpression = whereParamExpression.getExpression();
        Map<String, Object> paramMap = whereParamExpression.getParamMap();
        for (Map.Entry<String, Object> param : paramMap.entrySet()) {
            String key = param.getKey();
            String newKey = String.format("%s.%s", MapperConstants.GROUPED_QUERY_PARAMS, key);
            whereExpression = whereExpression.replace(key, newKey);
        }
        paramMap.put(MapperConstants.WHERE_EXPRESSION, whereExpression);
        // 分组
        String groupColumnExpression = QUERY_HELPER.toGroupByColumnsExpression(
                this.groupByQuery.tQueryClass, this.groupByQuery.getGroupedProperties());
        paramMap.put(MapperConstants.GROUP_COLUMNS_EXPRESSION, groupColumnExpression);
        // having
        ParamExpression havingParamExpression = QUERY_HELPER.toWhereExpression(
                this.getSelectClass(), this.getFilters());
        String havingExpression = havingParamExpression.getExpression();
        for (Map.Entry<String, Object> param : havingParamExpression.getParamMap().entrySet()) {
            String key = param.getKey();
            String newKey = String.format("%s.%s", MapperConstants.GROUPED_QUERY_PARAMS, key);
            havingExpression = havingExpression.replace(key, newKey);
        }
        paramMap.put(MapperConstants.HAVING_EXPRESSION, havingExpression);
        // 排序
        ParamExpression sortExpression = QUERY_HELPER.toSortExpression(this.getSelectClass(), sorts);
        paramMap.put(MapperConstants.SORT_EXPRESSION, sortExpression.getExpression());
        // 选择
        String selectColumnExpression = QUERY_HELPER.toSelectColumnsExpression(
                this.groupByQuery.tSelectClass,
                this.groupByQuery.getSelectedProperties(),
                this.groupByQuery.getIgnoredProperties(), isMapUnderscoreToCamelCase);
        paramMap.put(MapperConstants.SELECT_COLUMNS_EXPRESSION, selectColumnExpression);
        return paramMap;

    }
}
