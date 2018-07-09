package io.github.hooj0.springdata.template.repository.query;

import java.lang.reflect.Method;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.EntityMetadata;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.ClassUtils;

import io.github.hooj0.springdata.template.annotations.Query;
import io.github.hooj0.springdata.template.core.mapping.TemplatePersistentEntity;
import io.github.hooj0.springdata.template.core.mapping.TemplatePersistentProperty;

/**
 * <b>function:</b> 实现查询方法
 * @author hoojo
 * @createDate 2018年7月9日 下午4:38:25
 * @file TemplateQueryMethod.java
 * @package io.github.hooj0.springdata.template.repository.query
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class TemplateQueryMethod extends QueryMethod {

	private MappingContext<? extends TemplatePersistentEntity<?>, TemplatePersistentProperty> mappingContext;
	private final Query queryAnnotation;
	
	private final RepositoryMetadata metadata;
	private final Method method;
	
	public TemplateQueryMethod(Method method, RepositoryMetadata metadata, ProjectionFactory factory) {
		super(method, metadata, factory);
		
		verify(method, metadata);
		
		this.queryAnnotation = method.getAnnotation(Query.class);
		this.metadata = metadata;
		this.method = method;
	}

	public TemplateQueryMethod(Method method, RepositoryMetadata metadata, ProjectionFactory factory, MappingContext<? extends TemplatePersistentEntity<?>, TemplatePersistentProperty> mappingContext) {
		this(method, metadata, factory);
		
		this.mappingContext = mappingContext;
	}

	public boolean hasAnnotatedQuery() {
		return this.queryAnnotation != null;
	}

	public String getAnnotatedQuery() {
		return (String) AnnotationUtils.getValue(queryAnnotation, "value");
	}
	
	public void verify(Method method, RepositoryMetadata metadata) {

		if (isPageQuery()) {
			throw new RuntimeException("Page queries are not supported. Use a Slice query.");
		}
	}
	
	public TypeInformation<?> getReturnType() {
		return ClassTypeInformation.fromReturnTypeOf(this.method);
	}
	
	@Override
	public EntityMetadata<?> getEntityInformation() {

		if (this.metadata == null) {

			Class<?> returnedObjectType = getReturnedObjectType();
			Class<?> domainClass = getDomainClass();

			if (ClassUtils.isPrimitiveOrWrapper(returnedObjectType)) {

				// this.metadata = 
			} else {

				TemplatePersistentEntity<?> returnedEntity = this.mappingContext.getPersistentEntity(returnedObjectType);
				TemplatePersistentEntity<?> managedEntity = this.mappingContext.getRequiredPersistentEntity(domainClass);

				returnedEntity = returnedEntity == null || returnedEntity.getType().isInterface() ? managedEntity : returnedEntity;
				// this.metadata =
			}
		}

		//return this.metadata;
		return super.getEntityInformation();
	}

	public String getRequiredAnnotatedQuery() {
		return null;
	}
}
