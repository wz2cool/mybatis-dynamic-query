package com.github.wz2cool.dynamic;

import com.github.wz2cool.dynamic.builder.direction.ISortDirection;
import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.lambda.GetPropertyFunction;
import com.github.wz2cool.dynamic.mybatis.ParamExpression;
import com.github.wz2cool.dynamic.mybatis.QueryHelper;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Frank
 * @date 2020/11/28
 **/
public class GroupedQuery<TQuery, TSelect> extends BaseFilterGroup<TSelect, GroupedQuery<TQuery, TSelect>> {

    private static final QueryHelper QUERY_HELPER = new QueryHelper();
    private static final String COLUMN_EXPRESSION_PLACEHOLDER = "columnsExpression";
    private static final String WHERE_EXPRESSION_PLACEHOLDER = "whereExpression";
    private static final String GROUP_BY_EXPRESSION_PLACEHOLDER = "groupByExpression";
    private static final String HAVING_EXPRESSION_PLACEHOLDER = "havingExpression";
    private static final String SORT_EXPRESSION_PLACEHOLDER = "orderByExpression";

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

    public Map<String, Object> toQueryParamMap() {
        Map<String, Object> result = new HashMap<>(16);
        String selectColumnsExpression = getSelectColumnsExpression();
        result.put(COLUMN_EXPRESSION_PLACEHOLDER, selectColumnsExpression);
        // 查询筛选
        if (ArrayUtils.isNotEmpty(this.groupByQuery.getFilters())) {
            ParamExpression whereExpression = getWhereExpression();
            String whereString = String.format("WHERE %s ", whereExpression.getExpression());
            result.put(WHERE_EXPRESSION_PLACEHOLDER, whereString);
            result.putAll(whereExpression.getParamMap());
        } else {
            result.put(WHERE_EXPRESSION_PLACEHOLDER, "");
        }
        // 分组
        if (ArrayUtils.isNotEmpty(this.groupByQuery.getGroupedProperties())) {
            String groupByExpression = getGroupByExpression();
            String groupByString = String.format("GROUP BY %s ", groupByExpression);
            result.put(GROUP_BY_EXPRESSION_PLACEHOLDER, groupByString);
        } else {
            result.put(GROUP_BY_EXPRESSION_PLACEHOLDER, "");
        }
        // having 查询
        if (ArrayUtils.isNotEmpty(this.getFilters())) {
            ParamExpression havingExpression = getHavingExpression();
            String havingString = String.format("HAVING %s ", havingExpression.getExpression());
            result.put(HAVING_EXPRESSION_PLACEHOLDER, havingString);
            result.putAll(havingExpression.getParamMap());
        } else {
            result.put(HAVING_EXPRESSION_PLACEHOLDER, "");
        }
        // 排序
        if (ArrayUtils.isNotEmpty(this.sorts)) {
            ParamExpression sortExpression = getSortExpression();
            String sortString = String.format("ORDER BY %s ", sortExpression.getExpression());
            result.put(SORT_EXPRESSION_PLACEHOLDER, sortString);
            result.putAll(sortExpression.getParamMap());
        } else {
            result.put(SORT_EXPRESSION_PLACEHOLDER, "");
        }

        return result;
    }

    private String getSelectColumnsExpression() {
        return QUERY_HELPER.toSelectColumnsExpression(
                this.groupByQuery.tSelectClass,
                this.groupByQuery.getSelectedProperties(),
                this.groupByQuery.getIgnoredProperties(),
                false);
    }

    private ParamExpression getWhereExpression() {
        return QUERY_HELPER.toWhereExpression(this.groupByQuery.tQueryClass, this.groupByQuery.getFilters());
    }

    private String getGroupByExpression() {
        return QUERY_HELPER.toGroupByColumnsExpression(
                this.groupByQuery.tQueryClass, this.groupByQuery.getGroupedProperties());
    }

    private ParamExpression getHavingExpression() {
        return QUERY_HELPER.toWhereExpression(this.groupByQuery.tSelectClass, this.getFilters());
    }

    private ParamExpression getSortExpression() {
        return QUERY_HELPER.toSortExpression(this.groupByQuery.tSelectClass, this.sorts);
    }
}
