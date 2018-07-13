package io.github.hooj0.springdata.template.core.query;

import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;

/**
 * 对象查询类，方便通过实体对象模型构建查询，通过对象实体构建查询操作
 * @author hoojo
 * @createDate 2018年7月9日 下午5:05:40
 * @file CriteriaQuery.java
 * @package io.github.hooj0.springdata.template.core.query
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class CriteriaQuery extends AbstractQuery {

	private Criteria criteria;

	private CriteriaQuery() {}

	public CriteriaQuery(Criteria criteria) {
		this(criteria, Pageable.unpaged());
	}

	public CriteriaQuery(Criteria criteria, Pageable pageable) {

		Assert.notNull(criteria, "Criteria must not be null!");
		Assert.notNull(pageable, "Pageable must not be null!");

		this.criteria = criteria;
		this.pageable = pageable;
		this.addSort(pageable.getSort());
	}

	public static final CriteriaQuery fromQuery(CriteriaQuery source) {
		return fromQuery(source, new CriteriaQuery());
	}

	public static <T extends CriteriaQuery> T fromQuery(CriteriaQuery source, T destination) {
		if (source == null || destination == null) {
			return null;
		}

		if (source.getCriteria() != null) {
			destination.addCriteria(source.getCriteria());
		}

		if (source.getSort() != null) {
			destination.addSort(source.getSort());
		}

		return destination;
	}

	@SuppressWarnings("unchecked")
	public final <T extends CriteriaQuery> T addCriteria(Criteria criteria) {
		Assert.notNull(criteria, "Cannot add null criteria.");
		if (this.criteria == null) {
			this.criteria = criteria;
		} else {
			this.criteria.and(criteria);
		}
		return (T) this;
	}

	public Criteria getCriteria() {
		return this.criteria;
	}
}
