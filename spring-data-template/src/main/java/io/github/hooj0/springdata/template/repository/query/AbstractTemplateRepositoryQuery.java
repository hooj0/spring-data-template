package io.github.hooj0.springdata.template.repository.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.RepositoryQuery;

import io.github.hooj0.springdata.template.core.TemplateOperations;

/**
 * <b>function:</b> repo抽象查询方法，方便其他继承类扩展
 * @author hoojo
 * @createDate 2018年7月9日 下午4:49:22
 * @file AbstractTemplateRepositoryQuery.java
 * @package io.github.hooj0.springdata.template.repository.query
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public abstract class AbstractTemplateRepositoryQuery implements RepositoryQuery {

	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractTemplateRepositoryQuery.class);
	
	protected TemplateQueryMethod queryMethod;
	protected TemplateOperations operations;

	public AbstractTemplateRepositoryQuery(TemplateQueryMethod queryMethod, TemplateOperations operations) {
		this.queryMethod = queryMethod;
		this.operations = operations;
	}

	@Override
	public QueryMethod getQueryMethod() {
		return queryMethod;
	}
}
