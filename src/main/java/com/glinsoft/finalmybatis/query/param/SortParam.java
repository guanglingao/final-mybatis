package com.glinsoft.finalmybatis.query.param;

import com.glinsoft.finalmybatis.query.annotation.Condition;
import com.glinsoft.finalmybatis.query.Query;

/**
 * 排序查询参数
 */
@SuppressWarnings("serial")
public class SortParam extends BaseParam implements QuerySortableParam {
	/** 排序字段，数据库字段 */
	private String sort;
	/** 排序排序方式，asc或desc */
	private String order;
	
	@Override
	public Query toQuery() {
		return super.toQuery().addSortInfo(this);
	}

	@Condition(ignore = true)
	@Override
	public String getSortname() {
		return sort;
	}

	@Condition(ignore = true)
	@Override
	public String getSortorder() {
		return order;
	}

	@Condition(ignore = true)
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	@Condition(ignore = true)
	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	@Condition(ignore = true)
	@Override
	public String getDBSortname() {
		return this.sort;
	}

}
