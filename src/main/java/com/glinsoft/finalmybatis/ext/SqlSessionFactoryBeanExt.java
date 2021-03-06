package com.glinsoft.finalmybatis.ext;

import static org.springframework.util.Assert.notNull;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.glinsoft.finalmybatis.ext.exception.DatabaseConnectException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.Resource;

import com.glinsoft.finalmybatis.FinalmybatisConfig;


/**
 * SqlSessionFactoryBean扩展
 *
 */
public class SqlSessionFactoryBeanExt extends SqlSessionFactoryBean {
	
	private static final Log LOG = LogFactory.getLog(SqlSessionFactoryBeanExt.class);
	
	private MapperLocationsBuilder mapperLocationsBuilder = new MapperLocationsBuilder();
	
	private String basePackage;
	
	@Override
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
		String dbName = buildDbName(dataSource);
		mapperLocationsBuilder.setDbName(dbName);
	}
	
	@Override
	public void setMapperLocations(Resource[] mapperLocations) {
		mapperLocationsBuilder.storeMapperFile(mapperLocations);
	}
	
	@Override
	protected SqlSessionFactory buildSqlSessionFactory() throws Exception {
		notNull(this.basePackage, "属性 'basePackage' 必填");
		
		Resource[] allMapperLocations = mapperLocationsBuilder.build(this.basePackage);
		// 重新设置mapperLocation属性
		super.setMapperLocations(allMapperLocations);
		
		return super.buildSqlSessionFactory();
	}
	
	
	/**
	 * @param basePackage
	 *            指定哪些包需要被扫描,支持多个包"package.a,package.b"并对每个包都会递归搜索
	 */
	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public void setConfig(FinalmybatisConfig fastmybatisConfig) {
		mapperLocationsBuilder.setConfig(fastmybatisConfig);
	}
	
	/** 获取数据库类型 */
	private static String buildDbName(DataSource dataSource) {
		if(dataSource == null) {
			throw new NullPointerException("dataSource 不能为null");
		}
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			DatabaseMetaData metaData = conn.getMetaData();
			String dbName = metaData.getDatabaseProductName();
			LOG.debug("数据库名称：" + dbName);
			return dbName;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new DatabaseConnectException(e);
		}finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					LOG.error(e.getMessage(), e);
				}
			}
		}
	}
	
}
