package io.github.hooj0.springdata.template.repository.support;

import java.io.Serializable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.github.hooj0.springdata.template.core.MyTplTemplate;
import io.github.hooj0.springdata.template.core.convert.MappingTemplateConverter;
import io.github.hooj0.springdata.template.core.mapping.SimpleTemplateMappingContext;
import io.github.hooj0.springdata.template.core.mapping.TemplatePersistentEntity;
import lombok.Data;

/**
 * <b>function:</b> SimpleTemplateRepository TestUnit
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
@ContextConfiguration("classpath:/simple-repository-test.xml")
public class SimpleTemplateRepositoryTest {

	private SimpleTemplateMappingContext context = new SimpleTemplateMappingContext();
	private MappingTemplateConverter converter = new MappingTemplateConverter(context);
	private SimpleTemplateRepository<Object, ? extends Serializable> repository;
	
	@Autowired
	private MyTplTemplate template;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Before
	public void before() {
		TemplatePersistentEntity<?> entity = converter.getMappingContext().getRequiredPersistentEntity(SimplePerson.class);
		
		entity.forEach(property -> System.out.println("property: " + property));
		System.out.println("TypeInformation: " + entity.getTypeInformation());
		System.out.println("TypeInformation: " + entity.getTypeInformation().getProperty("age"));
		
		repository = new SimpleTemplateRepository<Object, String>(new MappingTemplateEntityInformation(entity, converter), template);
	}
	
	@Test
	public void testInject() {
		System.out.println(repository);
		System.out.println(template);
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
	
	@Data
	static class SimplePerson {
		@Id float id;
		String name;
		int age;
	}
}
