package io.github.hooj0.springdata.template.repository.complex;

import java.util.Collections;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.github.hooj0.springdata.template.config.AbstractTemplateConfiguration;
import io.github.hooj0.springdata.template.core.MyTplTemplate;
import io.github.hooj0.springdata.template.core.TemplateOperations;
import io.github.hooj0.springdata.template.repository.config.EnableTemplateRepositories;
import io.github.hooj0.springdata.template.repository.domain.User;

/**
 * <b>function:</b> ComplexCustomMethodRepositoryTest 自定义 repo实现测试
 * @author hoojo
 * @createDate 2018年7月13日 上午11:17:59
 * @file ComplexCustomMethodRepositoryTest.java
 * @package io.github.hooj0.springdata.template.repository.complex
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ComplexCustomMethodRepositoryTest {

	@Configuration
	@EnableTemplateRepositories(basePackageClasses = ComplexTemplateRepository.class,
	considerNestedRepositories = true, 
	includeFilters = @Filter(pattern = ".*ComplexTemplateRepository", type = FilterType.REGEX))
	public static class Config extends AbstractTemplateConfiguration {
		
		@Override
		protected Set<Class<?>> getInitialEntitySet() {
			return Collections.singleton(User.class);
		}
		
		@Bean("myTplTemplate")
		public MyTplTemplate myTplTemplate() throws ClassNotFoundException {
			return new MyTplTemplate("XxxxX", this.templateConverter());
		}
	}
	
	@Autowired
	private ComplexTemplateRepository repo;
	
	@Autowired
	@Qualifier("myTplTemplate")
	private TemplateOperations operations;
	
	@Test
	public void testSetup() {
		operations.invoke("Xxxx");
		
		System.out.println(repo.doSomethingSpecial());
		repo.index(new User());
	}
}
