package io.github.hooj0.springdata.template.repository.query.simple;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.util.Assert;

import io.github.hooj0.springdata.template.core.TemplateOperations;
import io.github.hooj0.springdata.template.core.convert.DateTimeConverter;
import io.github.hooj0.springdata.template.core.query.StringQuery;
import io.github.hooj0.springdata.template.repository.query.AbstractTemplateRepositoryQuery;
import io.github.hooj0.springdata.template.repository.query.TemplateQueryMethod;
import lombok.extern.slf4j.Slf4j;

/**
 * <b>function:</b> 字符串查询repo实现
 * @author hoojo
 * @createDate 2018年7月9日 下午4:55:50
 * @file TemplateStringQuery.java
 * @package io.github.hooj0.springdata.template.repository.query
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Slf4j
public class TemplateStringQuery extends AbstractTemplateRepositoryQuery {

	private static final Pattern PARAMETER_PLACEHOLDER = Pattern.compile("\\?(\\d+)");
	private String query;

	private final GenericConversionService conversionService = new GenericConversionService();

	{
		if (!conversionService.canConvert(java.util.Date.class, String.class)) {
			conversionService.addConverter(DateTimeConverter.JavaDateConverter.INSTANCE);
		}
		if (!conversionService.canConvert(org.joda.time.ReadableInstant.class, String.class)) {
			conversionService.addConverter(DateTimeConverter.JodaDateTimeConverter.INSTANCE);
		}
		if (!conversionService.canConvert(org.joda.time.LocalDateTime.class, String.class)) {
			conversionService.addConverter(DateTimeConverter.JodaLocalDateTimeConverter.INSTANCE);
		}
	}

	public TemplateStringQuery(TemplateQueryMethod queryMethod, TemplateOperations operations, String query) {
		super(queryMethod, operations);
		Assert.notNull(query, "Query cannot be empty");
		this.query = query;
	}

	@Override
	public Object execute(Object[] parameters) {
		ParametersParameterAccessor accessor = new ParametersParameterAccessor(queryMethod.getParameters(), parameters);
		log.info("execute accessor: {}", accessor);
		
		StringQuery stringQuery = createQuery(accessor);
		log.info("query stringQuery: {}", stringQuery);
		
		if (queryMethod.isPageQuery()) { // page 查询
			stringQuery.setPageable(accessor.getPageable());
			return operations.queryForPage(stringQuery, queryMethod.getEntityInformation().getJavaType());
		} else if (queryMethod.isCollectionQuery()) { // 集合查询
			if (accessor.getPageable().isPaged()) {
				stringQuery.setPageable(accessor.getPageable());
			}
			return operations.queryForList(stringQuery, queryMethod.getEntityInformation().getJavaType());
		} else if (queryMethod.hasAnnotatedQuery() && StringUtils.contains(queryMethod.getNamedQueryName(), "count")) {
			return operations.count(null, queryMethod.getEntityInformation().getJavaType());
		}

		// 对象查询
		return operations.queryForObject(stringQuery, queryMethod.getEntityInformation().getJavaType());
	}

	protected StringQuery createQuery(ParametersParameterAccessor parameterAccessor) {
		String queryString = replacePlaceholders(this.query, parameterAccessor);
		
		log.info("query string: {}", queryString);
		
		return new StringQuery(queryString);
	}

	// 替换占位符，将其替换为正确的参数
	private String replacePlaceholders(String input, ParametersParameterAccessor accessor) {
		Matcher matcher = PARAMETER_PLACEHOLDER.matcher(input);
		
		String result = input;
		while (matcher.find()) {
			String group = matcher.group();
			int index = Integer.parseInt(matcher.group(1));
			result = result.replace(group, getParameterWithIndex(accessor, index));
		}
		
		return result;
	}

	// 通过参数占位符获取参数名称
	private String getParameterWithIndex(ParametersParameterAccessor accessor, int index) {
		Object parameter = accessor.getBindableValue(index);
		if (parameter == null) {
			return "null";
		}
		
		// 参数转换
		if (conversionService.canConvert(parameter.getClass(), String.class)) {
			return conversionService.convert(parameter, String.class);
		}
		
		return parameter.toString();
	}
}
