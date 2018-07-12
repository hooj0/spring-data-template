package io.github.hooj0.springdata.template.repository.domain;

import org.springframework.data.annotation.Id;

import io.github.hooj0.springdata.template.annotations.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Model(indexName = "userIndex", createIndex = true, type = "User")
public class User {

	@Id private String id;

	private String firstname;
	private String lastname;

	public User(String id, String firstname, String lastname) {
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
	}
}
