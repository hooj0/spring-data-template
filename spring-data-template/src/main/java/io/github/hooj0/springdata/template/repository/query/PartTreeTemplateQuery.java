package io.github.hooj0.springdata.template.repository.query;

import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.repository.query.ParameterAccessor;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.parser.PartTree;

import io.github.hooj0.springdata.template.core.TemplateOperations;
import io.github.hooj0.springdata.template.core.mapping.TemplatePersistentEntity;
import io.github.hooj0.springdata.template.core.mapping.TemplatePersistentProperty;


/**
 * PartTreeTemplateQuery extends AbstractQuery 完成PartTree类型的查询
 * @author hoojo
 * @createDate 2018年7月9日 下午5:48:06
 * @file PartTreeCassandraQuery.java
 * @package io.github.hooj0.springdata.template.repository.query
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class PartTreeTemplateQuery implements RepositoryQuery {

	private final MappingContext<? extends TemplatePersistentEntity<?>, TemplatePersistentProperty> mappingContext;

	private final PartTree tree;

	private final Object statementFactory;

	public PartTreeTemplateQuery(TemplateQueryMethod queryMethod, TemplateOperations operations) {
		this.tree = new PartTree(queryMethod.getName(), queryMethod.getResultProcessor().getReturnedType().getDomainType());
		this.mappingContext = operations.getTemplateConverter().getMappingContext();
		this.statementFactory = new Object();
	}

	/**
	 * 返回此查询使用的{@link MappingContext}，以访问用于将对象存储（映射）到Template表的映射元数据。
	 * @return the {@link MappingContext} used by this query.
	 * @see TemplateMappingContext
	 */
	protected MappingContext<? extends TemplatePersistentEntity<?>, TemplatePersistentProperty> getMappingContext() {
		return this.mappingContext;
	}

	/**
	 * 返回原生的 StatementFactory 方便进行原生的查询操作
	 * @author hoojo
	 * @createDate 2018年7月9日 下午5:51:22
	 */
	protected Object getStatementFactory() {
		return this.statementFactory;
	}

	/**
	 * Return the {@link PartTree} backing the query.
	 * @return the tree
	 */
	protected PartTree getTree() {
		return this.tree;
	}
	

	protected Object createQuery(ParameterAccessor parameterAccessor) {

		/*if (isCountQuery()) {
			return getQueryStatementCreator().count(getStatementFactory(), getTree(), parameterAccessor);
		}

		if (isExistsQuery()) {
			return getQueryStatementCreator().exists(getStatementFactory(), getTree(), parameterAccessor);
		}

		return getQueryStatementCreator().select(getStatementFactory(), getTree(), parameterAccessor);
		*/
		
		return null;
	}

	protected boolean isCountQuery() {
		return getTree().isCountProjection();
	}

	protected boolean isExistsQuery() {
		return getTree().isExistsProjection();
	}

	protected boolean isLimiting() {
		return getTree().isLimiting();
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
