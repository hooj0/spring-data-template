package io.github.hooj0.springdata.template.core.mapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mapping.Association;
import org.springframework.data.mapping.MappingException;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.model.AnnotationBasedPersistentProperty;
import org.springframework.data.mapping.model.Property;
import org.springframework.data.mapping.model.SimpleTypeHolder;
import org.springframework.util.Assert;

/**
 * <b>function:</b> 持久化实体属性映射实现
 * @author hoojo
 * @createDate 2018年7月5日 上午10:08:57
 * @file SimpleTemplatePersistentProperty.java
 * @package io.github.hooj0.springdata.template.core.mapping
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class SimpleTemplatePersistentProperty extends AnnotationBasedPersistentProperty<TemplatePersistentProperty> implements TemplatePersistentProperty {

	private static final Set<Class<?>> SUPPORTED_ID_TYPES = new HashSet<>();
	private static final Set<String> SUPPORTED_ID_PROPERTY_NAMES = new HashSet<>();
	
	private final boolean isScore; 

	private ApplicationContext context;
	
	static {
		SUPPORTED_ID_TYPES.add(String.class);
		SUPPORTED_ID_PROPERTY_NAMES.add("id");
		SUPPORTED_ID_PROPERTY_NAMES.add("documentId");
	}
	
	public SimpleTemplatePersistentProperty(Property property, PersistentEntity<?, TemplatePersistentProperty> owner, SimpleTypeHolder simpleTypeHolder) {
		super(property, owner, simpleTypeHolder);
		
		this.isScore = true; //isAnnotationPresent(Score.class);
		
		if (isScore && !Arrays.asList(Float.TYPE, Float.class).contains(getType())) {
			throw new MappingException(String.format("Score property %s must be either of type float or Float!", property.getName()));
		}
	}
	
	@Override
	public boolean isIdProperty() {
		return super.isIdProperty() || SUPPORTED_ID_PROPERTY_NAMES.contains(getFieldName());
	}
	
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}

	@Override
	public String getFieldName() {
		return getProperty().getName();
	}

	@Override
	public String getDataType() {
		return getProperty().getType().getName();
	}

	@Override
	public String getColumnName() {
		return super.getField().getName();
	}

	@Override
	public boolean isCompositePrimaryKey() {
		return super.isIdProperty();
	}

	@Override
	public boolean isPartitionKeyColumn() {
		return false;
	}

	@Override
	public boolean isPrimaryKeyColumn() {
		return super.isIdProperty();
	}

	@Override
	public AnnotatedType findAnnotatedType(Class<? extends Annotation> annotationType) {
		
		return (AnnotatedType) super.findAnnotation(annotationType);
	}

	@Override
	public boolean isScoreProperty() {
		return isScore;
	}

	@Override
	protected Association<TemplatePersistentProperty> createAssociation() {
		
		Assert.isTrue(context != null, "contxt not null");
		return null;
	}
}
