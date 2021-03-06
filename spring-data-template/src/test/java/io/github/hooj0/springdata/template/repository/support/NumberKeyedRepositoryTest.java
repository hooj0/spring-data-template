package io.github.hooj0.springdata.template.repository.support;

import java.util.Collections;
import java.util.Set;

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
import io.github.hooj0.springdata.template.repository.domain.User;

/**
 * <b>function:</b> NumberKeyedRepository Test
 * @author hoojo
 * @createDate 2018年7月12日 上午11:40:32
 * @package io.github.hooj0.springdata.template.repository.support
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class NumberKeyedRepositoryTest {

	@Configuration
	@EnableTemplateRepositories(basePackageClasses = NumberKeyedTemplateRepo.class,
	considerNestedRepositories = true, 
	includeFilters = @Filter(pattern = ".*NumberKeyedTemplateRepo", type = FilterType.REGEX))
	public static class Config extends AbstractTemplateConfiguration {
		
		@Override
		protected Set<Class<?>> getInitialEntitySet() {
			return Collections.singleton(User.class);
		}
		
		@Bean
		MyTplTemplate myTplTemplate() throws ClassNotFoundException {
			return new MyTplTemplate("NumberKeyedRepositoryTest", this.templateConverter());
		}
	}
	
	@Autowired
	private NumberKeyedTemplateRepo repo;
	
	@Test
	public void testCRUD() {
		repo.index(new User());
		repo.refresh();
		repo.query("Xxxxx");
		repo.invoke("....");
	}
	
	interface NumberKeyedTemplateRepo extends TemplateRepository<User, Long> {
	}
}
