package io.github.hooj0.springdata.template.repository.support;

import org.springframework.data.mapping.context.MappingContext;
import org.springframework.util.Assert;

import io.github.hooj0.springdata.template.core.mapping.TemplatePersistentEntity;
import io.github.hooj0.springdata.template.core.mapping.TemplatePersistentProperty;

/**
 * <b>function:</b> TemplateEntityInformation 对象创造者接口实现
 * @author hoojo
 * @createDate 2018年7月5日 上午11:19:26
 * @file TemplateEntityInformationCreatorImpl.java
 * @package io.github.hooj0.springdata.template.repository.support
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class TemplateEntityInformationCreatorImpl implements TemplateEntityInformationCreator {

	private final MappingContext<? extends TemplatePersistentEntity<?>, TemplatePersistentProperty> mappingContext;

	public TemplateEntityInformationCreatorImpl(MappingContext<? extends TemplatePersistentEntity<?>, TemplatePersistentProperty> mappingContext) {
		Assert.notNull(mappingContext, "MappingContext must not be null!");

		this.mappingContext = mappingContext;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T, ID> TemplateEntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {
		TemplatePersistentEntity<T> persistentEntity = (TemplatePersistentEntity<T>) mappingContext.getRequiredPersistentEntity(domainClass);

		Assert.notNull(persistentEntity, String.format("Unable to obtain mapping metadata for %s!", domainClass));
		Assert.notNull(persistentEntity.getIdProperty(), String.format("No id property found for %s!", domainClass));

		return new MappingTemplateEntityInformation<>(persistentEntity);
	}
}
