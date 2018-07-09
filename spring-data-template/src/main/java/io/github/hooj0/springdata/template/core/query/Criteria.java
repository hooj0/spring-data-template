package io.github.hooj0.springdata.template.core.query;

/**
 * <b>function:</b>
 * 
 * @author hoojo
 * @createDate 2018年7月9日 下午5:18:29
 * @file Criteria.java
 * @package io.github.hooj0.springdata.template.core.query
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class Criteria {

	public Criteria() {}
	
	public Criteria(String dotPath) {
	}

	public Criteria or(Object query) {

		return this;
	}
}
