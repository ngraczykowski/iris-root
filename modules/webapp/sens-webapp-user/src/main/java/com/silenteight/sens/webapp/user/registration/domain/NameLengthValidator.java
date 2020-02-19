package com.silenteight.sens.webapp.user.registration.domain;

import io.vavr.control.Option;

public interface NameLengthValidator {

  Option<InvalidNameLengthError> validate(String name);

  class InvalidNameLengthError extends SimpleUserRegistrationDomainError {

    private static final long serialVersionUID = 7997935208993159823L;

    InvalidNameLengthError(String name, int minLength, int maxLength) {
      super(name + " has invalid length. "
          + "Should be between " + minLength +
          " and " + maxLength + " inclusive.");
    }
  }
}
