package io.github.hooj0.springdata.template.repository.cdi;

import java.util.Optional;

import io.github.hooj0.springdata.template.repository.TemplateRepository;
import io.github.hooj0.springdata.template.repository.domain.User;

/**
 * <b>function:</b> 定义一个 CDI待注入的 repo
 * @author hoojo
 * @createDate 2018年7月13日 下午2:37:37
 * @file CdiRepository.java
 * @package io.github.hooj0.springdata.template.repository.cdi
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface CdiUserRepository extends TemplateRepository<User, String> {

	public Optional<User> findUserById(String id);
}
