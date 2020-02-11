package com.silenteight.sens.webapp.user.registration.domain;

import io.vavr.control.Option;

public interface UsernameUniquenessValidator {

  Option<UsernameNotUnique> validate(String username);

  class UsernameNotUnique extends SimpleUserRegistrationDomainError {

    private static final long serialVersionUID = 2443691316375936859L;

    public UsernameNotUnique(String username) {
      super("Username " + username + " is not unique");
    }
  }
}
