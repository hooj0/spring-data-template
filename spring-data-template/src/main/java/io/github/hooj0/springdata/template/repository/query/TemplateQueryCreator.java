package io.github.hooj0.springdata.template.repository.query;

import java.util.Iterator;

import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PersistentPropertyPath;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.repository.query.ParameterAccessor;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.data.repository.query.parser.PartTree;

import io.github.hooj0.springdata.template.core.mapping.TemplatePersistentProperty;
import io.github.hooj0.springdata.template.core.query.Criteria;
import io.github.hooj0.springdata.template.core.query.CriteriaQuery;

/**
 * <b>function:</b> 通过实体对象构建查询
 * @author hoojo
 * @createDate 2018年7月9日 下午5:23:35
 * @file TemplateQueryCreator.java
 * @package io.github.hooj0.springdata.template.repository.query
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class TemplateQueryCreator extends AbstractQueryCreator<CriteriaQuery, CriteriaQuery> {

	private final MappingContext<?, TemplatePersistentProperty> context;

	public TemplateQueryCreator(PartTree tree, ParameterAccessor parameters, MappingContext<?, TemplatePersistentProperty> context) {
		super(tree, parameters);
		this.context = context;
	}

	public TemplateQueryCreator(PartTree tree, MappingContext<?, TemplatePersistentProperty> context) {
		super(tree);
		this.context = context;
	}

	@Override
	protected CriteriaQuery create(Part part, Iterator<Object> iterator) {
		PersistentPropertyPath<TemplatePersistentProperty> path = context.getPersistentPropertyPath(part.getProperty());
		return new CriteriaQuery(from(part, new Criteria(path.toDotPath(TemplatePersistentProperty.PropertyToFieldNameConverter.INSTANCE)), iterator));
	}

	@Override
	protected CriteriaQuery and(Part part, CriteriaQuery base, Iterator<Object> iterator) {
		if (base == null) {
			return create(part, iterator);
		}
		PersistentPropertyPath<TemplatePersistentProperty> path = context
				.getPersistentPropertyPath(part.getProperty());
		return base.addCriteria(from(part, new Criteria(path.toDotPath(TemplatePersistentProperty.PropertyToFieldNameConverter.INSTANCE)), iterator));
	}

	@Override
	protected CriteriaQuery or(CriteriaQuery base, CriteriaQuery query) {
		//return new CriteriaQuery(base.getCriteria().or(query.getCriteria()));
		return query;
	}

	@Override
	protected CriteriaQuery complete(CriteriaQuery query, Sort sort) {
		if (query == null) {
			return null;
		}
		return query.addSort(sort);
	}

	private Criteria from(Part part, Criteria instance, Iterator<?> parameters) {
		Part.Type type = part.getType();

		Criteria criteria = instance;
		if (criteria == null) {
			criteria = new Criteria();
		}
		
		switch (type) {
			/*
			case TRUE:
				return criteria.is(true);
			case FALSE:
				return criteria.is(false);
			case NEGATING_SIMPLE_PROPERTY:
				return criteria.is(parameters.next()).not();
			case REGEX:
				return criteria.expression(parameters.next().toString());
			case LIKE:
			case STARTING_WITH:
				return criteria.startsWith(parameters.next().toString());
			case ENDING_WITH:
				return criteria.endsWith(parameters.next().toString());
			case CONTAINING:
				return criteria.contains(parameters.next().toString());
			case GREATER_THAN:
				return criteria.greaterThan(parameters.next());
			case AFTER:
			case GREATER_THAN_EQUAL:
				return criteria.greaterThanEqual(parameters.next());
			case LESS_THAN:
				return criteria.lessThan(parameters.next());
			case BEFORE:
			case LESS_THAN_EQUAL:
				return criteria.lessThanEqual(parameters.next());
			case BETWEEN:
				return criteria.between(parameters.next(), parameters.next());
			case IN:
				return criteria.in(asArray(parameters.next()));
			case NOT_IN:
				return criteria.notIn(asArray(parameters.next()));
			*/
			default:
				throw new RuntimeException("Illegal criteria found '" + type + "'.");
		}
	}
}
