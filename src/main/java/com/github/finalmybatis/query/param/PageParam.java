package com.github.finalmybatis.query.param;

import com.github.finalmybatis.query.annotation.Condition;
import com.github.finalmybatis.query.Query;

/**
 * 分页查询参数
 * 
 *
 */
@SuppressWarnings("serial")
public class PageParam extends BaseParam implements QueryPageableParam {
	
	/** 当前第几页 */
	private int pageIndex = 1;
	/** 每页记录数 */
	private int pageSize = 20;

	@Override
	public Query toQuery() {
		return super.toQuery().addPaginationInfo(this);
	}

	@Condition(ignore = true)
	@Override
	public int getStart() {
		return (int) ((this.getPageIndex() - 1) * this.getPageSize());
	}

	@Condition(ignore = true)
	@Override
	public int getLimit() {
		return this.getPageSize();
	}

	@Condition(ignore = true)
	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	@Condition(ignore = true)
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
