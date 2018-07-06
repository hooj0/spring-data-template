package io.github.hooj0.springdata.template.repository.support;

import org.springframework.util.Assert;

import io.github.hooj0.springdata.template.core.mapping.TemplatePersistentEntity;
import io.github.hooj0.springdata.template.core.mapping.TemplatePersistentProperty;

/**
 * <b>function:</b> 实现 EntityInformation 填充 Entity 相关信息
 * @author hoojo
 * @createDate 2018年7月4日 下午5:08:33
 * @file MappingTemplateEntityInformation.java
 * @package io.github.hooj0.springdata.template.repository.support
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class MappingTemplateEntityInformation<T, ID> extends AbstractEntityInformation<T, ID> implements TemplateEntityInformation<T, ID> {

	private final TemplatePersistentEntity<T> entityMetadata;
	
	private String indexName;
	
	public MappingTemplateEntityInformation(TemplatePersistentEntity<T> entity) {
		super(entity);
		
		this.entityMetadata = entity;
	}
	
	public MappingTemplateEntityInformation(TemplatePersistentEntity<T> entity, String indexName) {
		super(entity);

		Assert.notNull(indexName, "IndexName must not be null!");

		this.entityMetadata = entity;
		this.indexName = indexName;
	}

	@Override
	public String getIdAttribute() {
		return entityMetadata.getRequiredIdProperty().getName();
	}

	@Override
	public String getIndexName() {
		return indexName;
	}

	@Override
	public String getType() {
		return entityMetadata.getParentType();
	}

	@Override
	public Long getVersion(T entity) {
		TemplatePersistentProperty versionProperty = entityMetadata.getVersionProperty();
		
		try {
			return versionProperty != null ? (Long) entityMetadata.getPropertyAccessor(entity).getProperty(versionProperty) : null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return entityMetadata.getVersion();
	}

	@Override
	public String getParentId(T entity) {

		TemplatePersistentProperty parentProperty = entityMetadata.getParentIdProperty();
		
		try {
			return parentProperty != null ? (String) entityMetadata.getPropertyAccessor(entity).getProperty(parentProperty) : null;
		} catch (Exception e) {
			throw new IllegalStateException("failed to load parent ID: " + e, e);
		}
	}

	@Override
	public String getTableName() {
		return entityMetadata.getTableName();
	}
}
