package io.github.hooj0.springdata.template.annotations;

import java.lang.annotation.*;

import org.springframework.data.annotation.Persistent;


@Persistent
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Setting {

	String settingPath() default "";
}
