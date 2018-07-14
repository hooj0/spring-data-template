package io.github.hooj0.springdata.template.repository.query;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.AbstractRepositoryMetadata;
import org.springframework.data.repository.query.ExtensionAwareQueryMethodEvaluationContextProvider;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.ReflectionUtils;

import io.github.hooj0.springdata.template.annotations.Query;
import io.github.hooj0.springdata.template.core.MyTplTemplate;
import io.github.hooj0.springdata.template.core.TemplateOperations;
import io.github.hooj0.springdata.template.core.convert.MappingTemplateConverter;
import io.github.hooj0.springdata.template.core.mapping.SimpleTemplateMappingContext;
import io.github.hooj0.springdata.template.repository.domain.Personl;
import io.github.hooj0.springdata.template.repository.query.TemplatePartQueryTest.AddressType;
import io.github.hooj0.springdata.template.repository.query.complex.SimpleStatement;
import io.github.hooj0.springdata.template.repository.query.complex.StringBasedTemplateQuery;


public class StringBasedTemplateQueryUnitTests {

	private static final SpelExpressionParser PARSER = new SpelExpressionParser();

	private TemplateOperations operations;
	private RepositoryMetadata metadata;
	private MappingTemplateConverter converter;
	private ProjectionFactory factory;

	@Before
	public void setUp() {
		SimpleTemplateMappingContext mappingContext = new SimpleTemplateMappingContext();

		this.metadata = AbstractRepositoryMetadata.getMetadata(SampleRepository.class);
		this.converter = new MappingTemplateConverter(mappingContext);
		this.factory = new SpelAwareProxyProjectionFactory();
		this.operations = new MyTplTemplate("", converter);
	}

	@Test // DATACASS-117
	public void bindsIndexParameterCorrectly() {

		StringBasedTemplateQuery templateQuery = getQueryMethod("findByLastname", String.class);

		SimpleStatement actual = templateQuery.execute(new Object[] { "Matthews" });
		System.out.println(actual);
	}

	@Test // DATACASS-259
	public void bindsIndexParameterForComposedQueryAnnotationCorrectly() {

		StringBasedTemplateQuery templateQuery = getQueryMethod("findByComposedQueryAnnotation", String.class);
		ParametersParameterAccessor accessor = new ParametersParameterAccessor(templateQuery.getQueryMethod().getParameters(), new Object[] { "Matthews" });

		SimpleStatement actual = templateQuery.createQuery(accessor, "Matthews");
		System.out.println(actual);
	}

	@Test // DATACASS-117
	public void bindsAndEscapesIndexParameterCorrectly() {

		StringBasedTemplateQuery templateQuery = getQueryMethod("findByLastname", String.class);

		SimpleStatement actual = templateQuery.execute(new Object[] { "Mat\th'ew\"s" });
		System.out.println(actual);
	}

	@Test // DATACASS-117
	public void bindsAndEscapesBytesIndexParameterCorrectly() {

		StringBasedTemplateQuery templateQuery = getQueryMethod("findByLastname", String.class);

		SimpleStatement actual = templateQuery.execute(new Object[] {  ByteBuffer.wrap(new byte[] { 1, 2, 3, 4 }) });
		System.out.println(actual);
	}

	@Test // DATACASS-117
	public void bindsIndexParameterInListCorrectly() {

		StringBasedTemplateQuery templateQuery = getQueryMethod("findByLastNameIn", Collection.class);

		SimpleStatement actual = templateQuery.execute(new Object[] { Arrays.asList("White", "Heisenberg") });
		System.out.println(actual);
	}

	@Test // DATACASS-117
	public void bindsIndexParameterIsListCorrectly() {

		StringBasedTemplateQuery templateQuery = getQueryMethod("findByLastNamesAndAge", Collection.class, int.class);

		SimpleStatement actual = templateQuery.execute(new Object[] { Arrays.asList("White", "Heisenberg"), 42 });
		System.out.println(actual);
	}

	@Test // DATACASS-117
	public void bindsIndexParameterInSetCorrectly() {

		StringBasedTemplateQuery templateQuery = getQueryMethod("findByLastNameIn", Collection.class);

		SimpleStatement actual = templateQuery.execute(new Object[] { new HashSet<>(Arrays.asList("White", "Heisenberg")) });
		System.out.println(actual);
	}

	@Test // DATACASS-117
	public void bindsNamedParameterCorrectly() {

		StringBasedTemplateQuery templateQuery = getQueryMethod("findByNamedParameter", String.class, String.class);

		SimpleStatement actual = templateQuery.execute(new Object[] { "Walter", "Matthews" });
		System.out.println(actual);
	}

	@Test // DATACASS-117
	public void bindsIndexExpressionParameterCorrectly() {

		StringBasedTemplateQuery templateQuery = getQueryMethod("findByIndexExpressionParameter", String.class);

		SimpleStatement actual = templateQuery.execute(new Object[] { "Matthews" });
		System.out.println(actual);
	}

	@Test // DATACASS-117
	public void bindsExpressionParameterCorrectly() {

		StringBasedTemplateQuery templateQuery = getQueryMethod("findByExpressionParameter", String.class);

		SimpleStatement actual = templateQuery.execute(new Object[] { "Matthews" });
		System.out.println(actual);
	}

	@Test // DATACASS-117
	public void bindsConditionalExpressionParameterCorrectly() {

		StringBasedTemplateQuery templateQuery = getQueryMethod("findByConditionalExpressionParameter", String.class);

		SimpleStatement actual = templateQuery.execute(new Object[] { "Walter" });
		System.out.println(actual);
	}

	@Test // DATACASS-117
	public void bindsReusedParametersCorrectly() {

		StringBasedTemplateQuery templateQuery = getQueryMethod("findByLastnameUsedTwice", String.class);

		SimpleStatement actual = templateQuery.execute(new Object[] { "Matthews" });
		System.out.println(actual);
	}

	@Test // DATACASS-117
	public void bindsMultipleParametersCorrectly() {

		StringBasedTemplateQuery templateQuery = getQueryMethod("findByLastnameAndFirstname", String.class, String.class);

		SimpleStatement actual = templateQuery.execute(new Object[] { "Matthews", "John" });
		System.out.println(actual);
	}

	@Test // DATACASS-296
	public void bindsConvertedParameterCorrectly() {

		StringBasedTemplateQuery templateQuery = getQueryMethod("findByCreatedDate", LocalDate.class);

		SimpleStatement actual = templateQuery.execute(new Object[] { LocalDate.of(2010, 7, 4) });
		System.out.println(actual);
	}

	@Test // DATACASS-146
	public void shouldApplyConsistencyLevel() {

		StringBasedTemplateQuery templateQuery = getQueryMethod("findByLastname", String.class);

		SimpleStatement actual = templateQuery.execute(new Object[] { "Matthews" });
		System.out.println(actual);
	}

	private StringBasedTemplateQuery getQueryMethod(String name, Class<?>... args) {

		Method method = ReflectionUtils.findMethod(SampleRepository.class, name, args);
		TemplateQueryMethod queryMethod = new TemplateQueryMethod(method, metadata, factory, converter.getMappingContext());

		return new StringBasedTemplateQuery(queryMethod, operations, PARSER, ExtensionAwareQueryMethodEvaluationContextProvider.DEFAULT);
	}

	@SuppressWarnings("unused")
	private interface SampleRepository extends Repository<Personl, String> {

		@Query("SELECT * FROM person WHERE lastname = ?0;")
		Personl findByLastname(String lastname);

		@Query("SELECT * FROM person WHERE lastname = ?0 or firstname = ?0;")
		Personl findByLastnameUsedTwice(String lastname);

		@Query("SELECT * FROM person WHERE lastname = :lastname;")
		Personl findByNamedParameter(@Param("another") String another, @Param("lastname") String lastname);

		@Query("SELECT * FROM person WHERE lastname = :#{[0]};")
		Personl findByIndexExpressionParameter(String lastname);

		@Query("SELECT * FROM person WHERE lastnames = [?0] AND age = ?1;")
		Personl findByLastNamesAndAge(Collection<String> lastname, int age);

		@Query("SELECT * FROM person WHERE lastname = ?0 AND age = ?2;")
		Personl findByOutOfBoundsLastNameShouldFail(String lastname);

		@Query("SELECT * FROM person WHERE lastname = :unknown;")
		Personl findByUnknownParameterLastNameShouldFail(String lastname);

		@Query("SELECT * FROM person WHERE lastname IN (?0);")
		Personl findByLastNameIn(Collection<String> lastNames);

		@Query("SELECT * FROM person WHERE lastname = :#{#lastname};")
		Personl findByExpressionParameter(@Param("lastname") String lastname);

		@Query("SELECT * FROM person WHERE lastname = :#{#lastname == 'Matthews' ? 'Woohoo' : #lastname};")
		Personl findByConditionalExpressionParameter(@Param("lastname") String lastname);

		@Query("SELECT * FROM person WHERE lastname=?0 AND firstname=?1;")
		Personl findByLastnameAndFirstname(String lastname, String firstname);

		@Query("SELECT * FROM person WHERE createdDate=?0;")
		Personl findByCreatedDate(LocalDate createdDate);

		@Query("SELECT * FROM person WHERE address=?0;")
		Personl findByMainAddress(AddressType address);

		@ComposedQueryAnnotation
		Personl findByComposedQueryAnnotation(String lastname);
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Query("SELECT * FROM person WHERE lastname = ?0;")
	@interface ComposedQueryAnnotation { }

}
