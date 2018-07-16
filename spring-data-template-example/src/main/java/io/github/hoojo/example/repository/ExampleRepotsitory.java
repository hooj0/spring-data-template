package io.github.hoojo.example.repository;

import io.github.hooj0.springdata.template.annotations.Query;
import io.github.hooj0.springdata.template.repository.TemplateRepository;
import io.github.hoojo.example.entity.User;

/**
 * <b>function:</b> example repo
 * @author hoojo
 * @createDate 2018年7月16日 上午9:22:38
 * @file ExampleRepotisory.java
 * @package io.github.hoojo.example.repository
 * @project spring-data-template-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface ExampleRepotsitory extends TemplateRepository<User, Long> {

	User findByName(String name);
	
	User findBySexAndAge(boolean sex, int age);
	
	@Query("select * from User where name = ?0 and nick_name = ?1")
	User findBySQL(String name, String nickName);
}
