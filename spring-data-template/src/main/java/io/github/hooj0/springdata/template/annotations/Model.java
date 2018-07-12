package io.github.hooj0.springdata.template.annotations;

import java.lang.annotation.*;

import org.springframework.data.annotation.Persistent;

/**
 * <b>function:</b> 实体模型注解
 * @author hoojo
 * @createDate 2018年7月10日 上午9:32:05
 * @file Model.java
 * @package io.github.hooj0.springdata.template.annotations
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Persistent
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Model {

	String indexName();

	String type() default "";

	short replicas() default 1;

	String refreshInterval() default "1s";

	boolean createIndex() default true;
}
