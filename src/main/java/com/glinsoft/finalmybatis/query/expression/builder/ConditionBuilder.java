package com.glinsoft.finalmybatis.query.expression.builder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.glinsoft.finalmybatis.exception.QueryException;
import com.glinsoft.finalmybatis.ext.code.util.FieldUtil;
import com.glinsoft.finalmybatis.query.annotation.Condition;
import com.glinsoft.finalmybatis.query.expression.Expressions;
import com.glinsoft.finalmybatis.query.expression.Expression;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/**
 * 条件生成
 */
public class ConditionBuilder {
	private static final String PREFIX_GET = "get";
	private static final String GETCLASS_NAME = "getClass";

	private static ConditionBuilder underlineFieldBuilder = new ConditionBuilder(true);
	private static ConditionBuilder camelFieldBuilder = new ConditionBuilder(false);

	private boolean camel2underline = Boolean.TRUE;

	public ConditionBuilder() {
		super();
	}

	public ConditionBuilder(boolean camel2underline) {
		super();
		this.camel2underline = camel2underline;
	}

	public static ConditionBuilder getUnderlineFieldBuilder() {
		return underlineFieldBuilder;
	}

	public static ConditionBuilder getCamelFieldBuilder() {
		return camelFieldBuilder;
	}

	/**
	 * 获取条件表达式
	 * 
	 * @param pojo pojo对象
	 * @return 返回表达式结合
	 */
	public List<Expression> buildExpressions(Object pojo) {
	    Assert.notNull(pojo, "buildExpressions(Object pojo) pojo can't be null.");
		List<Expression> expList = new ArrayList<Expression>();
		Class<?> clazz = pojo.getClass();
		Method[] methods = clazz.getMethods();
		try {
			for (Method method : methods) {
				if (couldBuildExpression(method)) {
					Object value = method.invoke(pojo);
					if (value == null) {
						continue;
					}
					Expression expression = buildExpression(method, value);
					if (expression != null) {
						expList.add(expression);
					}
				}
			}
		} catch (Exception e) {
			throw new QueryException(e);
		}
		return expList;
	}

	private Expression buildExpression(Method method, Object value) {
		String columnName = this.buildColumnName(method);
		Condition annotation = this.findCondition(method, columnName);
		Expression expression = null;

		if (annotation == null) {
			if (value.getClass().isArray()) {
				expression = Expressions.in(columnName, (Object[]) value);
			} else if (value instanceof Collection) {
				expression = Expressions.in(columnName, (Collection<?>) value);
			} else {
				expression = Expressions.eq(columnName, value);
			}
		} else {
			expression = ExpressionBuilder.buildExpression(annotation, columnName, value);
		}

		return expression;
	}
	
	private Condition findCondition(Method method, String columnName) {
		Condition annotation = method.getAnnotation(Condition.class);
		if(annotation == null) {
			Class<?> clazz = method.getDeclaringClass();
			Field field = ReflectionUtils.findField(clazz, columnName);
			if(field != null) {
				annotation = field.getAnnotation(Condition.class);
			}
		}
		return annotation;
	}
	
	/** 返回数据库字段名 */
	private String buildColumnName(Method method) {
		String getMethodName = method.getName();
		String columnName = getMethodName.substring(3);
		columnName = FieldUtil.lowerFirstLetter(columnName);
		if (camel2underline) {
			return FieldUtil.camelToUnderline(columnName);
		} else {
			return columnName;
		}
	}

	/** 能否构建表达式 */
	private static boolean couldBuildExpression(Method method) {
	    if(method.getReturnType() == Void.TYPE) {
	        return false;
	    }
	    String methodName = method.getName();
		return (!GETCLASS_NAME.equals(methodName)) && methodName.startsWith(PREFIX_GET);
	}

}
