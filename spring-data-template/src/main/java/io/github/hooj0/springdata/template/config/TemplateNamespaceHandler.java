package io.github.hooj0.springdata.template.config;

import org.springframework.beans.factory.xml.NamespaceHandler;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.data.repository.config.RepositoryBeanDefinitionParser;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

import io.github.hooj0.springdata.template.repository.config.TemplateRepositoryConfigurationExtension;

/**
 * {@link NamespaceHandler} implementation to register parser for {@code <template:repositories />} elements.
 * @author hoojo
 * @createDate 2018年7月10日 下午5:47:14
 * @file TemplateNamespaceHandler.java
 * @package io.github.hooj0.springdata.template.config
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class TemplateNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		
		RepositoryConfigurationExtension extension = new TemplateRepositoryConfigurationExtension();
		RepositoryBeanDefinitionParser parser = new RepositoryBeanDefinitionParser(extension);

		registerBeanDefinitionParser("repositories", parser);
	}
}
