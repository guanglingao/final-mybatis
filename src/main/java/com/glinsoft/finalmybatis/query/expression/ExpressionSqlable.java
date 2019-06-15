package com.glinsoft.finalmybatis.query.expression;


public interface ExpressionSqlable extends Expression {
    /**
     * 返回连接符
     * 
     * @return 返回连接符
     */
    String getJoint();

    /**
     * 返回sql
     * 
     * @return 返回sql
     */
    String getSql();
}
