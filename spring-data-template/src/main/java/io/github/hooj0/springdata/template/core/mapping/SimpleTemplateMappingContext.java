package io.github.hooj0.springdata.template.core.mapping;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.mapping.context.AbstractMappingContext;
import org.springframework.data.mapping.model.Property;
import org.springframework.data.mapping.model.SimpleTypeHolder;
import org.springframework.data.util.TypeInformation;

/**
 * <b>function:</b>
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
