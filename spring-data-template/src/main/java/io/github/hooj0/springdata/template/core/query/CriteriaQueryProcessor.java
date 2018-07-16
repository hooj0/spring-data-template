package io.github.hooj0.springdata.template.core.query;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.springframework.util.Assert;

import io.github.hooj0.springdata.template.core.query.Criteria.OperationKey;

/**
 * <b>function:</b>Criteria Query Processor 构建条件查询
 * @author hoojo
 * @createDate 2018年7月16日 上午9:37:34
 * @file CriteriaQueryProcessor.java
 * @package io.github.hooj0.springdata.template.core.query
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class CriteriaQueryProcessor {

	public String createQueryFromCriteria(Criteria criteria) {
		if (criteria == null) {
			System.err.println("criteria Null");
			return null;
		}

		List<String> shouldQueryBuilderList = new LinkedList<>();
		List<String> mustNotQueryBuilderList = new LinkedList<>();
		List<String> mustQueryBuilderList = new LinkedList<>();

		ListIterator<Criteria> chainIterator = criteria.getCriteriaChain().listIterator();

		String firstQuery = null;
		boolean negateFirstQuery = false;

		while (chainIterator.hasNext()) {
			Criteria chainedCriteria = chainIterator.next();
			String queryFragmentForCriteria = createQueryFragmentForCriteria(chainedCriteria);
			if (queryFragmentForCriteria != null) {
				if (firstQuery == null) {
					firstQuery = queryFragmentForCriteria;
					negateFirstQuery = chainedCriteria.isNegating();
					continue;
				}
				if (chainedCriteria.isOr()) {
					shouldQueryBuilderList.add(queryFragmentForCriteria);
				} else if (chainedCriteria.isNegating()) {
					mustNotQueryBuilderList.add(queryFragmentForCriteria);
				} else {
					mustQueryBuilderList.add(queryFragmentForCriteria);
				}
			}
		}

		if (firstQuery != null) {
			if (!shouldQueryBuilderList.isEmpty() && mustNotQueryBuilderList.isEmpty() && mustQueryBuilderList.isEmpty()) {
				shouldQueryBuilderList.add(0, firstQuery);
			} else {
				if (negateFirstQuery) {
					mustNotQueryBuilderList.add(0, firstQuery);
				} else {
					mustQueryBuilderList.add(0, firstQuery);
				}
			}
		}

		String query = "";
		if (!shouldQueryBuilderList.isEmpty() || !mustNotQueryBuilderList.isEmpty() || !mustQueryBuilderList.isEmpty()) {

			for (String qb : shouldQueryBuilderList) {
				query += " OR (" + qb + ")";
			}
			for (String qb : mustNotQueryBuilderList) {
				query += " must_Not (" + qb + ")";
			}
			for (String qb : mustQueryBuilderList) {
				query += " AND (" + qb + ")";
			}
		}

		return query;
	}


	private String createQueryFragmentForCriteria(Criteria chainedCriteria) {
		if (chainedCriteria.getQueryCriteriaEntries().isEmpty()) {
			System.err.println("query Null");			
			return null;
		}

		Iterator<Criteria.CriteriaEntry> it = chainedCriteria.getQueryCriteriaEntries().iterator();
		boolean singeEntryCriteria = (chainedCriteria.getQueryCriteriaEntries().size() == 1);

		String fieldName = chainedCriteria.getField().getName();
		Assert.notNull(fieldName, "Unknown field");

		String query = "";
		if (singeEntryCriteria) {
			Criteria.CriteriaEntry entry = it.next();
			query = processCriteriaEntry(entry, fieldName);
		} else {
			while (it.hasNext()) {
				Criteria.CriteriaEntry entry = it.next();
				query += processCriteriaEntry(entry, fieldName);
				if (it.hasNext()) {
					query += " & ";
				}
			}
		}

		//query += " boost=" + chainedCriteria.getBoost();
		return query;
	}


	@SuppressWarnings({ "unchecked" })
	private String processCriteriaEntry(Criteria.CriteriaEntry entry,/* OperationKey key, Object value,*/ String fieldName) {
		Object value = entry.getValue();
		if (value == null) {
			System.err.println("value Null");
			return null;
		}
		String query = null;

		OperationKey key = entry.getKey();
		String searchText = value.toString();

		Iterable<Object> collection = null;
		switch (key) {
			case EQUALS:
				query = (fieldName) +  "=" + (searchText);
				break;
			case CONTAINS:
				query = (fieldName) + " like %" + value + "%";
				break;
			case STARTS_WITH:
				query = (fieldName) + " like " + value + "%";
				break;
			case ENDS_WITH:
				query = (fieldName) + " like %" + value;
				break;
			case EXPRESSION:
				query = (fieldName) + " {#" + (searchText) + "} ";
				break;
			case LESS_EQUAL:
				query = (fieldName) + " <= " + (value);
				break;
			case GREATER_EQUAL:
				query = (fieldName) + " >= " + (value);
				break;
			case BETWEEN:
				Object[] ranges = (Object[]) value;
				query = (fieldName) + " between " + (ranges[0]) + " and " + (ranges[1]);
				break;
			case LESS:
				query = (fieldName) + " < " + (value);
				break;
			case GREATER:
				query = (fieldName) + " > " + (value);
				break;
			case FUZZY:
				query = (fieldName + " ~ " + searchText);
				break;
			case IN:
				query = fieldName + " in (";
				collection = (Iterable<Object>) value;
				for (Object item : collection) {
					query += (item.toString()) + ", ";
				}
				query += ")";
				break;
			case NOT_IN:
				query = fieldName + " not in (";
				collection = (Iterable<Object>) value;
				for (Object item : collection) {
					query += (item.toString()) + ", ";
				}
				query += ")";
				break;
			case WITHIN: 
				query = fieldName + " with in (";
				collection = (Iterable<Object>) value;
				for (Object item : collection) {
					query += (item.toString()) + ", ";
				}
				query += ")";
				break;
			case BBOX:
				query = fieldName + " box in (";
				collection = (Iterable<Object>) value;
				for (Object item : collection) {
					query += (item.toString()) + ", ";
				}
				query += ")";
				break;
			default:
				System.err.println("not found!");
				break;
					
		}
		return query;
	}
}
