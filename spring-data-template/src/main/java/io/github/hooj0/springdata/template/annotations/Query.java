package io.github.hooj0.springdata.template.annotations;

import java.lang.annotation.*;

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
@Target(ElementType.METHOD)
@Documented
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
}
