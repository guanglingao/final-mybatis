package com.github.finalmybatis.query.param;

/**
 * 分页排序查询参数
 * 
 */
public interface QueryPageableParam extends QueryParam {
    /**
     * 返回第一条记录的索引值
     * 
     * @return 返回第一条记录的索引值
     */
    int getStart();

    /**
     * 返回第一条记录的索引值
     * 
     * @return 返回第一条记录的索引值
     */
    int getLimit();
}
