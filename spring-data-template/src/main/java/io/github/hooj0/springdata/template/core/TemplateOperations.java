package io.github.hooj0.springdata.template.core;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;

import io.github.hooj0.springdata.template.core.convert.TemplateConverter;

/**
 * <b>function:</b> 定义一些 “模板” 通用的操作接口：如增删改查、或其他诸如增删改查相关的方法。
 * <br/> 这里的操作或实现，需要关联结合 “Template” 的原生对象实例完成。
 * <br/> 接口中的方法方便实现 XxxTemplate 的模板类操作API
 * @author hoojo
 * @createDate 2018年7月4日 下午4:07:26
 * @file TemplateOperations.java
 * @package io.github.hooj0.springdata.template.core
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface TemplateOperations {

	TemplateConverter getTemplateConverter();
	
	/**
	 * 获取模板客户端 
	 * @author hoojo
	 * @createDate 2018年7月4日 下午4:09:29
	 */
	Object getClient();
	
	String getTemplateName();
	
	String getEntityName();
	
	boolean add(Object entity);
	
	boolean edit(Object entity);
	
	boolean remove(Serializable id);
	
	Object get(Serializable id);
	
	<T> List<T> list(Object entity);
	
	<T> Page<T> queryForPage(Object entity, Class<T> classes);
}
