package com.silenteight.sens.webapp.user.registration.domain;

import io.vavr.control.Option;

public interface UsernameUniquenessValidator {

  Option<UsernameNotUniqueError> validate(String username);

  class UsernameNotUniqueError extends SimpleUserRegistrationDomainError {

    private static final long serialVersionUID = 2443691316375936859L;

    public UsernameNotUniqueError(String username) {
      super("Username " + username + " is not unique");
    }
  }
}
