package io.github.hooj0.springdata.template.repository.support;

/**
 * <b>function:</b> TemplateEntityInformation 对象创造者接口
 * @author hoojo
 * @createDate 2018年7月5日 上午11:17:59
 * @file TemplateEntityInformationCreator.java
 * @package io.github.hooj0.springdata.template.repository.support
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface TemplateEntityInformationCreator {

	<T, ID> TemplateEntityInformation<T, ID> getEntityInformation(Class<T> domainClass);
}
