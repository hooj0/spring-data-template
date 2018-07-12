package io.github.hooj0.springdata.template.repository.support;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.query.ExtensionAwareQueryMethodEvaluationContextProvider;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ClassUtils;

import io.github.hooj0.springdata.template.config.AbstractTemplateConfiguration;
import io.github.hooj0.springdata.template.core.TemplateOperations;
import io.github.hooj0.springdata.template.repository.TemplateRepository;
import io.github.hooj0.springdata.template.repository.domain.User;
import lombok.extern.slf4j.Slf4j;

/**
 * TemplateRepository Integration Test
 * 
 * @author hoojo
 * @createDate 2018年7月10日 下午6:25:01
 * @file SimpleTemplateRepositoryIntegrationTest.java
 * @package io.github.hooj0.springdata.template.repository.support
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Slf4j
@RunWith(SpringRunner.class)
@ContextConfiguration
public class TemplateRepositoryIntegrationTest implements BeanClassLoaderAware, BeanFactoryAware {

	@Configuration
	public static class Config extends AbstractTemplateConfiguration {
		@Override
		public String[] getEntityBasePackages() {
			return new String[] { User.class.getPackage().getName() };
		}

		@Bean
		CaptureEventListener eventListener() {
			return new CaptureEventListener();
		}
	}
	
	@Autowired 
	private TemplateOperations operations;
	@Autowired 
	private CaptureEventListener eventListener;
	
	private BeanFactory beanFactory;
	private TemplateRepositoryFactory factory;
	private ClassLoader classLoader;
	private UserRepostitory repository;

	private User dave, oliver, carter, boyd;
	
	@Before
	public void setUp() {

		factory = new TemplateRepositoryFactory(operations);
		factory.setRepositoryBaseClass(SimpleTemplateRepository.class);
		factory.setBeanClassLoader(classLoader);
		factory.setBeanFactory(beanFactory);
		factory.setEvaluationContextProvider(ExtensionAwareQueryMethodEvaluationContextProvider.DEFAULT);

		repository = factory.getRepository(UserRepostitory.class);

		repository.refresh();

		dave = new User("42", "Dave", "Matthews");
		oliver = new User("4", "Oliver August", "Matthews");
		carter = new User("49", "Carter", "Beauford");
		boyd = new User("45", "Boyd", "Tinsley");

		for (User user : Arrays.asList(oliver, dave, carter, boyd)) {
			repository.index(user);
		}
		
		System.out.println(eventListener);
	}
	
	@Test
	public void testInvoke() {
		repository.invoke("a", "b");
	}
	
	interface UserRepostitory extends TemplateRepository<User, String> { }
	
	static class CaptureEventListener implements ApplicationListener<ApplicationEvent> {

		@Override
		public void onApplicationEvent(ApplicationEvent event) {
			Object source = event.getSource();
			
			log.debug("event source: {}, timestamp: {}", source, event.getTimestamp());
		}
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;		
	}

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader();		
	}
}
