package io.github.hooj0.springdata.template.core.query;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import io.github.hooj0.springdata.template.core.query.Criteria.CriteriaEntry;
import io.github.hooj0.springdata.template.core.query.Criteria.OperationKey;

/**
 * <b>function:</b> 构建 查询 Statement 输出
 * @author hoojo
 * @createDate 2018年7月13日 下午6:50:44
 * @file Statement.java
 * @package io.github.hooj0.springdata.template.core
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public final class StatementPrint {

	private StatementPrint() {}
	
	public static String buildChain(CriteriaQuery query) {
		if (query == null || query.getCriteria() == null) {
			System.err.println("CriteriaQuery is null");
			return "";
		}
		
		return buildChain(query.getCriteria());
	}
	
	public static String buildChain(Criteria criter) {
		if (criter == null) {
			System.err.println("Criteria is null");
			return "";
		}
		
		StringBuilder sb = new StringBuilder();
		
		List<Criteria> criterias = criter.getCriteriaChain();
		for (Criteria criteria : criterias) {
			
			if (criteria.isAnd()) {
				sb.append(" AND ");
			} else if (criteria.isOr()) {
				sb.append(" OR ");
			} else if (criteria.isNegating()) {
				sb.append(" NET ");
			}
			
			sb.append(" (");
			sb.append(" ");
			
			sb.append(buildQuery(criteria));
			sb.append(buildFilter(criteria));
			//sb.append(criteria.getField().getName());
			
			sb.append(") ");
		}
		
		return sb.toString();
	}
	
	public static String buildQuery(CriteriaQuery query) {
		if (query == null || query.getCriteria() == null) {
			System.err.println("CriteriaQuery is null");
			return "";
		}
		
		return buildQuery(query.getCriteria());
	}
	
	public static String buildQuery(Criteria criter) {
		if (criter == null) {
			System.err.println("Criteria is null");
			return "";
		}
		
		StringBuilder sb = new StringBuilder();
		
		Iterator<CriteriaEntry> iter = criter.getQueryCriteriaEntries().iterator();
		while (iter.hasNext()) {
			CriteriaEntry item = iter.next();
			String value = outObjects(item.getValue());

			sb.append(criter.getField().getName());
			sb.append(" ");
			sb.append(item.getKey().toString());
			sb.append(" ");
			
			if (item.getKey() == OperationKey.BETWEEN) {
				sb.append(value);
			} else if(item.getKey() == OperationKey.IN || item.getKey() == OperationKey.NOT_IN) {
				sb.append("(").append(value).append(")");
			} else {
				sb.append(value);
			}
			sb.append("; ");
		}
		
		return sb.toString();
	}
	
	public static String buildFilter(CriteriaQuery query) {
		if (query == null || query.getCriteria() == null) {
			System.err.println("CriteriaQuery is null");
			return "";
		}
		
		return buildFilter(query.getCriteria());
	}
	
	public static String buildFilter(Criteria criter) {
		if (criter == null) {
			System.err.println("Criteria is null");
			return "";
		}
		
		StringBuilder sb = new StringBuilder();
		
		Iterator<CriteriaEntry> iter = criter.getFilterCriteriaEntries().iterator();
		while (iter.hasNext()) {
			CriteriaEntry item = iter.next();
			String value = outObjects(item.getValue());

			sb.append(criter.getField().getName());
			sb.append(" ");
			sb.append(item.getKey().toString());
			sb.append(" ");
			
			if (item.getKey() == OperationKey.BETWEEN) {
				sb.append(value);
			} else if(item.getKey() == OperationKey.IN || item.getKey() == OperationKey.NOT_IN) {
				sb.append("(").append(value).append(")");
			} else {
				sb.append(value);
			}
			sb.append("; ");
		}
		
		return sb.toString();
	}
	
	public static String outObjects(Object obj) {
		
		if (obj instanceof Object[]) {

			Object[] items = (Object[]) obj;	
			StringBuilder sb = new StringBuilder();
			for (Object item : items) {
				sb.append(" ").append(item).append(", ");
			}
			return sb.toString();
		} else if (obj instanceof Collection) {
			Object[] items = ((Collection<?>) obj).toArray();
			StringBuilder sb = new StringBuilder();
			for (Object item : items) {
				sb.append(" ").append(item).append(", ");
			}
			return sb.toString();
		} else {
			return obj.toString();
		}
		
	}
}
