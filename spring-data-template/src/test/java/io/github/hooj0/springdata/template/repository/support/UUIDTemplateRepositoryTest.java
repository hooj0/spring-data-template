package io.github.hooj0.springdata.template.repository.support;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.github.hooj0.springdata.template.repository.domain.User;

/**
 * <b>function:</b> UUIDTemplateRepository Test
 * @author hoojo
 * @createDate 2018年7月12日 上午11:40:32
 * @file UUIDTemplateRepositoryTest.java
 * @package io.github.hooj0.springdata.template.repository.support
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/simple-repository-test.xml")
public class UUIDTemplateRepositoryTest {

	@Autowired
	private UUIDTemplateRepository<User> repo;
	
	@Test
	public void testCRUD() {
		repo.index(new User());
		repo.refresh();
		repo.query("Xxxxx");
		repo.invoke("....");
	}
}
