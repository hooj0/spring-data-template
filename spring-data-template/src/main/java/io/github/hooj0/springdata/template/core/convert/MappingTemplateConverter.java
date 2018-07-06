package io.github.hooj0.springdata.template.core.convert;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.util.Assert;

import io.github.hooj0.springdata.template.core.mapping.TemplatePersistentEntity;
import io.github.hooj0.springdata.template.core.mapping.TemplatePersistentProperty;

/**
 * <b>function:</b> 实现模板转换器接口，载入上下文和转换服务，方便具体类型的转换器进行转换操作
 * @author hoojo
 * @createDate 2018年7月5日 上午11:29:09
 * @file MappingTemplateConverter.java
 * @package io.github.hooj0.springdata.template.core.convert
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class MappingTemplateConverter implements TemplateConverter, ApplicationContextAware {

	private final MappingContext<? extends TemplatePersistentEntity<?>, TemplatePersistentProperty> mappingContext;
	private final GenericConversionService conversionService;

	@SuppressWarnings("unused")
	private ApplicationContext applicationContext;
	
	public MappingTemplateConverter(MappingContext<? extends TemplatePersistentEntity<?>, TemplatePersistentProperty> mappingContext) {
		
		Assert.notNull(mappingContext, "MappingContext must not be null!");
		
		this.mappingContext = mappingContext;
		this.conversionService = new DefaultConversionService();
	}
	
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.applicationContext = context;
		
		if (mappingContext instanceof ApplicationContextAware) {
			((ApplicationContextAware) mappingContext).setApplicationContext(applicationContext);
		}
	}

	@Override
	public ConversionService getConversionService() {
		return conversionService;
	}

	@Override
	public MappingContext<? extends TemplatePersistentEntity<?>, TemplatePersistentProperty> getMappingContext() {
		return mappingContext;
	}
}
