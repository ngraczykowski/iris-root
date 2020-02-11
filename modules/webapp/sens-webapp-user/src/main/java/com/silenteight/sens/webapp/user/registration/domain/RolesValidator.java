package com.silenteight.sens.webapp.user.registration.domain;

import io.vavr.control.Option;

import java.util.Set;

import static java.lang.String.join;

public interface RolesValidator {

  Option<RolesDontExist> validate(Set<String> roles);

  class RolesDontExist extends SimpleUserRegistrationDomainError {

    private static final long serialVersionUID = -3940878504019982914L;

    public RolesDontExist(Set<String> roles) {
      super("One of roles [" + join(",", roles) + "] do not exist in the system");
    }
  }
}
