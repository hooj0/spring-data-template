package io.github.hooj0.springdata.template.core.mapping;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.mapping.context.AbstractMappingContext;
import org.springframework.data.mapping.model.Property;
import org.springframework.data.mapping.model.SimpleTypeHolder;
import org.springframework.data.util.TypeInformation;

/**
 * 用于构建映射元数据的基类，从而创建PersistentEntity和PersistentProperty的实例。<br/>
 * 该实现使用ReentrantReadWriteLock确保PersistentEntity在从外部访问之前完全填充。<br/>
 * 通过MappingContext能获取到当前映射实体的persistentEntity基本信息，<br/>
 * PersistentEntity 能获取属性PersistentProperty信息 和 类型的TypeInformation。
 * @author hoojo
 * @createDate 2018年7月5日 上午10:44:05
 * @file SimpleTemplateMappingContext.java
 * @package io.github.hooj0.springdata.template.core.mapping
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class SimpleTemplateMappingContext extends AbstractMappingContext<SimpleTemplatePersistentEntity<?>, TemplatePersistentProperty> implements ApplicationContextAware {

	private ApplicationContext context;
	
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}

	@Override
	protected <T> SimpleTemplatePersistentEntity<?> createPersistentEntity(TypeInformation<T> typeInformation) {
		final SimpleTemplatePersistentEntity<T> persistentEntity = new SimpleTemplatePersistentEntity<>(typeInformation);
		
		if (context != null) {
			persistentEntity.setApplicationContext(context);
		}
		return persistentEntity;
	}

	@Override
	protected TemplatePersistentProperty createPersistentProperty(Property property, SimpleTemplatePersistentEntity<?> owner, SimpleTypeHolder simpleTypeHolder) {
		return new SimpleTemplatePersistentProperty(property, owner, simpleTypeHolder);
	}
}
