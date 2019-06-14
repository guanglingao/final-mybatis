package com.zhisland.finalmybatis.query;

import java.util.Map;

import com.zhisland.finalmybatis.query.expression.Expressional;


public interface Queryable extends Sortable, Expressional, Pageable {
    /**
     * 返回自定义参数，在xml中使用<code>#{参数名}</code>获取值
     * 
     * @return 返回自定义参数
     */
    Map<String, Object> getParam();

}
