package com.silenteight.sens.webapp.user.registration.domain;

import io.vavr.control.Option;

public interface RegexValidator {

  Option<RegexValidator.InvalidNameCharsError> validate(String name);

  class InvalidNameCharsError extends SimpleUserRegistrationDomainError {

    private static final long serialVersionUID = 2934904087256642065L;

    InvalidNameCharsError(String name) {
      super(name + " has invalid chars. "
          + "Only lowercase letters, numbers and -_@. chars allowed.");
    }
  }
}
