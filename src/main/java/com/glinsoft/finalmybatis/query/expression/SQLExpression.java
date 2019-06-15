package com.glinsoft.finalmybatis.query.expression;

import com.glinsoft.finalmybatis.SQLConsts;

/**
 * 拼接SQL语句
 * 
 *
 */
public class SQLExpression implements ExpressionSqlable {

	private String joint = SQLConsts.AND;
	private String sql;

	public SQLExpression(String sql) {
		this.sql = sql;
	}
	
	public SQLExpression(String joint, String sql) {
		this.joint = joint;
		this.sql = sql;
	}

	@Override
	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	@Override
	public String getJoint() {
		return joint;
	}

	public void setJoint(String joint) {
		this.joint = joint;
	}
	
}
