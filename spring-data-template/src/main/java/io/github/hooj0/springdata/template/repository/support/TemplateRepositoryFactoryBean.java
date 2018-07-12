package io.github.hooj0.springdata.template.repository.support;

import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import io.github.hooj0.springdata.template.core.MyTplTemplate;
import io.github.hooj0.springdata.template.core.TemplateOperations;
import io.github.hooj0.springdata.template.repository.TemplateRepository;

/**
 * {@link org.springframework.beans.factory.FactoryBean} to create {@link TemplateRepository} instances.
 * @author hoojo
 * @createDate 2018年7月10日 上午10:44:27
 * @file TemplateRepositoryFactoryBean.java
 * @package io.github.hooj0.springdata.template.repository.support
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class TemplateRepositoryFactoryBean<T extends Repository<S, ID>, S, ID> extends RepositoryFactoryBeanSupport<T, S, ID> {

	private @Nullable TemplateOperations operations;
	
	protected TemplateRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
		super(repositoryInterface);
	}

	@Override
	protected RepositoryFactorySupport createRepositoryFactory() {
		Assert.state(operations != null, "TemplateOperations must not be null");

		return new TemplateRepositoryFactory(operations);
	}
	
	/**
	 * 配置 {@link MyTplTemplate} 来完成crud操作.
	 * @param MyTplTemplate 完成CRUD 业务模板
	 */
	public void setMyTplTemplate(MyTplTemplate myTplTemplate) {
		this.operations = myTplTemplate;
		setMappingContext(myTplTemplate.getTemplateConverter().getMappingContext());
	}
	
	public void setTemplateOperations(TemplateOperations operations) {
		Assert.notNull(operations, "TemplateOperations must not be null!");
		
		this.operations = operations;
		setMappingContext(operations.getTemplateConverter().getMappingContext());
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		
		Assert.notNull(operations, "TemplateOperations must not be null!");
	}
}
