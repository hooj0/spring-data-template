package io.github.hooj0.springdata.template.repository.query;

import java.util.Collection;

import io.github.hooj0.springdata.template.annotations.CountQuery;
import io.github.hooj0.springdata.template.annotations.Query;
import io.github.hooj0.springdata.template.repository.TemplateRepository;
import io.github.hooj0.springdata.template.repository.query.TemplatePartQueryTest.AddressType;
import io.github.hooj0.springdata.template.repository.query.TemplatePartQueryTest.AllowFiltering;
import io.github.hooj0.springdata.template.repository.query.TemplatePartQueryTest.Person;
import io.github.hooj0.springdata.template.repository.query.TemplatePartQueryTest.PersonProjection;


/**
 * <b>function:</b>
 * @author hoojo
 * @createDate 2018年7月13日 下午7:23:23
 * @file PartTreeQueryRepositoryTest.java
 * @package io.github.hooj0.springdata.template.repository
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class PartTreeQueryRepositoryTest {

	interface Repo extends TemplateRepository<Person, String> {

		@Query
		Person findByLastname(String lastname);

		Person findTop3By();

		Person findByFirstnameAndLastname(String firstname, String lastname);

		Person findPersonByFirstnameAndLastname(String firstname, String lastname);

		Person findByFirstnameOrLastName(String firstname);

		@CountQuery
		Person findPersonBy();

		Person findByMainAddress(AddressType address);

		Person findByMainAddressIn(Collection<AddressType> address);

		Person findByFirstnameIn(Collection<String> firstname);

		long countBy();

		boolean existsBy();

		@AllowFiltering
		Person findByFirstname(String firstname);

		PersonProjection findPersonProjectedByNickname(String nickname);

		<T> T findDynamicallyProjectedBy(Class<T> type);
	}
}
