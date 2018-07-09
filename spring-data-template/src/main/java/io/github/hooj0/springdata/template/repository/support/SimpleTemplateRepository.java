package io.github.hooj0.springdata.template.repository.support;

import java.io.Serializable;

import io.github.hooj0.springdata.template.core.TemplateOperations;

/**
 * <b>function:</b> 任意继承 Serializable Id key 类型 repo
 * @author hoojo
 * @createDate 2018年7月5日 上午11:45:09
 * @file SimpleTemplateRepository.java
 * @package io.github.hooj0.springdata.template.repository.support
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class SimpleTemplateRepository<T, ID extends Serializable> extends AbstractTemplateRepository<T, ID> {

	public SimpleTemplateRepository() {
		super();
	}

	public SimpleTemplateRepository(TemplateEntityInformation<T, ID> metadata, TemplateOperations operations) {
		super(metadata, operations);
	}

	public SimpleTemplateRepository(TemplateOperations operations) {
		super(operations);
	}

	@Override
	protected String stringIdRepresentation(ID id) {
		return (id != null) ? id.toString() : null;
	}
}
