package io.github.hooj0.springdata.template.repository.support;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.github.hooj0.springdata.template.config.AbstractTemplateConfiguration;
import io.github.hooj0.springdata.template.core.MyTplTemplate;
import io.github.hooj0.springdata.template.repository.TemplateRepository;
import io.github.hooj0.springdata.template.repository.config.EnableTemplateRepositories;
import io.github.hooj0.springdata.template.repository.support.SimpleTemplateRepositoryTest.SimplePerson;

/**
 * <b>function:</b> SimpleTemplateRepositoryIntegrationTest
 * @author hoojo
 * @createDate 2018年7月10日 下午4:33:51
 * @file SimpleTemplateRepositoryTest.java
 * @package io.github.hooj0.springdata.template.repository.support
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class SimpleTemplateRepositoryIntegrationTest {

	@Configuration
	@EnableTemplateRepositories(basePackageClasses = SimpleTemplateRepo.class,
		// 是否发现内部接口内部类
		considerNestedRepositories = true,
		includeFilters = @Filter(pattern = ".*SimpleTemplateRepo", type = FilterType.REGEX))
	public static class Config extends AbstractTemplateConfiguration {
		@Bean
		MyTplTemplate myTplTemplate() throws ClassNotFoundException {
			return new MyTplTemplate("SimpleTemplateRepo", this.templateConverter());
		}
	}
	
	@Autowired
	private SimpleTemplateRepo repository;
	
	@Test
	public void testInject() {
		System.out.println(repository);
	}
	
	@Test
	public void testIndex() {
		SimplePerson person = new SimplePerson();
		person.setId(1.1f);
		repository.index(person);
	}
	
	@Test
	public void testInvoke() {
		SimplePerson person = new SimplePerson();
		person.setId(1.1f);
		repository.invoke("a", "b", "c");
	}
	
	interface SimpleTemplateRepo extends TemplateRepository<SimplePerson, String> {
	}
}
