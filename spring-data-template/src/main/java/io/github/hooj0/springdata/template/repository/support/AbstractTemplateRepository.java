package io.github.hooj0.springdata.template.repository.support;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.hooj0.springdata.template.repository.TemplateRepository;

/**
 * <b>function:</b> 抽象的 repo 实现类型，方便其他的实现 repo 继承
 * @author hoojo
 * @createDate 2018年7月4日 下午3:28:51
 * @file AbstractTemplateRepository.java
 * @package io.github.hooj0.springdata.template.repository.support
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class AbstractTemplateRepository<T, ID extends Serializable> implements TemplateRepository<T, ID> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTemplateRepository.class);
	
	public AbstractTemplateRepository() {
	}

	@Override
	public boolean invoke(String... args) {
		return false;
	}

	@Override
	public String query(String param) {
		return null;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <S extends T> S index(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<T> getEntityClass() {
		// TODO Auto-generated method stub
		return null;
	}
}
