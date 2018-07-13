package io.github.hooj0.springdata.template.core.mapping;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.BeanFactoryAccessor;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.data.mapping.MappingException;
import org.springframework.data.mapping.model.BasicPersistentEntity;
import org.springframework.data.util.TypeInformation;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;

import io.github.hooj0.springdata.template.annotations.Field;
import io.github.hooj0.springdata.template.annotations.Model;
import io.github.hooj0.springdata.template.annotations.Score;
import io.github.hooj0.springdata.template.annotations.Setting;
import lombok.extern.slf4j.Slf4j;

/**
 * <b>function:</b> 持久化实体映射实现
 * @author hoojo
 * @createDate 2018年7月4日 下午6:51:58
 * @file SimpleTemplatePersistentEntity.java
 * @package io.github.hooj0.springdata.template.core.mapping
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Slf4j
public class SimpleTemplatePersistentEntity<T> extends BasicPersistentEntity<T, TemplatePersistentProperty> implements TemplatePersistentEntity<T>, ApplicationContextAware {

	private final StandardEvaluationContext context;
	private final SpelExpressionParser parser;
	
	private String indexName;
	private String indexType;
	private String refreshInterval;
	private String parentType;
	private String settingPath;

	private TemplatePersistentProperty parentIdProperty;
	private TemplatePersistentProperty scoreProperty;
	
	public SimpleTemplatePersistentEntity(TypeInformation<T> information) {
		super(information);
		
		this.context = new StandardEvaluationContext();
		this.parser = new SpelExpressionParser();

		// 从注解中获取数据，对entity 属性进行填充数据
		Class<T> clazz = information.getType();
		if (clazz.isAnnotationPresent(Model.class)) {
			Model model = clazz.getAnnotation(Model.class);
			Assert.hasText(model.indexName(), " Unknown indexName. Make sure the indexName is defined. e.g @Model(indexName=\"foo\")");
			log.debug("@Model: {}", model);
			
			this.indexName = model.indexName();
			this.indexType = model.type();
			this.refreshInterval = model.refreshInterval();
		}
		
		if (clazz.isAnnotationPresent(Setting.class)) {
			this.settingPath = clazz.getAnnotation(Setting.class).settingPath();
		}
		
		log.debug("indexType: {}, settingPath: {}", indexType, settingPath);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context.addPropertyAccessor(new BeanFactoryAccessor());
		context.setBeanResolver(new BeanFactoryResolver(applicationContext));
		context.setRootObject(applicationContext);
	}
	
	@Override
	public void addPersistentProperty(TemplatePersistentProperty property) {
		super.addPersistentProperty(property);

		Field annotation = property.findAnnotation(Field.class);

		if (annotation != null) {
			this.parentIdProperty = property;
			this.parentType = annotation.pattern();
		}

		if (property.isVersionProperty()) {
			Assert.isTrue(property.getType() == Long.class, "Version property must be of type Long!");
		}

		Score annotationScore = property.findAnnotation(Score.class);
		if (annotationScore != null) {
			log.debug("annotationScore: {}", annotationScore);
		}
		
		if (property.isScoreProperty()) {
			
			TemplatePersistentProperty scoreProperty = this.scoreProperty;
			if (scoreProperty != null) {
				throw new MappingException(String.format("Attempt to add score property %s but already have property %s registered as version. Check your mapping configuration!", property.getField(), scoreProperty.getField()));
			}

			this.scoreProperty = property;
		}
	}

	@Override
	public boolean isCompositePrimaryKey() {
		return false;
	}

	@Override
	public String getTableName() {
		// 从表达式中获取设置的值
		Expression expression = parser.parseExpression(indexName, ParserContext.TEMPLATE_EXPRESSION);
		return expression.getValue(context, String.class);
	}

	@Override
	public boolean isTupleType() {
		return false;
	}

	@Override
	public String getRefreshInterval() {
		return this.refreshInterval;
	}

	@Override
	public String getParentType() {
		return this.parentType;
	}

	@Override
	public long getVersion() {
		return 1L;
	}
	
	@Override
	public TemplatePersistentProperty getParentIdProperty() {
		return this.parentIdProperty;
	}

	@Override
	public String getIndexName() {
		return this.indexName;
	}

	@Override
	public String getIndexType() {
		return this.indexType;
	}
}
