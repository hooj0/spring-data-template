package io.github.hooj0.springdata.template.repository.support;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.hooj0.springdata.template.core.TemplateOperations;
import io.github.hooj0.springdata.template.repository.TemplateRepository;
import lombok.NoArgsConstructor;

/**
 * <b>function:</b> 抽象的 repo 实现类型，方便其他的实现 repo 继承扩展
 * @author hoojo
 * @createDate 2018年7月4日 下午3:28:51
 * @file AbstractTemplateRepository.java
 * @package io.github.hooj0.springdata.template.repository.support
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@NoArgsConstructor
public abstract class AbstractTemplateRepository<T, ID extends Serializable> implements TemplateRepository<T, ID> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTemplateRepository.class);
	
	private TemplateEntityInformation<T, ID> entityInformation;
	private TemplateOperations operations;
	
	protected Class<T> entityClass;
	
	public AbstractTemplateRepository(TemplateOperations operations) {
		this.operations = operations;
	}
	
	public AbstractTemplateRepository(TemplateEntityInformation<T, ID> entityInformation, TemplateOperations operations) {
		this.entityInformation = entityInformation;
		this.operations = operations;
	}

	// 获取 唯一 Id 对象
	protected abstract String stringIdRepresentation(ID id);
	
	protected ID extractIdFromBean(T entity) {
		return entityInformation.getId(entity);
	}
	
	@Override
	public boolean invoke(String... args) {
		LOGGER.info("exec invoke method, args: {}", new Object[] { args });
		
		return operations.invoke(args);
	}

	@Override
	public String query(String params) {
		LOGGER.info("exec query method, params: {}", params);
		
		return operations.query(params);
	}

	@Override
	public void refresh() {
		LOGGER.info("exec refresh method");
	}

	@Override
	public <S extends T> S index(S entity) {
		// 获取主键id
		String keyId = stringIdRepresentation(extractIdFromBean(entity));
		
		LOGGER.info("exec index method, entity: {}， ID： {}", entity, keyId);
		
		return operations.index(entity);
	}

	@Override
	public Class<T> getEntityClass() {
		if (!isEntityClassSet()) {
			try {
				this.entityClass = resolveReturnedClassFromGenericType();
			} catch (Exception e) {
				throw new RuntimeException("Unable to resolve EntityClass. Please use according setter!", e);
			}
		}
		return entityClass;
	}
	
	private boolean isEntityClassSet() {
		return entityClass != null;
	}
	
	@SuppressWarnings("unchecked")
	private Class<T> resolveReturnedClassFromGenericType() {
		ParameterizedType parameterizedType = resolveReturnedClassFromGenericType(getClass());
		return (Class<T>) parameterizedType.getActualTypeArguments()[0];
	}
	
	private ParameterizedType resolveReturnedClassFromGenericType(Class<?> clazz) {
		Object genericSuperclass = clazz.getGenericSuperclass();
		if (genericSuperclass instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
			Type rawtype = parameterizedType.getRawType();
			if (SimpleTemplateRepository.class.equals(rawtype)) {
				return parameterizedType;
			}
		}
		return resolveReturnedClassFromGenericType(clazz.getSuperclass());
	}
}
