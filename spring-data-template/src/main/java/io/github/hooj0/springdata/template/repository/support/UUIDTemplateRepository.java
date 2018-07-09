package io.github.hooj0.springdata.template.repository.support;

import java.util.UUID;

import io.github.hooj0.springdata.template.core.TemplateOperations;

/**
 * <b>function:</b> UUID key repo
 * 
 * @author hoojo
 * @createDate 2018年7月5日 上午11:45:17
 * @file UUIDTemplateRepository.java
 * @package io.github.hooj0.springdata.template.repository.support
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class UUIDTemplateRepository<T> extends AbstractTemplateRepository<T, UUID> {

	public UUIDTemplateRepository() {
		super();
	}

	public UUIDTemplateRepository(TemplateEntityInformation<T, UUID> metadata, TemplateOperations operations) {
		super(metadata, operations);
	}

	public UUIDTemplateRepository(TemplateOperations operations) {
		super(operations);
	}

	@Override
	protected String stringIdRepresentation(UUID id) {
		return (id != null) ? id.toString() : null;
	}
}
