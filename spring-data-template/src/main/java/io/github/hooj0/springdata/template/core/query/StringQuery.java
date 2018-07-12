package io.github.hooj0.springdata.template.core.query;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 字符串查询对象，通过构建字符串查询对象，完成数据查询
 * @author hoojo
 * @createDate 2018年7月12日 上午9:02:42
 * @file StringQuery.java
 * @package io.github.hooj0.springdata.template.core.query
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class StringQuery {

	private String source;

	public StringQuery(String source) {
		this.source = source;
	}

	public StringQuery(String source, Pageable pageable) {
		this.source = source;
	}

	public StringQuery(String source, Pageable pageable, Sort sort) {
		this.source = source;
	}

	public String getSource() {
		return source;
	}

	public void setPageable(Pageable pageable) {
		
	}
}
