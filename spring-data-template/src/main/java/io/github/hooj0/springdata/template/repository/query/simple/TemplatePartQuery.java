package io.github.hooj0.springdata.template.repository.query.simple;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.parser.PartTree;
import org.springframework.data.util.CloseableIterator;
import org.springframework.data.util.StreamUtils;
import org.springframework.util.ClassUtils;

import io.github.hooj0.springdata.template.core.TemplateOperations;
import io.github.hooj0.springdata.template.core.mapping.TemplatePersistentProperty;
import io.github.hooj0.springdata.template.core.query.CriteriaQuery;
import io.github.hooj0.springdata.template.repository.query.AbstractTemplateRepositoryQuery;
import io.github.hooj0.springdata.template.repository.query.SimpleTemplateQueryCreator;
import io.github.hooj0.springdata.template.repository.query.TemplateQueryMethod;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户自定义派生查询底层实现
 * @author hoojo
 * @createDate 2018年7月12日 上午9:33:39
 * @file TemplatePartQuery.java
 * @package io.github.hooj0.springdata.template.repository.query
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Slf4j
public class TemplatePartQuery extends AbstractTemplateRepositoryQuery {

	private final PartTree tree;
	private final MappingContext<?, TemplatePersistentProperty> mappingContext;

	public TemplatePartQuery(TemplateQueryMethod method, TemplateOperations operations) {
		super(method, operations);
		
		System.out.println("===============================================================");
		System.out.println("Method: " + queryMethod.getName());
		System.out.println("JavaType: " + queryMethod.getEntityInformation().getJavaType());
		System.out.println("DomainType: " + queryMethod.getResultProcessor().getReturnedType().getDomainType());
		System.out.println("===============================================================");
		
		this.tree = new PartTree(method.getName(), method.getEntityInformation().getJavaType());
		this.mappingContext = operations.getTemplateConverter().getMappingContext();
	}

	@Override	
	public Object execute(Object[] parameters) {
		
		ParametersParameterAccessor accessor = new ParametersParameterAccessor(queryMethod.getParameters(), parameters);
		log.debug("part query execute parameters-vals: {}", parameters);
		log.debug("part query execute accessor: {}", accessor);
		
		CriteriaQuery query = createQuery(accessor);
		log.debug("part query execute-query: {}", query);
		
		System.err.println("Query: " + new SimpleTemplateQueryCreator(tree, accessor, mappingContext).createQuery());
		
		if(tree.isDelete()) {
			Object result = pageQuery(query, accessor);
			operations.delete(query, queryMethod.getEntityInformation().getJavaType());
			return result;
		} else if (queryMethod.isPageQuery()) {
			query.setPageable(accessor.getPageable());
			return operations.queryForPage(query, queryMethod.getEntityInformation().getJavaType());
		} else if (queryMethod.isStreamQuery()) {
			Class<?> entityType = queryMethod.getEntityInformation().getJavaType();
			if (query.getPageable().isUnpaged()) { // page count 没有值
				int itemCount = (int) operations.count(query, queryMethod.getEntityInformation().getJavaType());
				query.setPageable(PageRequest.of(0, Math.max(1, itemCount)));
			}

			return StreamUtils.createStreamFromIterator((CloseableIterator<Object>) operations.stream(query, entityType));
		} else if (queryMethod.isCollectionQuery()) {
			if (accessor.getPageable() == null) {
				int itemCount = (int) operations.count(query, queryMethod.getEntityInformation().getJavaType());
				query.setPageable(PageRequest.of(0, Math.max(1, itemCount)));
			} else {
			    query.setPageable(accessor.getPageable());
		    }
			return operations.queryForList(query, queryMethod.getEntityInformation().getJavaType());
		} else if (tree.isCountProjection()) {
			return operations.count(query, queryMethod.getEntityInformation().getJavaType());
		}
		
		return operations.queryForObject(query, queryMethod.getEntityInformation().getJavaType());
	}

	private Object pageQuery(CriteriaQuery query, ParametersParameterAccessor accessor) {
		Object result = null;

		if (queryMethod.isCollectionQuery()) {
			if (accessor.getPageable().isUnpaged()) {
				int itemCount = (int) operations.count(query, queryMethod.getEntityInformation().getJavaType());
				query.setPageable(PageRequest.of(0, Math.max(1, itemCount)));
			} else {
				query.setPageable(accessor.getPageable());
			}
			result = operations.queryForList(query, queryMethod.getEntityInformation().getJavaType());
		}

		if (ClassUtils.isAssignable(Number.class, queryMethod.getReturnedObjectType())) {
			result = operations.count(query, queryMethod.getEntityInformation().getJavaType());
		}
		return result;
	}

	// 构建查询对象，进行参数绑定设置
	public CriteriaQuery createQuery(ParametersParameterAccessor accessor) {
		log.debug("part query createQuery: {}", accessor);
		
		System.err.println("Query: " + new SimpleTemplateQueryCreator(tree, accessor, mappingContext).createQuery());
		return new TemplateQueryCreator(tree, accessor, mappingContext).createQuery();
	}
	
	static void print(Object[] values) {
		System.out.println("##############parameters################");
		for (Object value : values) {
			System.out.println("value: " + value);
		}
		System.out.println("################parameters##############");
	}
	
	static void print(QueryMethod method) {
		System.out.println("##############QueryMethod################");
		method.getParameters().iterator().forEachRemaining(param -> {
			System.out.println(param.getIndex() + "->" + param.getName() + "/" + param.getPlaceholder() + "/" + param.getType().getSimpleName());
		});
		System.out.println("##############QueryMethod################");
	}
	
	static void print(ParametersParameterAccessor accessor) {
		System.out.println("##############accessor################");
		accessor.forEach(action -> {
			System.out.println("accessorIter: " + action);
		});
		
		accessor.getParameters().iterator().forEachRemaining(param -> {
			System.out.println("iterParameters: " + param.getIndex() + "->" + param.getName() + "/" + param.getPlaceholder() + "/" + param.getType().getSimpleName());
		});
		
		int count = accessor.getParameters().getBindableParameters().getNumberOfParameters();
		for (int i = 0; i < count; i++) {
			System.out.println("bindValue: " + accessor.getBindableValue(i));
		}
		System.out.println("##############accessor################");
	}
	
	public void print() {
		System.out.println("##############part-tree################");
		tree.forEach(part -> {
			part.forEach(p -> {
				System.out.println(p.getNumberOfArguments() + "->" + p.getProperty().getSegment() + "/" + p.getProperty() + "/" + p.getProperty().toDotPath());
			});
		});
		System.out.println("##############part-tree################");
	}
}
