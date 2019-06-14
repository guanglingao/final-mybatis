package com.github.finalmybatis.support;

import com.github.finalmybatis.query.annotation.Condition;
import com.github.finalmybatis.query.param.PageSortParam;

/**
 * 支持easyui表格参数
 */
@SuppressWarnings("serial")
public class EasyuiDatagridParam extends PageSortParam {
    private int page;
    private int rows;

    @Condition(ignore = true)
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
        this.setPageIndex(page);
    }

    @Condition(ignore = true)
    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
        this.setPageSize(rows);
    }

}