package com.silenteight.sens.webapp.backend.users.registration.domain;

import com.silenteight.sens.webapp.backend.users.registration.UserRegistrationDomainError;

import java.util.Set;

import static java.lang.String.join;

public interface RolesValidator {

  boolean rolesExist(Set<String> roles);

  class RolesDontExist extends UserRegistrationDomainError {

    private static final long serialVersionUID = -3940878504019982914L;

    RolesDontExist(Set<String> roles) {
      super("One of roles [" + join(",", roles) + "] do not exist in the system");
    }
  }
}
