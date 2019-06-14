package com.zhisland.finalmybatis.ext.exception;

/**
 * 生成mapper代码异常
 *
 */
@SuppressWarnings("serial")
public class MapperFileException extends RuntimeException {
	public MapperFileException(Throwable cause) {
		super(cause);
	}

	public MapperFileException(String message) {
		super(message);
	}
	
}
