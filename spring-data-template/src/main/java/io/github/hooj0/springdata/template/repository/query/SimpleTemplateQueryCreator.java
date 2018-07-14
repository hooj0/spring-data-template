package io.github.hooj0.springdata.template.repository.query;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PersistentPropertyPath;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.repository.query.ParameterAccessor;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.data.repository.query.parser.PartTree;

import com.google.common.collect.Lists;

import io.github.hooj0.springdata.template.core.mapping.TemplatePersistentProperty;
import lombok.extern.slf4j.Slf4j;

/**
 * <b>function:</b> 通过实体对象构建SQL条件查询
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
public class SimpleTemplateQueryCreator extends AbstractQueryCreator<String, String> {

	private final MappingContext<?, TemplatePersistentProperty> context;

	public SimpleTemplateQueryCreator(PartTree tree, ParameterAccessor parameters, MappingContext<?, TemplatePersistentProperty> context) {
		super(tree, parameters);
		this.context = context;
	}

	public SimpleTemplateQueryCreator(PartTree tree, MappingContext<?, TemplatePersistentProperty> context) {
		super(tree);
		this.context = context;
	}

	@Override
	protected String create(Part part, Iterator<Object> iterator) {
		PersistentPropertyPath<TemplatePersistentProperty> path = context.getPersistentPropertyPath(part.getProperty());
		
		return new String(from(part, new String(path.toDotPath(TemplatePersistentProperty.PropertyToFieldNameConverter.INSTANCE)), iterator));
	}

	@Override
	protected String and(Part part, String base, Iterator<Object> iterator) {
		if (base == null) {
			return create(part, iterator);
		}
		
		PersistentPropertyPath<TemplatePersistentProperty> path = context.getPersistentPropertyPath(part.getProperty());
		return base += " AND " + from(part, new String(path.toDotPath(TemplatePersistentProperty.PropertyToFieldNameConverter.INSTANCE)), iterator);
	}

	@Override
	protected String or(String base, String query) {
		
		return base + " OR " + query;
	}

	@Override
	protected String complete(String query, Sort sort) {
		
		if (query == null) {
			return null;
		}
		
		if (!sort.isUnsorted()) {
			return query + " ORDER BY " + sort;
		}
		return query;
	}

	private String from(Part part, String instance, Iterator<?> parameters) {
		Part.Type type = part.getType();

		String criteria = instance;
		if (criteria == null) {
			criteria = new String();
		}
		String property = part.getProperty().getSegment();
		
		switch (type) {
			case TRUE:
				return property + " Is True";
			case FALSE:
				return property + " Is FALSE";
			case NEGATING_SIMPLE_PROPERTY:
				return property + " != " + parameters.next();
			case REGEX:
				return property + " ^" + (parameters.next().toString()) + "/";
			case LIKE:
				return property + " LIKE %" + parameters.next() + "%";
			case STARTING_WITH:
				return property + " LIKE " + parameters.next() + "%";
			case ENDING_WITH:
				return property + " LIKE %" + parameters.next();
			case CONTAINING:
				return property + " CONTAINING %" + parameters.next() + "%";
			case GREATER_THAN:
				return property + " > " + parameters.next();
			case AFTER:
				return property + " -> " + parameters.next();
			case GREATER_THAN_EQUAL:
				return property + " >= " + parameters.next();
			case LESS_THAN:
				return property + " < " + parameters.next();
			case BEFORE:
				return property + " <- " + parameters.next();
			case LESS_THAN_EQUAL:
				return property + " <= " + parameters.next();
			case BETWEEN:
				return property + " BETWEEN" + parameters.next() + " and " + parameters.next();
			case IN:
				return property + " IN(" + asArray(parameters.next()) + ")";
			case NOT_IN:
				return property + " NOT IN(" + asArray(parameters.next()) + ")";
			case SIMPLE_PROPERTY:
				return property + " = " + parameters.next();
			case WITHIN: {
				return property + " WITHIN (" + join(parameters) + ")";
			}
			case NEAR: {
				return property + " NEAR (" + join(parameters) + ")";
			}
			default:
				log.debug("criteria: ", criteria.toString());
				throw new InvalidDataAccessApiUsageException("Illegal criteria found '" + type + "'.");
		}
	}
	
	private String join(Iterator<?> parameters) {
		List<String> list = Lists.newArrayList();
		while (parameters.hasNext()) {
			list.add(parameters.next().toString());
		}
		
		return StringUtils.join(list, ", ");
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
