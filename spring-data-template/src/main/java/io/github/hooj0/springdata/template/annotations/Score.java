package io.github.hooj0.springdata.template.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.data.annotation.ReadOnlyProperty;

/**
 * <b>function:</b> 定义一个属性版本的注解
 * @author hoojo
 * @createDate 2018年7月10日 上午9:53:59
 * @package io.github.hooj0.springdata.template.annotations
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Inherited
@ReadOnlyProperty
public @interface Score {
}
