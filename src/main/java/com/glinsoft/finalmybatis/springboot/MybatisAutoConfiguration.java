/**
 *    Copyright 2015-2017 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.glinsoft.finalmybatis.springboot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import com.glinsoft.finalmybatis.FinalmybatisConfig;
import com.glinsoft.finalmybatis.ext.SqlSessionFactoryBeanExt;
import com.glinsoft.finalmybatis.util.MyBeanUtil;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.glinsoft.finalmybatis.handler.BaseFill;


/**
 * {@link EnableAutoConfiguration Auto-Configuration} for Mybatis. Contributes a
 * {@link SqlSessionFactory} and a {@link SqlSessionTemplate}.
 *
 * If {@link org.mybatis.spring.annotation.MapperScan} is used, or a
 * configuration file is specified as a property, those will be considered,
 * otherwise this auto-configuration will attempt to register mappers based on
 * the interface definitions in or under the root auto-configuration package.
 *
 * @author Eddú Meléndez
 * @author Josh Long
 * @author Kazuki Shimizu
 * @author Eduardo Macarrón
 */
@org.springframework.context.annotation.Configuration
@ConditionalOnClass({ SqlSessionFactory.class, SqlSessionFactoryBean.class })
@ConditionalOnBean(DataSource.class)
@EnableConfigurationProperties(MybatisProperties.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class MybatisAutoConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(MybatisAutoConfiguration.class);

	private final MybatisProperties properties;

	private final Interceptor[] interceptors;

	private final ResourceLoader resourceLoader;

	private final DatabaseIdProvider databaseIdProvider;

	private final List<ConfigurationCustomizer> configurationCustomizers;
	
	private static ThreadLocal<List<String>> packageList = new ThreadLocal<>();
	

	public MybatisAutoConfiguration(MybatisProperties properties, ObjectProvider<Interceptor[]> interceptorsProvider,
			ResourceLoader resourceLoader, ObjectProvider<DatabaseIdProvider> databaseIdProvider,
			ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider) {
		this.properties = properties;
		this.interceptors = interceptorsProvider.getIfAvailable();
		this.resourceLoader = resourceLoader;
		this.databaseIdProvider = databaseIdProvider.getIfAvailable();
		this.configurationCustomizers = configurationCustomizersProvider.getIfAvailable();
	}

	@PostConstruct
	public void checkConfigFileExists() {
		if (this.properties.isCheckConfigLocation() && StringUtils.hasText(this.properties.getConfigLocation())) {
			Resource resource = this.resourceLoader.getResource(this.properties.getConfigLocation());
			Assert.state(resource.exists(), "Cannot find config location: " + resource
					+ " (please add config file or check your Mybatis configuration)");
		}
	}

	@Bean
	@ConditionalOnMissingBean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource, FinalmybatisConfig config) throws Exception {
		SqlSessionFactoryBeanExt factory = new SqlSessionFactoryBeanExt();
		factory.setDataSource(dataSource);
		factory.setVfs(SpringBootVFS.class);
		if (StringUtils.hasText(this.properties.getConfigLocation())) {
			factory.setConfigLocation(this.resourceLoader.getResource(this.properties.getConfigLocation()));
		}
		Configuration configuration = this.properties.getConfiguration();
		if (configuration == null && !StringUtils.hasText(this.properties.getConfigLocation())) {
			configuration = new Configuration();
		}
		if (configuration != null && !CollectionUtils.isEmpty(this.configurationCustomizers)) {
			for (ConfigurationCustomizer customizer : this.configurationCustomizers) {
				customizer.customize(configuration);
			}
		}
		factory.setConfiguration(configuration);
		if (this.properties.getConfigurationProperties() != null) {
			factory.setConfigurationProperties(this.properties.getConfigurationProperties());
		}
		if (!ObjectUtils.isEmpty(this.interceptors)) {
			factory.setPlugins(this.interceptors);
		}
		if (this.databaseIdProvider != null) {
			factory.setDatabaseIdProvider(this.databaseIdProvider);
		}
		if (StringUtils.hasLength(this.properties.getTypeAliasesPackage())) {
			factory.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
		}
		if (StringUtils.hasLength(this.properties.getTypeHandlersPackage())) {
			factory.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());
		}
		if (!ObjectUtils.isEmpty(this.properties.resolveMapperLocations())) {
			factory.setMapperLocations(this.properties.resolveMapperLocations());
		}
		String basePackage = this.properties.getBasePackage();
		if(StringUtils.isEmpty(basePackage)) {
			basePackage = StringUtils.collectionToDelimitedString(packageList.get(), ",");
		}
		factory.setBasePackage(basePackage);
		factory.setConfig(config);

		return factory.getObject();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public FinalmybatisConfig fastmybatisConfig() {
		FinalmybatisConfig config = new FinalmybatisConfig();

		MyBeanUtil.copyPropertiesIgnoreNull(this.properties, config);
		
		if(this.properties.getFill() != null) {
			config.setFills(this.buildFills(this.properties.getFill()));
		}
		
		return config;
	}
	
	private List<BaseFill<?>> buildFills(Map<String,String> fillMap) {
		Set<Entry<String, String>> entrySet = fillMap.entrySet();
		List<BaseFill<?>> fillList = new ArrayList<>(entrySet.size());
		String className = "",parameter = "";
		try {
			for (Entry<String, String> entry : entrySet) {
				className = entry.getKey();
				parameter = entry.getValue();
				BaseFill<?> handler = (BaseFill<?>)buildInstance(className, parameter);
				fillList.add(handler);
			}
			return fillList;
		}catch (ClassNotFoundException e) {
			throw new RuntimeException("属性[mybatis.fill." + className + "=" + parameter + "]设置错误.类" + className + "不存在",e);
		}catch (Exception e) {
			String errorMsg = "属性[mybatis.fill." + className + "=" + parameter + "]设置错误,检查类是否存在并继承com.gitee.fastmybatis.core.handler.FillHandler";
			logger.error(errorMsg,e);
			throw new RuntimeException(errorMsg,e);
		}
	}
	
	private static Object buildInstance(String className,String parameter) throws ClassNotFoundException, Exception {
		// 根据类名获取Class对象    
		Class<?> clazz= Class.forName(className);    
		
		if(StringUtils.hasLength(parameter)) {
			// 参数类型数组    
			Class<?>[] parameterTypes = {String.class};     
			// 根据参数类型获取相应的构造函数    
			java.lang.reflect.Constructor<?> constructor = clazz.getConstructor(parameterTypes);
			// 根据获取的构造函数和参数，创建实例    
			return constructor.newInstance(parameter); 
		}else {
			return clazz.newInstance();
		}
	}
	
	@Bean
	@ConditionalOnMissingBean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		ExecutorType executorType = this.properties.getExecutorType();
		if (executorType != null) {
			return new SqlSessionTemplate(sqlSessionFactory, executorType);
		} else {
			return new SqlSessionTemplate(sqlSessionFactory);
		}
	}

	/**
	 * This will just scan the same base package as Spring Boot does. If you
	 * want more power, you can explicitly use
	 * {@link org.mybatis.spring.annotation.MapperScan} but this will get typed
	 * mappers working correctly, out-of-the-box, similar to using Spring Data
	 * JPA repositories.
	 */
	public static class AutoConfiguredMapperScannerRegistrar
			implements BeanFactoryAware, ImportBeanDefinitionRegistrar, ResourceLoaderAware {

		private BeanFactory beanFactory;

		private ResourceLoader resourceLoader;

		@Override
		public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
				BeanDefinitionRegistry registry) {

			logger.debug("Searching for mappers annotated with @Mapper");

			ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);

			try {
				if (this.resourceLoader != null) {
					scanner.setResourceLoader(this.resourceLoader);
				}

				List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
				if (logger.isDebugEnabled()) {
					for (String pkg : packages) {
						logger.debug("Using auto-configuration base package '{}'", pkg);
					}
				}
				
				scanner.setMarkerInterface(com.glinsoft.finalmybatis.mapper.Mapper.class);
				scanner.setAnnotationClass(Mapper.class);
				scanner.registerFilters();
				scanner.doScan(StringUtils.toStringArray(packages));
				packageList.set(packages);
			} catch (IllegalStateException ex) {
				logger.debug("Could not determine auto-configuration package, automatic mapper scanning disabled.", ex);
			}
		}

		@Override
		public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
			this.beanFactory = beanFactory;
		}

		@Override
		public void setResourceLoader(ResourceLoader resourceLoader) {
			this.resourceLoader = resourceLoader;
		}

	}

	/**
	 * {@link org.mybatis.spring.annotation.MapperScan} ultimately ends up
	 * creating instances of {@link MapperFactoryBean}. If
	 * {@link org.mybatis.spring.annotation.MapperScan} is used then this
	 * auto-configuration is not needed. If it is _not_ used, however, then this
	 * will bring in a bean registrar and automatically register components
	 * based on the same component-scanning path as Spring Boot itself.
	 */
	@org.springframework.context.annotation.Configuration
	@Import({ AutoConfiguredMapperScannerRegistrar.class })
	@ConditionalOnMissingBean(MapperFactoryBean.class)
	public static class MapperScannerRegistrarNotFoundConfiguration {

		@PostConstruct
		public void afterPropertiesSet() {
			logger.debug("No {} found.", MapperFactoryBean.class.getName());
		}
	}

}
