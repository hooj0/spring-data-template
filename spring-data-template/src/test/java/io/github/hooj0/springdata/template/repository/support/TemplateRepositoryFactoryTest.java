package io.github.hooj0.springdata.template.repository.support;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.repository.Repository;

import io.github.hooj0.springdata.template.core.MyTplTemplate;
import io.github.hooj0.springdata.template.core.convert.MappingTemplateConverter;
import io.github.hooj0.springdata.template.core.convert.TemplateConverter;
import io.github.hooj0.springdata.template.core.mapping.SimpleTemplateMappingContext;
import io.github.hooj0.springdata.template.core.mapping.TemplatePersistentEntity;
import io.github.hooj0.springdata.template.repository.domain.User;
import lombok.extern.slf4j.Slf4j;

/**
 * <b>function:</b>TemplateRepositoryFactory Test
 * @author hoojo
 * @createDate 2018年7月11日 上午11:12:07
 * @file TemplateRepositoryFactoryTest.java
 * @package io.github.hooj0.springdata.template.repository.support
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Slf4j
@SuppressWarnings("rawtypes")
public class TemplateRepositoryFactoryTest {

	private TemplateConverter converter;
	private SimpleTemplateMappingContext context;
	private TemplatePersistentEntity entity;
	
	private MyTplTemplate template;
	
	interface MyPersonRepository extends Repository<User, Long> {
		Long getByLastname(String lastname);
	}
	
	@Before
	public void setUp() {
		context = new SimpleTemplateMappingContext();
		converter = new MappingTemplateConverter(context);
		entity = converter.getMappingContext().getPersistentEntity(User.class);
		
		template = new MyTplTemplate("user client", converter);
	}
	
	@Test
	public void testFactory() {
		System.out.println(entity.getType());
		
		TemplateRepositoryFactory factory = new TemplateRepositoryFactory(template);
		TemplateEntityInformation<User, Object> entityInformation = factory.getEntityInformation(User.class);
		
		log.debug("entityInformation: {}", entityInformation);
		log.debug("IdAttribute: {}", entityInformation.getIdAttribute());
		log.debug("IdType: {}", entityInformation.getIdType());
		log.debug("getIndexName: {}", entityInformation.getIndexName());
		log.debug("getJavaType: {}", entityInformation.getJavaType());
		log.debug("getTableName: {}", entityInformation.getTableName());
		log.debug("getType: {}", entityInformation.getType());
		
		MyPersonRepository repository = factory.getRepository(MyPersonRepository.class);
		System.out.println(repository); // NumberKeyedRepository 
		System.out.println(repository.getByLastname("Xxxxxx"));
	}
}
