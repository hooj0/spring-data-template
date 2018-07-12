package io.github.hooj0.springdata.template.core.query;

/**
 * 组建CRUD规范，通过实体模型构建增删改规范，从而生成增删改操作
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
