package io.github.hoojo.example.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.github.hooj0.springdata.template.config.AbstractTemplateConfiguration;
import io.github.hooj0.springdata.template.core.MyTplTemplate;
import io.github.hooj0.springdata.template.repository.config.EnableTemplateRepositories;

/**
 * <b>function:</b>
 * @author hoojo
 * @createDate 2018年7月16日 上午10:03:44
 * @file ExampleRepoTest.java
 * @package io.github.hoojo.example.repository
 * @project spring-data-template-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ExampleRepoTest {

	@Configuration
	@EnableTemplateRepositories(basePackageClasses = ExampleRepotsitory.class)
	public static class Config extends AbstractTemplateConfiguration {
		
		@Bean
		MyTplTemplate myTplTemplate() throws ClassNotFoundException {
			return new MyTplTemplate("PartTreeQueryRepositoryTest", this.templateConverter());
		}
	}
	
	@Autowired
	private ExampleRepotsitory repo;
	
	@Test
	public void test1() {
		System.out.println(repo.invoke("test"));
	}
	
	@Test
	public void test2() {
		System.out.println(repo.findByName("jack"));
	}
	
	@Test
	public void test3() {
		System.out.println(repo.findBySexAndAge(true, 33));
	}
	
	@Test
	public void test4() {
		System.out.println(repo.findBySQL("jack Lee", "jack"));
	}
}
