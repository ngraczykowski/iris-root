package com.silenteight.sens.webapp.user.registration.domain;

import io.vavr.control.Option;

import java.util.Set;

import static java.lang.String.join;

public interface RolesValidator {

  Option<RolesDontExistError> validate(Set<String> roles);

  class RolesDontExistError extends SimpleUserRegistrationDomainError {

    private static final long serialVersionUID = -3940878504019982914L;

    public RolesDontExistError(Set<String> roles) {
      super("One of roles [" + join(",", roles) + "] do not exist in the system");
    }
  }
}
