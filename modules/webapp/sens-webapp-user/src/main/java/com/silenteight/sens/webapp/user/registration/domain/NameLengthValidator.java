package com.silenteight.sens.webapp.user.registration.domain;

import io.vavr.control.Option;

public interface NameLengthValidator {

  Option<InvalidNameLength> validate(String name);

  class InvalidNameLength extends SimpleUserRegistrationDomainError {

    private static final long serialVersionUID = 7997935208993159823L;

    InvalidNameLength(String name, int minLength, int maxLength) {
      super(name + " has invalid length. "
          + "Should be between " + minLength +
          " and " + maxLength + " inclusive.");
    }
  }
}
