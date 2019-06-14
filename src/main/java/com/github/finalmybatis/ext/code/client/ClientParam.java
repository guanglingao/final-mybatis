package com.github.finalmybatis.ext.code.client;

import com.github.finalmybatis.util.ClassUtil;
import com.github.finalmybatis.FinalmybatisConfig;

/**
 * 客户端参数
 */
public class ClientParam {
	private Class<?> mapperClass;
	/** 模板路径 */
	private String templateLocation;
	private String globalVmLocation;
	private FinalmybatisConfig config;

	public Class<?> getEntityClass() {
		if (mapperClass.isInterface()) {
			return ClassUtil.getSuperInterfaceGenricType(mapperClass, 0);
		} else {
			return ClassUtil.getSuperClassGenricType(mapperClass, 0);
		}
	}
	
	public String getGlobalVmPlaceholder() {
		return this.config.getGlobalVmPlaceholder();
	}

	public String getGlobalVmLocation() {
		return globalVmLocation;
	}

	public void setGlobalVmLocation(String globalVmLocation) {
		this.globalVmLocation = globalVmLocation;
	}

	public Class<?> getMapperClass() {
		return mapperClass;
	}

	public void setMapperClass(Class<?> mapperClass) {
		this.mapperClass = mapperClass;
	}

	public String getTemplateLocation() {
		return templateLocation;
	}

	public void setTemplateLocation(String templateLocation) {
		this.templateLocation = templateLocation;
	}

	public FinalmybatisConfig getConfig() {
		return config;
	}

	public void setConfig(FinalmybatisConfig config) {
		this.config = config;
	}

	public String getCountExpression() {
		return this.config.getCountExpression();
	}

}
