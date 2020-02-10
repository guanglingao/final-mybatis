package com.glinsoft.finalmybatis.query.param;


public interface QuerySortableParam extends QueryParam {
	/**返回排序字段
	 * @return 返回排序字段
	 */
	String getSortname();
	/**返回排序字段
	 * @return 返回排序字段
	 */
	String getSortorder();
	/**
	 * 数据库排序字段
	 * @return 返回数据库排序字段
	 */
	String getDBSortname();
}
