package io.github.hooj0.springdata.template.repository;

import java.io.Serializable;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

/**
 * <b>function:</b> 模板repo接口，提供基本的CRUD或者通用的接口方法。
 * <br/>添加 @NoRepositoryBean 注解，不被repo scan扫描到
 * @author hoojo
 * @createDate 2018年7月4日 下午2:47:23
 * @file TemplateRepository.java
 * @package io.github.hooj0.springdata.template.repository
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@NoRepositoryBean
public interface TemplateRepository<T, ID extends Serializable> extends Repository<T, ID> {

	boolean invoke(String... args);
	
	String query(String params);
	
	void refresh();
	
	<S extends T> S index(S entity);
	
	Class<T> getEntityClass();
}
