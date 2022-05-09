package com.silenteight.sep.usermanagement.api.role;

import com.silenteight.sep.usermanagement.api.error.SimpleUserDomainError;

import java.util.Optional;
import java.util.Set;

import static java.lang.String.join;

public interface RoleValidator {

  Optional<RolesDontExistError> validate(String scope, Set<String> roles);

  class RolesDontExistError extends SimpleUserDomainError {

    private static final long serialVersionUID = -3940878504019982914L;

    public RolesDontExistError(Set<String> roles) {
      super("One of roles [" + join(",", roles) + "] do not exist in the system");
    }

    public RoleValidationException toException() {
      return RoleValidationException.of(getReason());
    }
  }
}
