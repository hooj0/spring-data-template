package io.github.hooj0.springdata.template.repository.query.complex;

import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.repository.query.ParameterAccessor;
import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.ResultProcessor;
import org.springframework.data.repository.query.parser.PartTree;

import io.github.hooj0.springdata.template.core.TemplateOperations;
import io.github.hooj0.springdata.template.core.mapping.TemplatePersistentEntity;
import io.github.hooj0.springdata.template.core.mapping.TemplatePersistentProperty;
import io.github.hooj0.springdata.template.repository.query.AbstractTemplateRepositoryQuery;
import io.github.hooj0.springdata.template.repository.query.SimpleTemplateQueryCreator;
import io.github.hooj0.springdata.template.repository.query.TemplateQueryMethod;
import lombok.extern.slf4j.Slf4j;


/**
 * PartTreeTemplateQuery 完成用户自定义派生类型的查询
 * @author hoojo
 * @createDate 2018年7月9日 下午5:48:06
 * @file PartTreeCassandraQuery.java
 * @package io.github.hooj0.springdata.template.repository.query
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Slf4j
public class PartTreeTemplateQuery extends AbstractTemplateRepositoryQuery implements RepositoryQuery {

	private final MappingContext<? extends TemplatePersistentEntity<?>, TemplatePersistentProperty> mappingContext;
	private final PartTree tree;
	private final Object statementFactory;
	private final TemplateQueryMethod queryMethod;

	public PartTreeTemplateQuery(TemplateQueryMethod queryMethod, TemplateOperations operations) {
		super(queryMethod, operations);
		
		System.out.println("===============================================================");
		System.out.println("Method: " + queryMethod.getName());
		System.out.println("JavaType: " + queryMethod.getEntityInformation().getJavaType());
		System.out.println("DomainType: " + queryMethod.getResultProcessor().getReturnedType().getDomainType());
		System.out.println("===============================================================");
		
		this.tree = new PartTree(queryMethod.getName(), queryMethod.getResultProcessor().getReturnedType().getDomainType());
		this.mappingContext = operations.getTemplateConverter().getMappingContext();
		this.statementFactory = new Object();
		this.queryMethod = queryMethod;
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
		log.debug("parameterAccessor: {}", parameterAccessor);
		
		if (isCountQuery()) {
			log.info("part tree execute CountQuery({})", getStatementFactory(), getTree(), parameterAccessor);
		}

		if (isExistsQuery()) {
			log.info("part tree execute ExistsQuery({})", getStatementFactory(), getTree(), parameterAccessor);
		}

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

	@SuppressWarnings("unused")
	@Override
	public Object execute(Object[] parameters) {
		log.info("part tree execute({})", parameters);
		
		ParameterAccessor parameterAccessor = new ParametersParameterAccessor(getQueryMethod().getParameters(), parameters); 
		// 对返回值进行类型转换，利用converter，如果是原始对象类型可以直接返回
		ResultProcessor resultProcessor = getQueryMethod().getResultProcessor().withDynamicProjection(parameterAccessor);
		createQuery(parameterAccessor);
		
		System.err.println("Query: " + new SimpleTemplateQueryCreator(tree, parameterAccessor, mappingContext).createQuery());
		return null;
	}

	@Override
	public QueryMethod getQueryMethod() {
		return this.queryMethod;
	}
}
