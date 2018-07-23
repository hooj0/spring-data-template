package io.github.hooj0.springdata.template.repository.support;

import static org.springframework.data.querydsl.QuerydslUtils.QUERY_DSL_PRESENT;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.Assert;

import io.github.hooj0.springdata.template.core.TemplateOperations;
import io.github.hooj0.springdata.template.core.mapping.TemplatePersistentEntity;
import io.github.hooj0.springdata.template.core.mapping.TemplatePersistentProperty;
import io.github.hooj0.springdata.template.repository.TemplateRepository;
import io.github.hooj0.springdata.template.repository.query.TemplateQueryMethod;
import io.github.hooj0.springdata.template.repository.query.complex.PartTreeTemplateQuery;
import io.github.hooj0.springdata.template.repository.query.complex.StringBasedTemplateQuery;
import io.github.hooj0.springdata.template.repository.query.simple.TemplatePartQuery;
import io.github.hooj0.springdata.template.repository.query.simple.TemplateStringQuery;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class TemplateRepositoryFactory extends RepositoryFactorySupport {

	private static final SpelExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();
	
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
	public <T, ID> TemplateEntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {
		return entityInformationCreator.getEntityInformation(domainClass);
	}

	@Override
	protected Object getTargetRepository(RepositoryInformation metadata) {
		// 创建repo目标对象，传入构造参数值
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

		log.debug("IdType: {}", metadata.getIdType());
		
		if (Integer.class.isAssignableFrom(metadata.getIdType()) || Long.class.isAssignableFrom(metadata.getIdType()) || Double.class.isAssignableFrom(metadata.getIdType())) {
			return NumberKeyedRepository.class;
		} else if (metadata.getIdType() == String.class) {
			return SimpleTemplateRepository.class;
		} else if (metadata.getIdType() == UUID.class) {
			return UUIDTemplateRepository.class;
		} 
		
		throw new IllegalArgumentException("UnSupport has not been implemented yet.");
	}
	
	/**
	 * Querydsl 类型的查询 repo
	 * @author hoojo
	 * @createDate 2018年7月4日 下午4:47:10
	 */
	private static boolean isQueryDslRepository(Class<?> repositoryInterface) {
		log.debug("repositoryInterface: {}", repositoryInterface);
		
		return QUERY_DSL_PRESENT && QuerydslPredicateExecutor.class.isAssignableFrom(repositoryInterface);
	}
	
	@Override
	protected Optional<QueryLookupStrategy> getQueryLookupStrategy(Key key, QueryMethodEvaluationContextProvider evaluationContextProvider) {
		log.debug("key: {}", key);
		
		// EnableTemplateRepositories 配置中 queryLookupStrategy()
		if (key == Key.CREATE_IF_NOT_FOUND) {
			return Optional.of(new TemplateQueryLookupStrategy(operations.getTemplateConverter().getMappingContext()));
		} else {
			return Optional.of(new TemplatesQueryLookupStrategy(operations, evaluationContextProvider, operations.getTemplateConverter().getMappingContext()));
		}
	}

	/**
	 * 查询查找策略
	 */
	private class TemplateQueryLookupStrategy implements QueryLookupStrategy {
		private final MappingContext<? extends TemplatePersistentEntity<?>, TemplatePersistentProperty> mappingContext;
		
		TemplateQueryLookupStrategy(MappingContext<? extends TemplatePersistentEntity<?>, TemplatePersistentProperty> mappingContext) {
			this.mappingContext = mappingContext;
		}
		
		@Override
		public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, ProjectionFactory factory, NamedQueries namedQueries) {

			TemplateQueryMethod queryMethod = new TemplateQueryMethod(method, metadata, factory, mappingContext);
			String namedQueryName = queryMethod.getNamedQueryName();
			
			log.debug("queryMethod.getName: {}", queryMethod.getName());
			log.debug("queryMethod: {}", queryMethod);
			log.debug("namedQueryName: {}", namedQueryName);

			System.out.println("namedQueryName: " + namedQueryName);
			System.out.println("RepositoryInterface: " + metadata.getRepositoryInterface().getSimpleName());
			
			if (namedQueries.hasQuery(namedQueryName)) {
				String namedQuery = namedQueries.getQuery(namedQueryName);
				return new TemplateStringQuery(queryMethod, operations, namedQuery);
			} else if (queryMethod.hasAnnotatedQuery()) {
				return new TemplateStringQuery(queryMethod, operations, queryMethod.getAnnotatedQuery());
			}
			
			return new TemplatePartQuery(queryMethod, operations);
		}
	}
	
	/**
	 * 查询查找策略
	 */
	private class TemplatesQueryLookupStrategy implements QueryLookupStrategy {

		private final TemplateOperations operations;
		private final QueryMethodEvaluationContextProvider evaluationContextProvider;
		private final MappingContext<? extends TemplatePersistentEntity<?>, TemplatePersistentProperty> mappingContext;
		
		TemplatesQueryLookupStrategy(TemplateOperations operations, QueryMethodEvaluationContextProvider evaluationContextProvider, MappingContext<? extends TemplatePersistentEntity<?>, TemplatePersistentProperty> mappingContext) {
			this.operations = operations;
			this.evaluationContextProvider = evaluationContextProvider;
			this.mappingContext = mappingContext;
		}

		@Override
		public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, ProjectionFactory factory, NamedQueries namedQueries) {

			TemplateQueryMethod queryMethod = new TemplateQueryMethod(method, metadata, factory, mappingContext);
			String namedQueryName = queryMethod.getNamedQueryName();

			log.debug("queryMethod.getName: {}", queryMethod.getName());
			log.debug("queryMethod: {}", queryMethod);
			log.debug("namedQueryName: {}", namedQueryName);

			System.out.println("namedQueryName: " + namedQueryName);
			System.out.println("RepositoryInterface: " + metadata.getRepositoryInterface().getSimpleName());
			
			if (namedQueries.hasQuery(namedQueryName)) {
				String namedQuery = namedQueries.getQuery(namedQueryName);
				return new StringBasedTemplateQuery(namedQuery, queryMethod, operations, EXPRESSION_PARSER, evaluationContextProvider);
			} else if (queryMethod.hasAnnotatedQuery()) {
				return new StringBasedTemplateQuery(queryMethod, operations, EXPRESSION_PARSER, evaluationContextProvider);
			} else {
				return new PartTreeTemplateQuery(queryMethod, operations);
			}
		}
	}
}
