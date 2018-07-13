package io.github.hooj0.springdata.template.core.query;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * <b>function:</b> 抽象查询基类
 * @author hoojo
 * @createDate 2018年7月13日 下午5:56:44
 * @file AbstractQuery.java
 * @package io.github.hooj0.springdata.template.core.query
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class AbstractQuery {

	protected Sort sort;
	protected Pageable pageable;
	
	public void setPageable(Pageable pageable) {
		this.pageable = pageable;
	}

	public Pageable getPageable() {
		return this.pageable;
	}
	
	@SuppressWarnings("unchecked")
	public final <T extends AbstractQuery> T addSort(Sort sort) {
		if (sort == null) {
			return (T) this;
		}

		if (this.sort == null) {
			this.sort = sort;
		} else {
			this.sort = this.sort.and(sort);
		}

		return (T) this;
	}
	
	public Sort getSort() {
		return this.sort;
	}
}
