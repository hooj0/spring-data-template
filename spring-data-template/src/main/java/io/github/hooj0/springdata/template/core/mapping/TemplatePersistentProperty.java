package io.github.hooj0.springdata.template.core.mapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;

import org.springframework.context.ApplicationContextAware;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.lang.Nullable;

/**
 * <b>function:</b> 实现 实体属性的映射，要根据具体的 对象
 * @author hoojo
 * @createDate 2018年7月4日 下午5:27:34
 * @file TemplatePersistentProperty.java
 * @package io.github.hooj0.springdata.template.core.mapping
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface TemplatePersistentProperty extends PersistentProperty<TemplatePersistentProperty>, ApplicationContextAware {

	/**
	 * 获取属性名称 
	 * @author hoojo
	 * @createDate 2018年7月4日 下午5:33:45
	 * @return
	 */
	String getFieldName();
	
	/**
	 * 获取属性返回值类型
	 * @author hoojo
	 * @createDate 2018年7月4日 下午5:33:58
	 * @return
	 */
	String getDataType();

	/**
	 * 获取列名
	 * @author hoojo
	 * @createDate 2018年7月4日 下午5:34:11
	 * @return
	 */
	String getColumnName();
	
	/**
	 * 组合主键
	 */
	boolean isCompositePrimaryKey();

	/**
	 * 属性是否为分区键列。
	 */
	boolean isPartitionKeyColumn();

	/**
	 * 是否是主键列
	 * @see #isPartitionKeyColumn()
	 * @see #isClusterKeyColumn()
	 */
	boolean isPrimaryKeyColumn();

	/**
	 * 通过从属性类型派生的{@code annotationType}查找{@link AnnotatedType}。 
	 * 内省属性字段/访问器可以查找带注释的类型。 
	 * 类型参数中的类型注释会对集合/类似地图类型进行内省。
	 *
	 * @param annotationType must not be {@literal null}.
	 * @return the annotated type or {@literal null}.
	 * @since 2.0
	 */
	@Nullable
	AnnotatedType findAnnotatedType(Class<? extends Annotation> annotationType);
	
	boolean isScoreProperty();

	public enum PropertyToFieldNameConverter implements Converter<TemplatePersistentProperty, String> {
		INSTANCE;

		public String convert(TemplatePersistentProperty source) {
			return source.getFieldName();
		}
	}
}
