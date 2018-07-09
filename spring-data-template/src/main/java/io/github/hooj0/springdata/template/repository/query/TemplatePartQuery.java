package io.github.hooj0.springdata.template.repository.query;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.data.repository.query.parser.PartTree;
import org.springframework.data.util.CloseableIterator;
import org.springframework.data.util.StreamUtils;
import org.springframework.util.ClassUtils;

import io.github.hooj0.springdata.template.core.TemplateOperations;
import io.github.hooj0.springdata.template.core.mapping.TemplatePersistentProperty;
import io.github.hooj0.springdata.template.core.query.CriteriaQuery;

public class TemplatePartQuery extends AbstractTemplateRepositoryQuery {

	private final PartTree tree;
	private final MappingContext<?, TemplatePersistentProperty> mappingContext;

	public TemplatePartQuery(TemplateQueryMethod method, TemplateOperations operations) {
		super(method, operations);
		
		this.tree = new PartTree(method.getName(), method.getEntityInformation().getJavaType());
		this.mappingContext = operations.getTemplateConverter().getMappingContext();
	}

	@Override	
	public Object execute(Object[] parameters) {
		
		ParametersParameterAccessor accessor = new ParametersParameterAccessor(queryMethod.getParameters(), parameters);
		
		CriteriaQuery query = createQuery(accessor);
		if(tree.isDelete()) {
			Object result = countOrGetDocumentsForDelete(query, accessor);
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

	private Object countOrGetDocumentsForDelete(CriteriaQuery query, ParametersParameterAccessor accessor) {

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

	public CriteriaQuery createQuery(ParametersParameterAccessor accessor) {
		return new TemplateQueryCreator(tree, accessor, mappingContext).createQuery();
	}
}
