package io.github.hooj0.springdata.template.core.mapping;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.BeanFactoryAccessor;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.data.mapping.model.BasicPersistentEntity;
import org.springframework.data.util.TypeInformation;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;

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
public class SimpleTemplatePersistentEntity<T> extends BasicPersistentEntity<T, TemplatePersistentProperty> implements TemplatePersistentEntity<T>, ApplicationContextAware {

	private final StandardEvaluationContext context;
	private final SpelExpressionParser parser;
	
	private String indexName;
	private String indexType;
	private String refreshInterval;
	private String parentType;
	private TemplatePersistentProperty parentIdProperty;
	private TemplatePersistentProperty scoreProperty;
	
	public SimpleTemplatePersistentEntity(TypeInformation<T> information) {
		super(information);
		
		this.context = new StandardEvaluationContext();
		this.parser = new SpelExpressionParser();

		// 从注解中获取数据，对entity 属性进行填充数据
		Class<T> clazz = information.getType();
		/*if (clazz.isAnnotationPresent(Document.class)) {
			Document document = clazz.getAnnotation(Document.class);
			Assert.hasText(document.indexName(), " Unknown indexName. Make sure the indexName is defined. e.g @Document(indexName=\"foo\")");
			this.indexName = document.indexName();
			this.indexType = hasText(document.type()) ? document.type() : clazz.getSimpleName().toLowerCase(Locale.ENGLISH);
			this.refreshInterval = document.refreshInterval();
		}
		
		if (clazz.isAnnotationPresent(Setting.class)) {
			this.settingPath = typeInformation.getType().getAnnotation(Setting.class).settingPath();
		}
		*/
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

		/*Parent annotation = property.findAnnotation(Parent.class);

		if (annotation != null) {
			Assert.isNull(this.parentIdProperty, "Only one field can hold a @Parent annotation");
			Assert.isNull(this.parentType, "Only one field can hold a @Parent annotation");
			Assert.isTrue(property.getType() == String.class, "Parent ID property should be String");
			
			this.parentIdProperty = property;
			this.parentType = annotation.type();
		}*/

		if (property.isVersionProperty()) {
			Assert.isTrue(property.getType() == Long.class, "Version property must be of type Long!");
		}

		if (property.isScoreProperty()) {
			System.out.println(property);
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
}
