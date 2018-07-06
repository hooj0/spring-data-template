package io.github.hooj0.springdata.template.repository.support;

import static org.springframework.data.querydsl.QuerydslUtils.QUERY_DSL_PRESENT;

import java.util.UUID;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.util.Assert;

import io.github.hooj0.springdata.template.core.TemplateOperations;
import io.github.hooj0.springdata.template.repository.TemplateRepository;

/**
 * <b>function:</b> 通过 TemplateRepositoryFactory 创建 {@link TemplateRepository} 实例
 * @author hoojo
 * @createDate 2018年7月4日 下午4:33:51
 * @file TemplateRepositoryFactory.java
 * @package io.github.hooj0.springdata.template.repository.support
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class TemplateRepositoryFactory extends RepositoryFactorySupport {

	private final TemplateOperations operations;
	private final TemplateEntityInformationCreator entityInformationCreator;
	
	private final Object templateClientBean;
	private final String templateName;
	
	public TemplateRepositoryFactory(TemplateOperations operations) {
		
		Assert.notNull(operations, "TemplateOperations must not be null!");
		
		this.operations = operations;
		this.entityInformationCreator = new TemplateEntityInformationCreatorImpl(this.operations.getTemplateConverter().getMappingContext());

		// 这里可以通过 operations 获取一些对象，来操作创建 Repository 时需要的数据
		this.templateClientBean = this.operations.getClient();
		this.templateName = this.operations.getTemplateName();
		
		Assert.notNull(this.templateClientBean, "TemplateOperations must not be null!");
		Assert.notNull(this.templateName, "TemplateOperations must not be null!");
	}
	
	@Override
	public <T, ID> EntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {
		return entityInformationCreator.getEntityInformation(domainClass);
	}

	@Override
	protected Object getTargetRepository(RepositoryInformation metadata) {
		return getTargetRepositoryViaReflection(metadata, getEntityInformation(metadata.getDomainType()), operations);
	}

	/**
	 * 针对不同类型的metadata 可以返回对应的 repo.class
	 * @author hoojo
	 * @createDate 2018年7月4日 下午4:47:32
	 */
	@Override
	protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
		
		if (isQueryDslRepository(metadata.getRepositoryInterface())) {
			throw new IllegalArgumentException("QueryDsl Support has not been implemented yet.");
		}
		
		if (Integer.class.isAssignableFrom(metadata.getIdType()) || Long.class.isAssignableFrom(metadata.getIdType()) || Double.class.isAssignableFrom(metadata.getIdType())) {
			return NumberKeyedRepository.class;
		} else if (metadata.getIdType() == String.class) {
			return SimpleTemplateRepository.class;
		} else if (metadata.getIdType() == UUID.class) {
			return UUIDTemplateRepository.class;
		} else {
			throw new IllegalArgumentException("Unsupported ID type " + metadata.getIdType());
		}
	}
	
	/**
	 * Querydsl 类型的查询 repo
	 * @author hoojo
	 * @createDate 2018年7月4日 下午4:47:10
	 * @param repositoryInterface
	 * @return
	 */
	private static boolean isQueryDslRepository(Class<?> repositoryInterface) {
		return QUERY_DSL_PRESENT && QuerydslPredicateExecutor.class.isAssignableFrom(repositoryInterface);
	}
}
