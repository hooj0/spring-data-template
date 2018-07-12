package io.github.hooj0.springdata.template.annotations;

import java.lang.annotation.*;

import org.springframework.data.annotation.QueryAnnotation;

/**
 * <b>function:</b> Query 查询注解，专门做查询的业务
 * @author hoojo
 * @createDate 2018年7月9日 下午4:40:55
 * @file Query.java
 * @package io.github.hooj0.springdata.template.annotations
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
// annotated 和 method 都可用
@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Documented
// 查询注解
@QueryAnnotation
public @interface Query {

	/**
	 * query to be used when executing query. May contain placeholders eg. ?0
	 * @return
	 */
	String value() default "";

	/**
	 * Named Query Named looked up by repository.
	 * @return
	 */
	String name() default "";
	
	/**
	 * count 查询
	 */
	boolean count() default false;
	
	boolean exists() default false;
}
