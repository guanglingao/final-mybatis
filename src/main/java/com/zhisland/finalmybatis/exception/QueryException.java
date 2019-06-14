package com.zhisland.finalmybatis.exception;

/**
 * 条件构建异常
 *
 *
 */
@SuppressWarnings("serial")
public class QueryException extends RuntimeException {

	public QueryException(String message) {
		super(message);
	}

	public QueryException(Throwable cause) {
		super(cause);
	}

}
