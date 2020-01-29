package com.silenteight.sens.webapp.backend.users.registration.domain;

import com.silenteight.sens.webapp.backend.users.registration.UserRegistrationDomainError;

public interface UsernameValidator {

  boolean isUnique(String username);

  class UsernameNotUnique extends UserRegistrationDomainError {

    private static final long serialVersionUID = 2443691316375936859L;

    UsernameNotUnique(String username) {
      super("Username " + username + " is not unique");
    }
  }
}
