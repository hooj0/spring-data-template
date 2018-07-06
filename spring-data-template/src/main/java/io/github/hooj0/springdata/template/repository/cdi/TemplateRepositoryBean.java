package io.github.hooj0.springdata.template.repository.cdi;

import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.repository.support.CassandraRepositoryFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.repository.cdi.CdiRepositoryBean;
import org.springframework.data.repository.config.CustomRepositoryImplementationDetector;
import org.springframework.util.Assert;

import io.github.hooj0.springdata.template.core.TemplateOperations;
import io.github.hooj0.springdata.template.repository.TemplateRepository;

/**
 * 使用 CdiRepositoryBean 创建 {@link TemplateRepository } 实例
 * @author hoojo
 * @createDate 2018年7月4日 下午2:51:58
 * @file TemplateRepositoryBean.java
 * @package io.github.hooj0.springdata.template.repository.cdi
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class TemplateRepositoryBean<T> extends CdiRepositoryBean<T> {

	private final Bean<TemplateOperations> templateOperationsBean;
	
	public TemplateRepositoryBean(Bean<TemplateOperations> operations, Set<Annotation> qualifiers, Class<T> repositoryType, BeanManager beanManager, Optional<CustomRepositoryImplementationDetector> detector) {
		super(qualifiers, repositoryType, beanManager, detector);
		
		Assert.notNull(operations, "Cannot create repository with 'null' for TemplateOperations.");
		this.templateOperationsBean = operations;
	}

	@Override
	protected T create(CreationalContext<T> creationalContext, Class<T> repositoryType) {

		TemplateOperations templateOperations = getDependencyInstance(templateOperationsBean, TemplateOperations.class);

		return create(() -> new TemplateRepositoryFactory(templateOperations), repositoryType);
	}

	@Override
	public Class<? extends Annotation> getScope() {
		return templateOperationsBean.getScope();
	}

}
