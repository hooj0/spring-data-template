package io.github.hooj0.springdata.template.repository.query;

import org.junit.Test;

import io.github.hooj0.springdata.template.core.query.Criteria;
import io.github.hooj0.springdata.template.core.query.CriteriaQuery;
import io.github.hooj0.springdata.template.core.query.CriteriaQueryProcessor;
import io.github.hooj0.springdata.template.core.query.StatementPrint;

/**
 * <b>function:</b> CriteriaTest
 * @author hoojo
 * @createDate 2018年7月14日 上午11:07:22
 * @file CriteriaTest.java
 * @package io.github.hooj0.springdata.template.repository.query
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class CriteriaTest {

	@Test
	public void test1() {
		Criteria c = new Criteria("AGE").in("1", "2").between("a", "b").greaterThan("99");
		c.and(new Criteria("NAME").contains("8").greaterThan("20"));
		c.and(new Criteria("ADDRESS").contains("8"));
		build(c);
		
		Criteria c2 = c.and("nickName").lessThanEqual("666");
		build(c2);
	}
	
	@Test
	public void test2() {
		
		Criteria c = new Criteria();
		
		CriteriaQuery query = new CriteriaQuery(c);
		query.addCriteria(new Criteria("AGE").in("1", "2").between("a", "b").greaterThan("99"));
		query.addCriteria(c.or("NAME").notIn("8", "a").greaterThan("20"));
		query.addCriteria(c.or("SEX").contains("true"));
		query.addCriteria(new Criteria("Y").expression("true"));
		query.addCriteria(c.or("NickName").expression("true").in("x", "y"));
		query.addCriteria(c.or(new Criteria("X").expression("true").endsWith("SS")).and("XXX").contains("2").endsWith("1"));
		
		build(query.getCriteria());
		//System.out.println(Statement.buildChain(query));
	}
	
	private void build(Criteria query) {
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println(new CriteriaQueryProcessor().createQueryFromCriteria(query));

		System.out.println("chain: " + StatementPrint.buildChain(query));
		System.out.println("query: " + StatementPrint.buildQuery(query));
		System.out.println("filter: " + StatementPrint.buildFilter(query));
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}
}
