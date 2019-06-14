package com.github.finalmybatis.query.expression;

import com.github.finalmybatis.SQLConsts;


public abstract class AbstractLikeExpression extends ValueExpression {

	public AbstractLikeExpression(String column, Object value) {
		super(column, value);
	}

	public AbstractLikeExpression(String joint, String column, Object value) {
		super(joint, column, SQLConsts.LIKE, value);
	}

	@Override
	public String getEqual() {
		return SQLConsts.LIKE;
	}

}