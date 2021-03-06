package io.github.hooj0.springdata.template.repository.query.simple;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang3.ClassUtils;
import org.springframework.dao.InvalidDataAccessApiUsageException;
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
import lombok.extern.slf4j.Slf4j;

/**
 * <b>function:</b> 通过实体对象构建CriteriaQuery查询
 * @author hoojo
 * @createDate 2018年7月9日 下午5:23:35
 * @file TemplateQueryCreator.java
 * @package io.github.hooj0.springdata.template.repository.query
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Slf4j
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
		
		PersistentPropertyPath<TemplatePersistentProperty> path = context.getPersistentPropertyPath(part.getProperty());
		return base.addCriteria(from(part, new Criteria(path.toDotPath(TemplatePersistentProperty.PropertyToFieldNameConverter.INSTANCE)), iterator));
	}

	@Override
	protected CriteriaQuery or(CriteriaQuery base, CriteriaQuery query) {
		
		return new CriteriaQuery(base.getCriteria().or(query.getCriteria()));
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
			case TRUE:
				return criteria.is(true);
			case FALSE:
				return criteria.is(false);
			case NEGATING_SIMPLE_PROPERTY:
				return criteria.is(parameters.next()).not();
			case REGEX:
				return criteria.expression(parameters.next().toString());
			case LIKE:
				return criteria.contains(parameters.next().toString());
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
			case SIMPLE_PROPERTY:
				Object obj = parameters.next();
				if (ClassUtils.isPrimitiveOrWrapper(obj.getClass())) {
					return criteria.in(asArray(obj));
				} else {
					return criteria.notIn(asArray(obj));
				}
			case WITHIN: {
				Object firstParameter = null;
				Object secondParameter = null;
				if (parameters.hasNext()) {
					firstParameter = parameters.next();
				}
				if (parameters.hasNext()) {
					secondParameter = parameters.next();
				}

				if (firstParameter instanceof String && secondParameter instanceof String)
					return criteria.within((String) firstParameter, (String) secondParameter);
			}
			case NEAR: {
				if (!parameters.hasNext()) {
					return criteria;
				}
				Object firstParameter = parameters.next();
				Object secondParameter = parameters.next();

				// "near" query can be the same query as the "within" query
				if (firstParameter instanceof String && secondParameter instanceof String)
					return criteria.within((String) firstParameter, (String) secondParameter);
			}

			log.debug("criteria: ", criteria.toString());
			default:
				throw new InvalidDataAccessApiUsageException("Illegal criteria found '" + type + "'.");
		}
	}
	
	private Object[] asArray(Object o) {
		if (o instanceof Collection) {
			return ((Collection<?>) o).toArray();
		} else if (o.getClass().isArray()) {
			return (Object[]) o;
		}
		return new Object[]{o};
	}
}
