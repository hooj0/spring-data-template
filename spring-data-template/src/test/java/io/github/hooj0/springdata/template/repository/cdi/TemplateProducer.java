package io.github.hooj0.springdata.template.repository.cdi;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import io.github.hooj0.springdata.template.core.MyTplTemplate;
import io.github.hooj0.springdata.template.core.TemplateOperations;

/**
 * <b>function:</b> Template Producer 生产者
 * @author hoojo
 * @createDate 2018年7月13日 下午2:59:16
 * @file TemplateProducer.java
 * @package io.github.hooj0.springdata.template.repository.cdi
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
// 指定bean是应用程序作用域。
@ApplicationScoped
public class TemplateProducer {

	// 加载到Application中，其他应用可以注入该对象
	@Produces
	public MyTplTemplate createMyTplTemplate() {
		return new MyTplTemplate("template client");
	}
	
	@Produces
	// 设置注入依赖的注解，在TemplateRepositoryExtension 中需要注入TemplateOperations对象
	@OtherQualifier
	@PersonDB
	public TemplateOperations createQualifiedTemplate() {
		return new MyTplTemplate("template client");
	}
	
	@PreDestroy
	public void shutdown() {
		// remove everything to avoid conflicts with other tests in case server not shut down properly
	}
}
