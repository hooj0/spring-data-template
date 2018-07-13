package io.github.hooj0.springdata.template.repository.cdi;

import org.apache.webbeans.cditest.CdiTestContainer;
import org.apache.webbeans.cditest.CdiTestContainerLoader;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * CDI 依赖注入 repo 集成测试<br/>
 * CDI 环境依赖文件：<br/>
 * 		beans.xml<br/>
 * 		javax.enterprise.inject.spi.Extension
 * @author hoojo
 * @createDate 2018年7月13日 下午2:24:19
 * @file CdiRepositoryIntegrationTest.java
 * @package io.github.hooj0.springdata.template.repository.cdi
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class CdiRepositoryIntegrationTest {

	private static CdiTestContainer cdiContainer;
	private CdiUserRepository repository;
	private SamplePersonRepository personRepo;
	private QualifiedUserRepository userRepo;
	
	@BeforeClass
	public static void init() throws Exception {

		cdiContainer = CdiTestContainerLoader.getCdiContainer();
		cdiContainer.startApplicationScope();
		cdiContainer.bootContainer();
	}

	@AfterClass
	public static void shutdown() throws Exception {

		cdiContainer.stopContexts();
		cdiContainer.shutdownContainer();
	}
	
	@Before
	public void setUp() {
		
		CdiRepositoryClient client = cdiContainer.getInstance(CdiRepositoryClient.class);
		repository = client.getRepository();
		repository.refresh();

		personRepo = client.getSamplePersonRepository();
		userRepo  = client.getQualifiedUserRepository();
	}
	
	@Test
	public void testInject() {
		System.out.println(repository);
		System.out.println(personRepo);
		System.out.println(userRepo);
	}
	
	@Test
	public void testRun() {
		repository.findUserById("XxxxId");
	}
	
	@Test
	public void testRunCustom() {
		personRepo.returnOne();
	}
}
