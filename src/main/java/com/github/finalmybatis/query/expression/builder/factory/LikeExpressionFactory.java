package com.github.finalmybatis.query.expression.builder.factory;

import com.github.finalmybatis.query.Joint;
import com.github.finalmybatis.query.Operator;
import com.github.finalmybatis.query.expression.Expression;
import com.github.finalmybatis.query.expression.ValueExpression;


public class LikeExpressionFactory implements ExpressionFactory {

	@Override
	public Expression buildExpression(Joint joint, String columnName, Operator operator, Object value) {
		return new ValueExpression(joint.getJoint(), columnName, operator.getOperator(), this.getValue(value));
	}

	protected String getValue(Object value) {
		return "%" + value + "%";
	}

}
