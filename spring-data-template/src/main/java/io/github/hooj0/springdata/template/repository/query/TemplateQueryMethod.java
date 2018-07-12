package io.github.hooj0.springdata.template.repository.query;

import java.lang.reflect.Method;
import java.util.Optional;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;

import io.github.hooj0.springdata.template.annotations.CountQuery;
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

	private @Nullable TemplateEntityMetadata<?> metadata;
	private Query queryAnnotation;
	private final Method method;

	
	public TemplateQueryMethod(Method method, RepositoryMetadata metadata, ProjectionFactory factory, MappingContext<? extends TemplatePersistentEntity<?>, TemplatePersistentProperty> mappingContext) {
		super(method, metadata, factory);
		
		verify(method, metadata);
		
		this.mappingContext = mappingContext;
		this.method = method;
		// 获取Query注解对象
		// this.queryAnnotation = method.getAnnotation(Query.class);
		// 获取Query注解或在注解中关联Query，进结果合并
		this.queryAnnotation = AnnotatedElementUtils.findMergedAnnotation(method, Query.class);
		
		System.out.println("--------------------------------------------------");
		System.out.println("method: " + method.getName());
		System.out.println("queryAnnotation: " + queryAnnotation);
		Query query = method.getAnnotation(Query.class);
		System.out.println("query: " + query);
		
		CountQuery count = method.getAnnotation(CountQuery.class);
		System.out.println("CountQuery: " + count);
		System.out.println("--------------------------------------------------");
	}

	public Query getQueryAnnotation() {
		return this.queryAnnotation;
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
	
	@SuppressWarnings("unchecked")
	@Override
	public TemplateEntityMetadata<?> getEntityInformation() {

		if (this.metadata == null) {
			Class<?> returnedObjectType = getReturnedObjectType();
			Class<?> domainClass = getDomainClass();

			System.out.println("***************************************************");
			System.out.println("returnedObjectType: " + returnedObjectType);
			System.out.println("domainClass: " + domainClass);
			System.out.println("DeclaringClass: " + method.getDeclaringClass());
			System.out.println("***************************************************");
			
			if (ClassUtils.isPrimitiveOrWrapper(returnedObjectType)) {
				this.metadata = new SimpleTemplateEntityMetadata<>((Class<Object>) domainClass, this.mappingContext.getRequiredPersistentEntity(domainClass)); 
			} else {
				TemplatePersistentEntity<?> returnedEntity = this.mappingContext.getPersistentEntity(returnedObjectType);
				TemplatePersistentEntity<?> managedEntity = this.mappingContext.getRequiredPersistentEntity(domainClass);

				returnedEntity = returnedEntity == null || returnedEntity.getType().isInterface() ? managedEntity : returnedEntity;
				this.metadata = new SimpleTemplateEntityMetadata<>(returnedEntity.getType(), managedEntity); 
			}
		}
		
		return this.metadata;
	}

	public String getRequiredAnnotatedQuery() {
		return Optional.of(this.queryAnnotation).map(Query::value).orElseThrow(() -> new IllegalStateException("Query method " + this + " has no annotated query"));
	}
}
