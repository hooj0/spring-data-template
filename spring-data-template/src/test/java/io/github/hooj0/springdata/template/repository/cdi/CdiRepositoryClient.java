package io.github.hooj0.springdata.template.repository.cdi;

import javax.inject.Inject;


/**
 * <b>function:</b> CDI 依赖注入客户端，提供各种数据注入
 * @author hoojo
 * @createDate 2018年7月13日 下午2:35:53
 * @file CdiRepositoryClient.java
 * @package io.github.hooj0.springdata.template.repository.cdi
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
class CdiRepositoryClient {

	private CdiUserRepository repository;
	private SamplePersonRepository samplePersonRepository;
	private QualifiedUserRepository qualifiedUserRepository;
	
	public CdiUserRepository getRepository() {
		return this.repository;
	}
	
	@Inject
	public void setRepository(CdiUserRepository repository) {
		this.repository = repository;
	}

	public SamplePersonRepository getSamplePersonRepository() {
		return samplePersonRepository;
	}

	@Inject
	public void setSamplePersonRepository(SamplePersonRepository samplePersonRepository) {
		this.samplePersonRepository = samplePersonRepository;
	}

	public QualifiedUserRepository getQualifiedUserRepository() {
		return qualifiedUserRepository;
	}

	@Inject
	// 注入QualifiedUserRepository的对象，在QualifiedUserRepository对象中定义Qualifier 注解
	public void setQualifiedUserRepository(@PersonDB @OtherQualifier QualifiedUserRepository qualifiedUserRepository) {
		this.qualifiedUserRepository = qualifiedUserRepository;
	}
}
