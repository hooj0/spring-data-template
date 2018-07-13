package io.github.hooj0.springdata.template.repository.support;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.github.hooj0.springdata.template.annotations.Field;
import io.github.hooj0.springdata.template.annotations.Model;
import io.github.hooj0.springdata.template.annotations.Score;
import io.github.hooj0.springdata.template.annotations.Setting;
import io.github.hooj0.springdata.template.config.AbstractTemplateConfiguration;
import io.github.hooj0.springdata.template.core.MyTplTemplate;
import io.github.hooj0.springdata.template.repository.TemplateRepository;
import io.github.hooj0.springdata.template.repository.config.EnableTemplateRepositories;
import io.github.hooj0.springdata.template.repository.support.TemplateRepositoryIntegrationTest.CaptureEventListener;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * <b>function:</b> SpelQueryTemplateRepositoryTest
 * @author hoojo
 * @createDate 2018年7月12日 下午3:48:56
 * @file SpelQueryTemplateRepositoryTest.java
 * @package io.github.hooj0.springdata.template.repository.support
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class SpelQueryTemplateRepositoryTest {

	@Configuration
	@EnableTemplateRepositories(basePackageClasses = SpelQueryRepo.class,
		considerNestedRepositories = true, 
		includeFilters = @Filter(pattern = ".*SpelQueryRepo", type = FilterType.REGEX))
	public static class Config extends AbstractTemplateConfiguration {
		
		@Override
		protected Set<Class<?>> getInitialEntitySet() {
			return Collections.singleton(Person.class);
		}

		@Bean
		CaptureEventListener eventListener() {
			return new CaptureEventListener();
		}
		
		@Bean
		MyTplTemplate myTplTemplate() throws ClassNotFoundException {
			return new MyTplTemplate("SpelQueryTemplateRepositoryTest", this.templateConverter());
		}
	}
	
	@Autowired
	private SpelQueryRepo repo;
	
	@Test
	public void testSpelQuery() {
		Person person = new Person(new BigInteger("2"));
		person.setName("Xxxx");
		person.setVersion(33L);
		
		repo.countQueryByName(person);
	}
	
	@Data
	@AllArgsConstructor
	@RequiredArgsConstructor
	@Model(indexName = "Persion")
	@Setting(settingPath = "/aaa/bbb/xxx/person")
	static class Person {
		@Id private final BigInteger number;
		@Score float socre;
		
		//@Value("#{@someBean.someMethod(target, args[0])}")
		//@Value("#{@someBean.value}")
		//@Value("#{target.name}")
		@Field 
		String name;
		
		@Version Long version;
	}
	
	interface SpelQueryRepo extends TemplateRepository<Person, Long> {
		Long countQueryByName(Person person);
	}
	
	static class Target {
		public String getName() {
			return "target name";
		}
	}

	static class SomeBean {
		public String getValue() {
			return "value-xxxx";
		}

		public String someMethod(Target target, Integer parameter) {
			return target.getName() + parameter.toString();
		}
	}
}
