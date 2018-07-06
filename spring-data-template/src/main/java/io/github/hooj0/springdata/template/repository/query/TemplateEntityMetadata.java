package io.github.hooj0.springdata.template.repository.query;

import org.springframework.data.repository.core.EntityMetadata;

/**
 * <b>function:</b> 实体元数据接口，扩展{@link EntityMetadata} 以额外公开表名称持久化实体。
 * @author hoojo
 * @createDate 2018年7月4日 下午6:09:49
 * @file TemplateEntityMetadata.java
 * @package io.github.hooj0.springdata.template.repository.query
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface TemplateEntityMetadata<T> extends EntityMetadata<T> {

	String getTableName();
}
