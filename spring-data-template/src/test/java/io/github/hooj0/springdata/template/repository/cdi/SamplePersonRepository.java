package io.github.hooj0.springdata.template.repository.cdi;

import org.springframework.data.repository.Repository;

import io.github.hooj0.springdata.template.repository.domain.Personl;

/**
 * 继承自定义方法接口
 * @author hoojo
 * @createDate 2018年7月13日 下午5:26:25
 * @file SamplePersonRepository.java
 * @package io.github.hooj0.springdata.template.repository.cdi
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface SamplePersonRepository extends Repository<Personl, Long>, SamplePersonRepositoryCustom {

}
