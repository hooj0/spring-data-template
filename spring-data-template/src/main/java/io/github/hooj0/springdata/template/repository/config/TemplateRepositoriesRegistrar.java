package io.github.hooj0.springdata.template.repository.config;

import java.lang.annotation.Annotation;

import org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

/**
 * <b>function:</b> {@link RepositoryBeanDefinitionRegistrarSupport}通过{@link EnableTemplateRepositories}设置Template存储库。
 * @author hoojo
 * @createDate 2018年7月10日 上午10:33:14
 * @file TemplateRepositoriesRegistrar.java
 * @package io.github.hooj0.springdata.template.repository.config
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class TemplateRepositoriesRegistrar extends RepositoryBeanDefinitionRegistrarSupport {

	@Override
	protected Class<? extends Annotation> getAnnotation() {
		return EnableTemplateRepositories.class;
	}

	@Override
	protected RepositoryConfigurationExtension getExtension() {
		return new TemplateRepositoryConfigurationExtension();
	}
}
