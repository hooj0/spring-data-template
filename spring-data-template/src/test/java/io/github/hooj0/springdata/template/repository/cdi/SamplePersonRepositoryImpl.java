package io.github.hooj0.springdata.template.repository.cdi;

/**
 * <b>function:</b> 实现自定义方法接口
 * @author hoojo
 * @createDate 2018年7月13日 下午5:26:58
 * @file SamplePersonRepositoryImpl.java
 * @package io.github.hooj0.springdata.template.repository.cdi
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
class SamplePersonRepositoryImpl implements SamplePersonRepositoryCustom {

	@Override
	public int returnOne() {
		return 1;
	}
}
