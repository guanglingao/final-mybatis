package com.glinsoft.finalmybatis.query.expression;


public interface ExpressionJoinable extends Expression {
    /**
     * 返回连接sql
     * 
     * @return 返回连接sql
     */
    String getJoinSql();
}
