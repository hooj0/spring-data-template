package io.github.hooj0.springdata.template.repository.query;

import java.util.Collection;
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

import io.github.hooj0.springdata.template.annotations.CountQuery;
import io.github.hooj0.springdata.template.annotations.Query;
import io.github.hooj0.springdata.template.config.AbstractTemplateConfiguration;
import io.github.hooj0.springdata.template.core.MyTplTemplate;
import io.github.hooj0.springdata.template.repository.TemplateRepository;
import io.github.hooj0.springdata.template.repository.config.EnableTemplateRepositories;
import io.github.hooj0.springdata.template.repository.query.TemplatePartQueryTest.AddressType;
import io.github.hooj0.springdata.template.repository.query.TemplatePartQueryTest.AllowFiltering;
import io.github.hooj0.springdata.template.repository.query.TemplatePartQueryTest.Person;
import io.github.hooj0.springdata.template.repository.query.TemplatePartQueryTest.PersonProjection;


/**
 * <b>function:</b> PartTreeQueryRepositoryTest
 * @author hoojo
 * @createDate 2018年7月13日 下午7:23:23
 * @file PartTreeQueryRepositoryTest.java
 * @package io.github.hooj0.springdata.template.repository
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class PartTreeQueryRepositoryTest {

	@Configuration
	@EnableTemplateRepositories(basePackageClasses = MyRepo.class,
		considerNestedRepositories = true, 
		includeFilters = @Filter(pattern = ".*MyRepo", type = FilterType.REGEX))
	public static class Config extends AbstractTemplateConfiguration {
		
		@Override
		protected Set<Class<?>> getInitialEntitySet() {
			return Collections.singleton(Person.class);
		}
		
		@Bean
		MyTplTemplate myTplTemplate() throws ClassNotFoundException {
			return new MyTplTemplate("PartTreeQueryRepositoryTest", this.templateConverter());
		}
	}
	
	@Autowired
	private MyRepo repo;
	
	@Test
	public void testFindByFirstname() {
		System.out.println(repo.findByFirstname("jack"));
	}
	
	interface MyRepo extends TemplateRepository<Person, String> {

		@Query("select -> lastname = ?0")
		Person findByLastname(String lastname);

		Person findTop3By();

		Person findByFirstnameAndLastname(String firstname, String lastname);

		Person findPersonByFirstnameAndLastname(String firstname, String lastname);

		Person findByFirstnameOrLastname(String lastname);

		@CountQuery
		Person findPersonBy();

		Person findByMainAddress(AddressType address);

		Person findByMainAddressIn(Collection<AddressType> address);

		Person findByFirstnameIn(Collection<String> firstname);

		long countBy();

		boolean existsBy();

		@AllowFiltering
		Person findByFirstname(String firstname);

		PersonProjection findPersonProjectedByNickname(String nickname);

		<T> T findDynamicallyProjectedBy(Class<T> type);
	}
}
