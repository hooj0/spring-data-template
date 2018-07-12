package io.github.hooj0.springdata.template.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.data.annotation.QueryAnnotation;

@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
/**
 * 在提供的元素上方的注释层次结构中查找指定注释类型的第一个注释，
 * 将注释的属性与注释层次结构的较低级别中的注释的匹配属性合并，
 * 并将结果合成回指定注释类型的注释。
 * 完全支持@AliasFor语义，包括单个注释和注释层次结构。
 */
@Query(count = true)
@QueryAnnotation
public @interface CountQuery {

	@AliasFor(annotation = Query.class)
	String value() default "";
}
