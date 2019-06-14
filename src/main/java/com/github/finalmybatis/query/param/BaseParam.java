package com.github.finalmybatis.query.param;

import java.io.Serializable;

import com.github.finalmybatis.query.Query;


@SuppressWarnings("serial")
public class BaseParam implements Serializable {

	/** 
	 * 生成Query查询对象
	 * @return 返回查询对象
	 */
	public Query toQuery() {
		return new Query().addAnnotionExpression(this);
	}
}
