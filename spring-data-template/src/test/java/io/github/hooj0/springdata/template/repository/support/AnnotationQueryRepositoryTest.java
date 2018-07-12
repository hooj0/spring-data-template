package io.github.hooj0.springdata.template.repository.support;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.scheduling.annotation.Async;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.github.hooj0.springdata.template.annotations.CountQuery;
import io.github.hooj0.springdata.template.annotations.Field;
import io.github.hooj0.springdata.template.annotations.Model;
import io.github.hooj0.springdata.template.annotations.Query;
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
 * <b>function:</b>Annotation Query Repository Test
 * @author hoojo
 * @createDate 2018年7月11日 下午5:21:08
 * @file AnnotationQueryRepositoryTest.java
 * @package io.github.hooj0.springdata.template.repository.support
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class AnnotationQueryRepositoryTest {

	@Configuration
	@EnableTemplateRepositories(basePackageClasses = Person.class,
	// 是否启用自定义 namedQueriesLocation 自动派生接口映射
	//namedQueriesLocation = "classpath:META-INF/PersonRepositoryWithNamedQueries.properties",
	// 是否发现内部接口内部类
	considerNestedRepositories = true, 
	// 扫描指定正则的repo
	includeFilters = @Filter(pattern = ".*AnnotationQueryRepo", type = FilterType.REGEX))
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
			return new MyTplTemplate("AnnotationQueryRepositoryTest", this.templateConverter());
		}
	}
	
	@Autowired AnnotationQueryRepo repo;

	@Test
	public void testQueryWithReference() {
		BigInteger number = new BigInteger("5555555");
		Person saved = new Person(number);
		
		repo.refresh();
		repo.index(saved);
	}
	
	@Test
	public void testAnnotatedQuery() {
		BigInteger number = new BigInteger("4444444");
		repo.findThingByBigInteger(number);
	}

	@Test
	public void testQueryAnnotatedCount() {
		repo.count();
	}
	
	@Test
	public void testGetPerson() {
		BigInteger number = new BigInteger("0");
		Person person = new Person(number);
		person.setName("XxxxXXX");
		
		// PartTree 自动派生查询
		repo.getPersonByName(person);
	}
	
	@Test
	public void testQueryCount() {
		BigInteger number = new BigInteger("1111");
		
		// PartTree 自动派生查询
		repo.countQueryByName(number);
	}

	@Test
	public void testByNumber() {
		BigInteger number = new BigInteger("2222");
		
		// PartTree 自动派生查询
		repo.getByNumber(number);
	}

	@Test
	public void testFindByNamedQuery() {
		BigInteger number = new BigInteger("2222");
		
		// PartTree 自动派生查询
		repo.findByNamedQuery(number);
	}
	
	@Test
	public void testAsync() {
		// PartTree 自动派生查询
		try {
			repo.findOneByName("223333").get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	@Data
	@AllArgsConstructor
	@RequiredArgsConstructor
	@Model(indexName = "Persion")
	@Setting(settingPath = "/aaa/bbb/xxx/person")
	static class Person {
		@Id private final BigInteger number;
		@Score float socre;
		@Field String name;
		@Version Long version;
		//int count;
	}
	
	interface AnnotationQueryRepo extends TemplateRepository<Person, Long> {

		@Query("SELECT * from person where number = ?0")
		Person findThingByBigInteger(BigInteger number);
		
		@CountQuery("SELECT count(1) from person")
		int count();
		
		Person getPersonByName(Person entity);
		
		// PartTree 自动派生查询
		Long countQueryByName(BigInteger name);

		// PartTree 自动派生查询
		Long getByNumber(BigInteger number);
		
		@Async
		CompletableFuture<Person> findOneByName(String name);
		
		Object findByNamedQuery(BigInteger number);
	}
}
