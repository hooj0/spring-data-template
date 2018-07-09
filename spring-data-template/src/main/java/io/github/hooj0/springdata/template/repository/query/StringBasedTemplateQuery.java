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

import org.springframework.data.repository.query.ParameterAccessor;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import io.github.hooj0.springdata.template.core.TemplateOperations;

/**
 * 基于字符串的repo查询实现。
 * {@link StringBasedTemplateQuery}期望使用注释查询方法带有str查询的{@link org.springframework.data.cassandra.repository.Query}。
 *  基于字符串的查询支持在查询执行期间解析的命名，基于索引和表达式参数。
 */
public class StringBasedTemplateQuery implements RepositoryQuery {

	private Object stringBasedQuery;
	private boolean isCountQuery;
	private boolean isExistsQuery;
	
	private SpelExpressionParser expressionParser;
	private QueryMethodEvaluationContextProvider evaluationContextProvider;
	
	public StringBasedTemplateQuery(TemplateQueryMethod queryMethod, TemplateOperations operations, SpelExpressionParser expressionParser, QueryMethodEvaluationContextProvider evaluationContextProvider) {

		// 查询方法对象 的相关数据
		queryMethod.getName();
		queryMethod.getNamedQueryName();
		queryMethod.getReturnType();
		queryMethod.getParameters();
		
		// operations CRUD
		operations.getEntityName();
		// 表达式
		expressionParser.parseExpression("#{user}");
		
	}

	public StringBasedTemplateQuery(String namedQuery, TemplateQueryMethod queryMethod, TemplateOperations operations,
			SpelExpressionParser expressionParser2, QueryMethodEvaluationContextProvider evaluationContextProvider2) {
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
		//return getQueryStatementCreator().select(getStringBasedQuery(), parameterAccessor);
		
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
	public Object execute(Object[] parameters) {
		return null;
	}

	@Override
	public QueryMethod getQueryMethod() {
		return null;
	}
}
