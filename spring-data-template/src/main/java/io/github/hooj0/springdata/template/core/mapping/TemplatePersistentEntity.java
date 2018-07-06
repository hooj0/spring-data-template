package io.github.hooj0.springdata.template.core.mapping;

import org.springframework.data.mapping.PersistentEntity;

/**
 * <b>function:</b> 持久化实体对象抽象接口
 * @author hoojo
 * @createDate 2018年7月4日 下午5:43:43
 * @file TemplatePersistentEntity.java
 * @package io.github.hooj0.springdata.template.core.mapping
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface TemplatePersistentEntity<T> extends PersistentEntity<T, TemplatePersistentProperty> {

	boolean isCompositePrimaryKey();
	
	String getTableName();
	
	boolean isTupleType();
	
	String getRefreshInterval();

	String getParentType();
	
	long getVersion();
	
	TemplatePersistentProperty getParentIdProperty();
}
