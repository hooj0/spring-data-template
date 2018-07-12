package io.github.hooj0.springdata.template.config;

import java.util.Set;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

import com.google.common.collect.Table;

import io.github.hooj0.springdata.template.core.MyTplTemplate;
import io.github.hooj0.springdata.template.core.TemplateOperations;
import io.github.hooj0.springdata.template.core.convert.MappingTemplateConverter;
import io.github.hooj0.springdata.template.core.mapping.SimpleTemplateMappingContext;

/**
 * <b>function:</b> 添加抽象配置中心，加载必选配置数据
 * @author hoojo
 * @createDate 2018年7月10日 下午5:56:06
 * @file AbstractTemplateConfiguration.java
 * @package io.github.hooj0.springdata.template.config
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Configuration
public class AbstractTemplateConfiguration implements BeanClassLoaderAware {

	private @Nullable ClassLoader beanClassLoader;
	
	@Bean
	public SimpleTemplateMappingContext templateMapping() throws ClassNotFoundException {
		SimpleTemplateMappingContext mappingContext = new SimpleTemplateMappingContext();
		mappingContext.setInitialEntitySet(getInitialEntitySet());

		return mappingContext;
	}
	
	@Bean
	public MappingTemplateConverter templateConverter() throws ClassNotFoundException {
		
		MappingTemplateConverter converter = new MappingTemplateConverter(templateMapping());
		return converter;
	}
	
	@Bean
	public TemplateOperations templateOperations() throws ClassNotFoundException {
		
		return new MyTplTemplate(clientFactory(), templateConverter());
	}
	
	private Object clientFactory() {
		return "client template";
	}
	
	/**
	 *  基本包以扫描使用{@link Table}注释注释的实体。
	 *  默认情况下，返回包名称{@literal this} {@ code this.getClass().getPackage().getName()}。
	 *  此方法必须永远不会返回{@literal null}。
	 */
	public String[] getEntityBasePackages() {
		return new String[] { getClass().getPackage().getName() };
	}
	
	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.beanClassLoader = classLoader;
	}

	/**
	 * 返回初始实体类的{@link Set}。 使用{@link #getEntityBasePackages() }默认扫描类路径。 
	 * 可以由子类覆盖以跳过类路径扫描并返回一组固定的实体类。
	 */
	protected Set<Class<?>> getInitialEntitySet() throws ClassNotFoundException {
		return TemplateEntityClassScanner.scan(getEntityBasePackages());
	}
	
}
