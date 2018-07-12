/*
 * Copyright 2016-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.hooj0.springdata.template.repository.query;

import java.util.Arrays;

import org.springframework.data.repository.query.ParameterAccessor;
import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import io.github.hooj0.springdata.template.annotations.Query;
import io.github.hooj0.springdata.template.core.TemplateOperations;
import lombok.extern.slf4j.Slf4j;

/**
 * 基于字符串的repo查询实现。
 * {@link StringBasedTemplateQuery}期望使用注释查询方法带有str查询的{@link org.springframework.data.cassandra.repository.Query}。
 *  基于字符串的查询支持在查询执行期间解析的命名，基于索引和表达式参数。
 */
@Slf4j
public class StringBasedTemplateQuery extends AbstractTemplateRepositoryQuery implements RepositoryQuery {

	private Object stringBasedQuery;
	private boolean isCountQuery;
	private boolean isExistsQuery;
	
	private TemplateQueryMethod queryMethod;
	private SpelExpressionParser expressionParser;
	private QueryMethodEvaluationContextProvider evaluationContextProvider;
	
	public StringBasedTemplateQuery(TemplateQueryMethod queryMethod, TemplateOperations operations, SpelExpressionParser expressionParser, QueryMethodEvaluationContextProvider evaluationContextProvider) {
		super(queryMethod, operations);
		
		this.expressionParser = expressionParser;
		this.evaluationContextProvider = evaluationContextProvider;
		this.queryMethod = queryMethod;
		
		log.debug("queryMethod: {}", queryMethod);
		
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

	public StringBasedTemplateQuery(String namedQuery, TemplateQueryMethod method, TemplateOperations operations, SpelExpressionParser expressionParser, QueryMethodEvaluationContextProvider evaluationContextProvider) {
		this(method, operations, expressionParser, evaluationContextProvider);
		
		log.debug("namedQuery: {}", namedQuery);
	}

	// 获取表达式值
	@SuppressWarnings("unused")
	private Object evaluateExpression(String expressionString, Object parameters, Object[] parameterValues) {

		EvaluationContext evaluationContext = evaluationContextProvider.getEvaluationContext(null, parameterValues);
		Expression expression = expressionParser.parseExpression(expressionString);

		return expression.getValue(evaluationContext, Object.class);
	}
	
	protected Object getStringBasedQuery() {
		return this.stringBasedQuery;
	}

	public Object createQuery(ParameterAccessor parameterAccessor) {
		// 访问方法参数的接口。 允许专用访问特殊类型的参数
		parameterAccessor.forEach(System.out::println);
		
		// 创建 QueryStatementCreator
		// 调用 QueryStatementCreator 构建查询语句、查询对象 Statement
		// 返回 Statement，在 execute 方法中利用 Statement 执行查询
		return null;
	}
	
	@Override
	public Object execute(Object[] parameters) {
		log.debug("execute: {}", new Object[] { parameters });
		
		// 构建 Parameter 访问器
		ParametersParameterAccessor accessor = new ParametersParameterAccessor(queryMethod.getParameters(), parameters);
		
		// 利用parameterAccessor、queryMethod、SpelExpressionParser 绑定参数或表达式值，
		
		// 利用 Conversion/converter 转换数据
		
		// 构建 Statement 查询组件
		
		// 通过 createQuery 构建 Statement 查询组件
		createQuery(accessor);
		
		// 利用 TemplateOperations 执行查询
		
		//  Conversion/converter 转换数据类型，进行绑定或映射，返回结果
		
		return null;
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
