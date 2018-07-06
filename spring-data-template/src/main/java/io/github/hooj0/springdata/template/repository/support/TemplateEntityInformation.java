package io.github.hooj0.springdata.template.repository.support;

import org.springframework.data.repository.core.EntityInformation;

import io.github.hooj0.springdata.template.repository.query.TemplateEntityMetadata;

/**
 * <b>function:</b> 扩展EntityMetadata以添加查询实体实例信息的功能。
 * @author hoojo
 * @createDate 2018年7月4日 下午5:00:13
 * @file MappingTemplateEntityInformation.java
 * @package io.github.hooj0.springdata.template.repository.support
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface TemplateEntityInformation<T, ID> extends EntityInformation<T, ID>, TemplateEntityMetadata<T> {

	String getIdAttribute();

	String getIndexName();

	String getType();

	Long getVersion(T entity);

	String getParentId(T entity);
}
