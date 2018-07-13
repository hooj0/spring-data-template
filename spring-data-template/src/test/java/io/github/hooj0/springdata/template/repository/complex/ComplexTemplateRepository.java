package io.github.hooj0.springdata.template.repository.complex;

import io.github.hooj0.springdata.template.repository.TemplateRepository;
import io.github.hooj0.springdata.template.repository.domain.User;

/**
 * <b>function:</b> 组合 自定义接口和TemplateRepository ，拼装成一个全新的 repo
 * @author hoojo
 * @createDate 2018年7月13日 上午11:10:23
 * @file ComplexTemplateRepository.java
 * @package io.github.hooj0.springdata.template.repository.complex
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface ComplexTemplateRepository extends TemplateRepository<User, Long>, CustomMethodRepository {

}
