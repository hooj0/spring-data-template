package io.github.hooj0.springdata.template.repository.cdi;

import io.github.hooj0.springdata.template.repository.TemplateRepository;
import io.github.hooj0.springdata.template.repository.domain.User;

@PersonDB
@OtherQualifier
public interface QualifiedUserRepository extends TemplateRepository<User, String> {

}
