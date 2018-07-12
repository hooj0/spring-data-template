package io.github.hooj0.springdata.template.core.convert;

import org.springframework.core.convert.ConversionService;
import org.springframework.data.mapping.context.MappingContext;

import io.github.hooj0.springdata.template.core.mapping.TemplatePersistentEntity;
import io.github.hooj0.springdata.template.core.mapping.TemplatePersistentProperty;

/**
 * 转换器接口，转换属性或实体对象，可以完成复杂类型到底层类型的转换，已达到自动映射
 * @author hoojo
 * @createDate 2018年7月5日 上午11:24:38
 * @file TemplateConverter.java
 * @package io.github.hooj0.springdata.template.core.convert
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface TemplateConverter {

	/**
	 * 转换服务 
	 * @author hoojo
	 * @createDate 2018年7月5日 上午11:25:16
	 */
	ConversionService getConversionService();
	
	/**
	 * 实体属性映射上下文
	 * @author hoojo
	 * @createDate 2018年7月5日 上午11:25:55
	 */
	MappingContext<? extends TemplatePersistentEntity<?>, TemplatePersistentProperty> getMappingContext();
}
