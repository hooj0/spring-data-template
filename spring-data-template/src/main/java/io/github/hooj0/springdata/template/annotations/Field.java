package io.github.hooj0.springdata.template.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.github.hooj0.springdata.template.enums.FieldType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Inherited
public @interface Field {

	FieldType type() default FieldType.Auto;
	
	boolean index() default true;

	String pattern() default "";

	boolean store() default false;

	String[] ignoreFields() default {};

	boolean includeInParent() default false;
}
