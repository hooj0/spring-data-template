package io.github.hooj0.springdata.template.repository.query.complex;

import java.util.Arrays;

import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import io.github.hooj0.springdata.template.annotations.Query;
import io.github.hooj0.springdata.template.core.TemplateOperations;
import io.github.hooj0.springdata.template.repository.query.AbstractTemplateRepositoryQuery;
import io.github.hooj0.springdata.template.repository.query.TemplateQueryMethod;
import lombok.extern.slf4j.Slf4j;

/**
 * 基于字符串的repo查询实现。
 * {@link StringBasedTemplateQuery}期望使用注释查询方法带有str查询的{@link org.springframework.data.cassandra.repository.Query}。
 *  基于字符串的查询支持在查询执行期间解析的命名，基于索引和表达式参数。
 */
@Slf4j
public class StringBasedTemplateQuery extends AbstractTemplateRepositoryQuery implements RepositoryQuery {

	private StringBasedQuery stringBasedQuery;
	private boolean isCountQuery;
	private boolean isExistsQuery;
	
	private TemplateQueryMethod queryMethod;
	
	public StringBasedTemplateQuery(TemplateQueryMethod queryMethod, TemplateOperations operations, SpelExpressionParser expressionParser, QueryMethodEvaluationContextProvider evaluationContextProvider) {
		this(queryMethod.getRequiredAnnotatedQuery(), queryMethod, operations, expressionParser, evaluationContextProvider);
	}

	public StringBasedTemplateQuery(String namedQuery, TemplateQueryMethod queryMethod, TemplateOperations operations, SpelExpressionParser expressionParser, QueryMethodEvaluationContextProvider evaluationContextProvider) {
		super(queryMethod, operations);
		
		log.debug("namedQuery: {}", namedQuery);
		log.debug("queryMethod: {}", queryMethod);

		this.queryMethod = queryMethod;
		this.stringBasedQuery = new StringBasedQuery(namedQuery, new ExpressionEvaluatingParameterBinder(expressionParser, evaluationContextProvider));
		
		if (queryMethod.hasAnnotatedQuery()) {
			Query queryAnnotation = queryMethod.getQueryAnnotation();

			this.isCountQuery = queryAnnotation.count();
			this.isExistsQuery = queryAnnotation.exists();

			if (hasAmbiguousProjectionFlags(this.isCountQuery, this.isExistsQuery)) {
				throw new IllegalArgumentException(String.format(COUNT_AND_EXISTS, queryMethod));
			}
		} else {
			this.isCountQuery = false;
			this.isExistsQuery = false;
		}
		
		// 查询方法对象 的相关数据
		log.debug("queryMethod.getName: {}", queryMethod.getName());
		log.debug("queryMethod.getNamedQueryName: {}", queryMethod.getNamedQueryName());
		log.debug("queryMethod.getReturnType: {}", queryMethod.getReturnType());
		log.debug("queryMethod.getParameters: {}", queryMethod.getParameters());
		log.debug("queryMethod.getAnnotatedQuery: {}", queryMethod.getAnnotatedQuery());
		
		// operations CRUD
		operations.getEntityName();
		// 表达式
		// expressionParser.parseExpression("#{user}");
		
		log.debug("evaluationContextProvider: {}", evaluationContextProvider);
	}

	public StringBasedQuery getStringBasedQuery() {
		return this.stringBasedQuery;
	}

	public SimpleStatement createQuery(ParametersParameterAccessor parameterAccessor, Object... parameters) {
		// 访问方法参数的接口。 允许专用访问特殊类型的参数
		
		// 创建 QueryStatementCreator
		// 调用 QueryStatementCreator 构建查询语句、查询对象 Statement
		// 返回 Statement，在 execute 方法中利用 Statement 执行查询
		return stringBasedQuery.bindQuery(parameterAccessor, this.queryMethod, parameters);
	}
	
	@Override
	public SimpleStatement execute(Object[] parameters) {
		log.debug("execute: {}", new Object[] { parameters });
		
		// 构建 Parameter 访问器
		ParametersParameterAccessor accessor = new ParametersParameterAccessor(queryMethod.getParameters(), parameters);
		
		
		// 利用parameterAccessor、queryMethod、SpelExpressionParser 绑定参数或表达式值，
		
		// 利用 Conversion/converter 转换数据
		
		// 构建 Statement 查询组件
		
		// 通过 createQuery 构建 Statement 查询组件
		//createQuery(accessor);
		
		// 利用 TemplateOperations 执行查询
		
		//  Conversion/converter 转换数据类型，进行绑定或映射，返回结果
		return stringBasedQuery.bindQuery(accessor, this.queryMethod, parameters);
	}

	protected boolean isCountQuery() {
		return this.isCountQuery;
	}

	protected boolean isExistsQuery() {
		return this.isExistsQuery;
	}

	protected boolean isLimiting() {
		return false;
	}

	@Override
	public QueryMethod getQueryMethod() {
		return queryMethod;
	}
	
	static boolean hasAmbiguousProjectionFlags(Boolean... flags) {
		return Arrays.stream(flags).filter(Boolean::booleanValue).count() > 1;
	}
}
