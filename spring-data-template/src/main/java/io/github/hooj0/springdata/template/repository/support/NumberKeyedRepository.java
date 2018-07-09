package io.github.hooj0.springdata.template.repository.support;

import io.github.hooj0.springdata.template.core.TemplateOperations;

/**
 * <b>function:</b> Number 类型 repo
 * @author hoojo
 * @createDate 2018年7月4日 下午4:53:44
 * @file NumberKeyedRepository.java
 * @package io.github.hooj0.springdata.template.repository.support
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class NumberKeyedRepository<T, ID extends Number> extends AbstractTemplateRepository<T, ID> {

	public NumberKeyedRepository() {
		super();
	}

	public NumberKeyedRepository(TemplateEntityInformation<T, ID> metadata, TemplateOperations operations) {
		super(metadata, operations);
	}

	public NumberKeyedRepository(TemplateOperations operations) {
		super(operations);
	}

	@Override
	protected String stringIdRepresentation(ID id) {
		return (id != null) ? id.toString() : null;
	}
}
