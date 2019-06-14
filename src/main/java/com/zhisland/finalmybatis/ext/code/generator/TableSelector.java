package com.zhisland.finalmybatis.ext.code.generator;

import javax.persistence.Table;

import com.zhisland.finalmybatis.FinalmybatisConfig;
import com.zhisland.finalmybatis.ext.code.util.FieldUtil;

/**
 * 表选择
 */
public class TableSelector {

	private ColumnSelector columnSelector;
	private Class<?> entityClass;
	private FinalmybatisConfig config;

	public TableSelector(Class<?> entityClass, FinalmybatisConfig config) {
		if(config == null) {
			throw new IllegalArgumentException("FinalmybatisConfig不能为null");
		}
		if(entityClass == null) {
			throw new IllegalArgumentException("entityClass不能为null");
		}
		this.entityClass = entityClass;
		this.config = config;
		this.columnSelector = new ColumnSelector(entityClass,config);
	}
	
	public TableDefinition getTableDefinition() {
		TableDefinition tableDefinition = new TableDefinition();
		Table tableAnno = entityClass.getAnnotation(Table.class);
		
		String schema = "";
		String tableName = entityClass.getSimpleName();
		
		if(tableAnno != null) {
			schema = tableAnno.schema();
			tableName = tableAnno.name();
			
		}else {
			String javaBeanName = entityClass.getSimpleName();
			if(config.isCamel2underline()) {
				tableName = FieldUtil.camelToUnderline(javaBeanName);
			}
		}
		
		tableDefinition.setSchema(schema);
		tableDefinition.setTableName(tableName);
		
		tableDefinition.setColumnDefinitions(columnSelector.getColumnDefinitions());
		tableDefinition.setAssociationDefinitions(columnSelector.getAssociationDefinitions());
		
		return tableDefinition;
	}



}
