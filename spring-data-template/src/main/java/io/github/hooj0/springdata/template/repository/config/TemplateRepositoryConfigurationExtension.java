package io.github.hooj0.springdata.template.repository.config;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.data.repository.config.AnnotationRepositoryConfigurationSource;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;
import org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport;
import org.springframework.data.repository.config.XmlRepositoryConfigurationSource;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import io.github.hooj0.springdata.template.annotations.Model;
import io.github.hooj0.springdata.template.repository.TemplateRepository;
import io.github.hooj0.springdata.template.repository.support.TemplateRepositoryFactoryBean;

/**
 * {@link RepositoryConfigurationExtension}的基本实现，以简化接口的实现。
 * 将基于实现者提供的模块前缀默认默认的命名查询位置（请参阅{@link #getModulePrefix() }）。
 * 存储后处理方法，因为默认情况下可能不需要它们。
 * @author hoojo
 * @createDate 2018年7月10日 上午10:34:58
 * @file TemplateRepositoryConfigurationExtension.java
 * @package io.github.hooj0.springdata.template.repository.config
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class TemplateRepositoryConfigurationExtension extends RepositoryConfigurationExtensionSupport {

	private static final String TPL_TEMPLATE_REF = "tpl-template-ref";
	
	/**
	 * <b>function:</b> spring data 模块名称
	 * @author hoojo
	 * @createDate 2018年7月10日 上午10:45:26
	 */
	@Override
	public String getModuleName() {
		return "Template";
	}
	
	/**
	 * <b>function:</b> 模块前缀
	 * @author hoojo
	 * @createDate 2018年7月10日 上午10:45:44
	 */
	@Override
	protected String getModulePrefix() {
		return "template";
	}

	/**
	 * <b>function:</b> template repo Factory Bean
	 * @author hoojo
	 * @createDate 2018年7月10日 上午10:46:03
	 */
	@Override
	public String getRepositoryFactoryBeanClassName() {
		return TemplateRepositoryFactoryBean.class.getName();
	}
	
	@Override
	public void postProcess(BeanDefinitionBuilder builder, XmlRepositoryConfigurationSource config) {
		Element element = config.getElement();

		String tplTemplateRef = Optional.ofNullable(element.getAttribute(TPL_TEMPLATE_REF)) //
				.filter(StringUtils::hasText) //
				.orElse(TPL_TEMPLATE_REF);

		builder.addPropertyReference("myTplTemplate", tplTemplateRef);
		builder.addPropertyReference("templateOperations", tplTemplateRef);
	}
	
	@Override
	public void postProcess(BeanDefinitionBuilder builder, AnnotationRepositoryConfigurationSource config) {
		String templateRef = config.getAttributes().getString("tplTemplateRef");

		if (StringUtils.hasText(templateRef)) {
			builder.addPropertyReference("myTplTemplate", templateRef);
			builder.addPropertyReference("templateOperations", templateRef);
		}
	}

	/**
	 * 在评估存储分配的存储库接口时，返回注释以扫描域类型。 模块应返回标识明确由repo管理的域类型的注释。
	 * @author hoojo
	 * @createDate 2018年7月10日 上午10:53:11
	 */
	@Override
	protected Collection<Class<? extends Annotation>> getIdentifyingAnnotations() {
		return Collections.singleton(Model.class);
	}

	@Override
	protected Collection<Class<?>> getIdentifyingTypes() {
		return Collections.singleton(TemplateRepository.class);
	}

	@Override
	protected boolean useRepositoryConfiguration(RepositoryMetadata metadata) {
		return !metadata.isReactiveRepository();
	}
}
