package com.glinsoft.finalmybatis.query.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.glinsoft.finalmybatis.query.Joint;
import com.glinsoft.finalmybatis.query.Operator;

/**
 * 条件表达式,作用在bean的get方法上
 * 
 */
@Documented
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Condition {
	/**
	 * 表达式之间的连接符,AND|OR,默认AND
	 * @return 默认AND
	 */
	Joint joint() default Joint.AND;

	/**
	 * 数据库字段名
	 * 
	 * @return 默认""
	 */
	String column() default "";

	/**
	 * 连接符
	 * 
	 * @return 返回连接符
	 */
	Operator operator() default Operator.nil;
	
	/**
     * 是否忽略，设置true，@Condition将不起作用
     * 
     * @return 返回true，@Condition将不起作用
     */
	boolean ignore() default false;
	
	/**
	 * 是否忽略空字符串，设置true，忽略空字符串的字段
	 *
	 * @return 返回true，空字符串字段不生成条件
	 */
	boolean ignoreEmptyString() default false;
}
