package com.zhisland.finalmybatis.query.expression.builder.factory;

import com.zhisland.finalmybatis.query.Joint;
import com.zhisland.finalmybatis.query.Operator;
import com.zhisland.finalmybatis.query.expression.Expression;
import com.zhisland.finalmybatis.query.expression.ValueExpression;


public class ValueExpressionFactory implements ExpressionFactory {

	@Override
	public Expression buildExpression(Joint joint, String columnName, Operator operator, Object value) {
		return new ValueExpression(joint.getJoint(), columnName, operator.getOperator(), value);
	}

}
