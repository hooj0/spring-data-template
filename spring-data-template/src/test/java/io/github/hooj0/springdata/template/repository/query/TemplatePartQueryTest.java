package io.github.hooj0.springdata.template.repository.query;


import java.io.Serializable;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.annotation.Id;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.data.repository.core.support.DefaultRepositoryMetadata;
import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.util.ClassUtils;

import io.github.hooj0.springdata.template.annotations.CountQuery;
import io.github.hooj0.springdata.template.annotations.Field;
import io.github.hooj0.springdata.template.annotations.Model;
import io.github.hooj0.springdata.template.annotations.Query;
import io.github.hooj0.springdata.template.core.MyTplTemplate;
import io.github.hooj0.springdata.template.core.TemplateOperations;
import io.github.hooj0.springdata.template.core.mapping.SimpleTemplateMappingContext;
import io.github.hooj0.springdata.template.core.query.CriteriaQuery;
import io.github.hooj0.springdata.template.core.query.CriteriaQueryProcessor;
import io.github.hooj0.springdata.template.enums.FieldType;
import io.github.hooj0.springdata.template.repository.TemplateRepository;
import io.github.hooj0.springdata.template.repository.query.simple.TemplatePartQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <b>function:</b> PartTree QueryTest
 * @author hoojo
 * @createDate 2018年7月13日 下午9:24:38
 * @file TemplatePartQueryTest.java
 * @package io.github.hooj0.springdata.template.repository.query
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class TemplatePartQueryTest {

	private TemplateOperations operations;
	@SuppressWarnings("rawtypes")
	private MappingContext mappingContext;

	@Before
	public void setUp() {
		this.operations = new MyTplTemplate("client");
		this.mappingContext = new SimpleTemplateMappingContext();
	}

	@Test // DATACASS-7
	public void shouldDeriveSimpleQuery() {
		build(deriveQueryFromMethod("findByLastname", "foo"));
		
		//assertThat(query).isEqualTo("SELECT * FROM person WHERE lastname='foo';");
	}

	@Test // DATACASS-511
	public void shouldDeriveLimitingQuery() {
		build(deriveQueryFromMethod("findTop3By"));
		
		//assertThat(query).isEqualTo("SELECT * FROM person LIMIT 3;");
	}

	@Test // DATACASS-7
	public void shouldDeriveSimpleQueryWithoutNames() {
		build(deriveQueryFromMethod("findPersonBy"));
		//assertThat(query).isEqualTo("SELECT * FROM person;");
	}

	@Test // DATACASS-7
	public void shouldDeriveAndQuery() {
		build(deriveQueryFromMethod("findByFirstnameAndLastnameAndNicknameOrLastname", "foo", "bar", "see", "xxx"));
		build(deriveQueryFromMethod("findByFirstnameOrLastnameOrNickname", "foo", "bar", "daa"));
		
		//assertThat(query).isEqualTo("SELECT * FROM person WHERE firstname='foo' AND lastname='bar';");
	}

	@Test // DATACASS-7
	public void usesDynamicProjection() {
		build(deriveQueryFromMethod("findDynamicallyProjectedBy", PersonProjection.class));
		
		//assertThat(query).isEqualTo("SELECT * FROM person;");
	}

	@Test // DATACASS-479
	public void usesProjectionQueryHiddenField() {
		build(deriveQueryFromMethod("findPersonProjectedByNickname", "foo"));
		//assertThat(query).isEqualTo("SELECT * FROM person WHERE nickname='foo';");
	}

	@Test // DATACASS-357
	public void shouldDeriveFieldInCollectionQuery() {

		build(deriveQueryFromMethod(Repo.class, "findByFirstnameIn", new Class[] { Collection.class }, Arrays.asList("Hank", "Walter")));
		
		//assertThat(query).isEqualTo("SELECT * FROM person WHERE firstname IN ('Hank','Walter');");
	}

	@Test // DATACASS-172
	public void shouldDeriveSimpleQueryWithMappedUDT() {

		build(deriveQueryFromMethod("findByMainAddress", new AddressType()));

		
		//assertThat(query).isEqualTo("SELECT * FROM person WHERE mainaddress={};");
	}

	@Test // DATACASS-343
	public void shouldRenderMappedColumnNamesForCompositePrimaryKey() {

		CriteriaQuery query = deriveQueryFromMethod(GroupRepository.class, "findByIdHashPrefix", new Class[] { String.class },
				"foo");

		build(query);
		//assertThat(query.toString()).isEqualTo("SELECT * FROM group WHERE hash_prefix='foo';");
	}

	@Test // DATACASS-376
	public void shouldAllowFiltering() {

		CriteriaQuery query = deriveQueryFromMethod(Repo.class, "findByFirstname", new Class[] { String.class }, "foo");

		build(query);
		//assertThat(query.toString()).isEqualTo("SELECT * FROM person WHERE firstname='foo' ALLOW FILTERING;");
	}

	private void build(CriteriaQuery query) {
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println(new CriteriaQueryProcessor().createQueryFromCriteria(query.getCriteria()));
		//System.out.println("chain: " + Statement.buildChain(query));
		//System.out.println("query: " + Statement.buildQuery(query));
		//System.out.println("filter: " + Statement.buildFilter(query));
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}
	
	@Test // DATACASS-146
	public void shouldApplyQueryOptions() {

		CriteriaQuery statement = deriveQueryFromMethod(Repo.class, "findByFirstname",
				new Class[] { String.class }, "Walter");

		build(statement);
		//assertThat(statement.toString()).isEqualTo("SELECT * FROM person WHERE firstname='Walter';");
		//assertThat(statement.getFetchSize()).isEqualTo(777);
	}

	@Test // DATACASS-146
	public void shouldApplyConsistencyLevel() {

		CriteriaQuery statement = deriveQueryFromMethod(Repo.class, "findPersonBy", new Class[0]);
		build(statement);
		//assertThat(statement.toString()).isEqualTo("SELECT * FROM person;");
	}

	@Test // DATACASS-512
	public void shouldCreateCountQuery() {

		CriteriaQuery statement = deriveQueryFromMethod(Repo.class, "countBy", new Class[0]);

		build(statement);
		//assertThat(statement.toString()).isEqualTo("SELECT COUNT(1) FROM person;");
	}

	@Test // DATACASS-512
	public void shouldCreateExistsQuery() {

		CriteriaQuery statement = deriveQueryFromMethod(Repo.class, "existsBy", new Class[0]);

		build(statement);
		//assertThat(statement.toString()).isEqualTo("SELECT * FROM person LIMIT 1;");
	}

	private CriteriaQuery deriveQueryFromMethod(String method, Object... args) {
		Class<?>[] types = new Class<?>[args.length];

		for (int i = 0; i < args.length; i++) {
			types[i] = ClassUtils.getUserClass(args[i].getClass());
		}

		return deriveQueryFromMethod(Repo.class, method, types, args);
	}

	private CriteriaQuery deriveQueryFromMethod(Class<?> repositoryInterface, String method, Class<?>[] types, Object... args) {
		TemplatePartQuery partTreeQuery = createQueryForMethod(repositoryInterface, method, types);
		ParametersParameterAccessor accessor = new ParametersParameterAccessor(partTreeQuery.getQueryMethod().getParameters(), args);

		return partTreeQuery.createQuery(accessor);
	}

	@SuppressWarnings("unchecked")
	private TemplatePartQuery createQueryForMethod(Class<?> repositoryInterface, String methodName, Class<?>... paramTypes) {
		Class<?>[] userTypes = Arrays.stream(paramTypes)//
				.map(it -> it.getName().contains("Mockito") ? it.getSuperclass() : it)//
				.toArray(size -> new Class<?>[size]);
		try {
			Method method = repositoryInterface.getMethod(methodName, userTypes);
			ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
			TemplateQueryMethod queryMethod = new TemplateQueryMethod(method, new DefaultRepositoryMetadata(repositoryInterface), factory, mappingContext);

			return new TemplatePartQuery(queryMethod, operations);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new IllegalArgumentException(e.getMessage(), e);
		}
	}
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	class GroupKey implements Serializable {

		private static final long serialVersionUID = 1L;
		@Field(type = FieldType.Ip, index = true) private String groupname;
		@Field(pattern = "sdflsdf") private String hashPrefix;
		@Field(pattern = "XXxxxx") private String username;
	}
	
	@Data
	@NoArgsConstructor
	class Group {

		@Id private GroupKey id;

		private String email;
		private int age;

		public Group(GroupKey id) {
			this.id = id;
		}
	}
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public class AddressType {

		String city;
		String country;
	}

	@Model(indexName = "person", type = "xxXx")
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public class Person {

		private String id;
		@Field private String lastname;

		@Field private String firstname;

		private String nickname;
		private Date birthDate;
		private int numberOfChildren;
		private boolean cool;

		private LocalDate createdDate;
		private ZoneId zoneId;

		private AddressType mainAddress;
		private List<AddressType> alternativeAddresses;

		public Person(String firstname, String lastname) {

			this.firstname = firstname;
			this.lastname = lastname;
		}
	}
	
	@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@Query(exists = true)
	public @interface AllowFiltering {
	}
	
	interface GroupRepository extends TemplateRepository<Group, String> {

		Group findByIdHashPrefix(String hashPrefix);
	}

	interface Repo extends TemplateRepository<Person, String> {

		@Query
		Person findByLastname(String lastname);

		Person findTop3By();

		Person findByFirstnameAndLastname(String firstname, String lastname);
		Person findByFirstnameAndLastnameAndNicknameOrLastname(String firstname, String lastname, String see, String x);
		Person findByFirstnameOrLastnameOrNickname(String firstname, String lastname, String daa);

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

	interface PersonProjection {

		String getFirstname();

		String getLastname();
	}
}
