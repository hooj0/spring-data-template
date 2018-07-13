package io.github.hooj0.springdata.template.core;

import java.util.Iterator;
import java.util.List;

import io.github.hooj0.springdata.template.core.query.Criteria;
import io.github.hooj0.springdata.template.core.query.Criteria.CriteriaEntry;
import io.github.hooj0.springdata.template.core.query.CriteriaQuery;

/**
 * <b>function:</b> 构建 查询 Statement
 * @author hoojo
 * @createDate 2018年7月13日 下午6:50:44
 * @file Statement.java
 * @package io.github.hooj0.springdata.template.core
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public final class Statement {

	private Statement() {}
	
	public static String buildChain(CriteriaQuery query) {
		if (query == null || query.getCriteria() == null) {
			System.err.println("CriteriaQuery is null");
			return "";
		}
		
		StringBuilder sb = new StringBuilder();
		
		List<Criteria> criterias = query.getCriteria().getCriteriaChain();
		for (Criteria criteria : criterias) {
			sb.append(criteria.getField().getName());
			sb.append(" ");
		}
		
		return sb.toString();
	}
	
	public static String buildQuery(CriteriaQuery query) {
		if (query == null || query.getCriteria() == null) {
			System.err.println("CriteriaQuery is null");
			return "";
		}
		
		StringBuilder sb = new StringBuilder();
		
		Iterator<CriteriaEntry> iter = query.getCriteria().getQueryCriteriaEntries().iterator();
		while (iter.hasNext()) {
			CriteriaEntry item = iter.next();
			sb.append(item.getKey().name());
			sb.append(item.getValue());
			sb.append(" ");
		}
		
		return sb.toString();
	}
	
	public static String buildFilter(CriteriaQuery query) {
		if (query == null || query.getCriteria() == null) {
			System.err.println("CriteriaQuery is null");
			return "";
		}
		
		StringBuilder sb = new StringBuilder();
		
		Iterator<CriteriaEntry> iter = query.getCriteria().getFilterCriteriaEntries().iterator();
		while (iter.hasNext()) {
			CriteriaEntry item = iter.next();
			sb.append(item.getKey().name());
			sb.append("=");
			sb.append(String.format("%s , %s", (Object[]) item.getValue()));
			sb.append(" ");
		}
		
		return sb.toString();
	}
}
