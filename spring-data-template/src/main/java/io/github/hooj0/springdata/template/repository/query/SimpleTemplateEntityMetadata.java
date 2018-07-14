package io.github.hooj0.springdata.template.repository.query;

import org.springframework.util.Assert;

import io.github.hooj0.springdata.template.core.mapping.TemplatePersistentEntity;

/**
 * Implementation of {@link TemplateEntityMetadata} based on the type and {@link TemplatePersistentEntity}.
 */
public class SimpleTemplateEntityMetadata<T> implements TemplateEntityMetadata<T> {

	private final TemplatePersistentEntity<?> entity;

	private final Class<T> type;

	/**
	 * Create a new {@link SimpleTemplateEntityMetadata} using the given type and {@link TemplatePersistentEntity} to
	 * use for table lookups.
	 *
	 * @param type must not be {@literal null}.
	 * @param entity must not be {@literal null} or empty.
	 */
	SimpleTemplateEntityMetadata(Class<T> type, TemplatePersistentEntity<?> entity) {
		Assert.notNull(type, "Type must not be null");
		Assert.notNull(entity, "Collection entity must not be null or empty");

		this.type = type;
		this.entity = entity;
	}

	
	@Override
	public Class<T> getJavaType() {
		return type;
	}

	@Override
	public String getTableName() {
		return entity.getTableName();
	}
}
