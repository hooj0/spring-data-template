package io.github.hooj0.springdata.template.repository.support;

import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.support.PersistentEntityInformation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * 
 * <b>function:</b> 提供持久化实体信息的抽象基类
 * @author hoojo
 * @createDate 2018年7月4日 下午6:03:28
 * @file AbstractEntityInformation.java
 * @package io.github.hooj0.springdata.template.repository.support
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 * @param <T>
 * @param <ID>
 */
@RequiredArgsConstructor
public abstract class AbstractEntityInformation<T, ID> extends PersistentEntityInformation<T, ID> implements EntityInformation<T, ID> {

	private final @NonNull Class<T> domainClass;

	public AbstractEntityInformation(PersistentEntity<T, ?> entity) {
		super(entity);
		
		this.domainClass = entity.getType();
	}

	@Override
	public boolean isNew(T entity) {

		ID id = getId(entity);
		Class<ID> idType = getIdType();

		if (!idType.isPrimitive()) {
			return id == null;
		}

		if (id instanceof Number) {
			return ((Number) id).longValue() == 0L;
		}

		throw new IllegalArgumentException(String.format("Unsupported primitive id type %s!", idType));
	}

	@Override
	public Class<T> getJavaType() {
		return this.domainClass;
	}
}
