package io.github.hooj0.springdata.template.repository.query;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import io.github.hooj0.springdata.template.repository.query.complex.ExpressionEvaluatingParameterBinder.ParameterBinding;
import io.github.hooj0.springdata.template.repository.query.complex.StringBasedQuery.ParameterBindingParser;

public class ParameterBindingParserUnitTests {

	@Test // DATACASS-117
	public void parseWithoutParameters() {

		String query = "SELECT * FROM hello_world";
		List<ParameterBinding> bindings = new ArrayList<>();

		String transformed = ParameterBindingParser.INSTANCE.parseAndCollectParameterBindingsFromQueryIntoBindings(query, bindings);

		System.out.println(transformed);
		System.out.println(bindings);
	}

	@Test // DATACASS-117
	public void parseWithStaticParameters() {

		String query = "SELECT * FROM hello_world WHERE a = 1 AND b = {'list'} AND c = {'key':'value'}";
		List<ParameterBinding> bindings = new ArrayList<>();

		String transformed = ParameterBindingParser.INSTANCE.parseAndCollectParameterBindingsFromQueryIntoBindings(query, bindings);

		System.out.println(transformed);
		System.out.println(bindings);
	}

	@Test // DATACASS-117
	public void parseWithPositionalParameters() {

		String query = "SELECT * FROM hello_world WHERE a = ?0 and b = ?13";
		List<ParameterBinding> bindings = new ArrayList<>();

		String transformed = ParameterBindingParser.INSTANCE.parseAndCollectParameterBindingsFromQueryIntoBindings(query, bindings);

		System.out.println(transformed);
		System.out.println(bindings);
	}

	@Test // DATACASS-117
	public void parseWithNamedParameters() {

		String query = "SELECT * FROM hello_world WHERE a = :hello and b = :world";
		List<ParameterBinding> bindings = new ArrayList<>();

		String transformed = ParameterBindingParser.INSTANCE.parseAndCollectParameterBindingsFromQueryIntoBindings(query, bindings);

		System.out.println(transformed);
		System.out.println(bindings);
	}

	@Test // DATACASS-117
	public void parseWithIndexExpressionParameters() {

		String query = "SELECT * FROM hello_world WHERE a = ?#{[0]} and b = ?#{[2]}";
		List<ParameterBinding> bindings = new ArrayList<>();

		String transformed = ParameterBindingParser.INSTANCE.parseAndCollectParameterBindingsFromQueryIntoBindings(query, bindings);

		System.out.println(transformed);
		System.out.println(bindings);
	}

	@Test // DATACASS-117
	public void parseWithNameExpressionParameters() {

		String query = "SELECT * FROM hello_world WHERE a = :#{#a} and b = :#{#b}";
		List<ParameterBinding> bindings = new ArrayList<>();

		String transformed = ParameterBindingParser.INSTANCE.parseAndCollectParameterBindingsFromQueryIntoBindings(query, bindings);

		System.out.println(transformed);
		System.out.println(bindings);
	}

	@Test // DATACASS-117
	public void parseWithMixedParameters() {

		String query = "SELECT * FROM hello_world WHERE (a = ?1 and b = :name) and c = (:#{#a}) and (d = ?#{[1]})";
		List<ParameterBinding> bindings = new ArrayList<>();

		String transformed = ParameterBindingParser.INSTANCE.parseAndCollectParameterBindingsFromQueryIntoBindings(query, bindings);

		System.out.println(transformed);
		System.out.println(bindings);
	}
}
