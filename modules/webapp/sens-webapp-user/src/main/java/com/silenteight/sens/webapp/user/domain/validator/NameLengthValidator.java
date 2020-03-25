package com.silenteight.sens.webapp.user.domain.validator;

import com.silenteight.sens.webapp.user.update.exception.DisplayNameValidationException;

import io.vavr.control.Option;

public interface NameLengthValidator {

  Option<InvalidNameLengthError> validate(String name);

  class InvalidNameLengthError extends SimpleUserDomainError {

    private static final long serialVersionUID = 7997935208993159823L;

    InvalidNameLengthError(String name, int minLength, int maxLength) {
      super(name + " has invalid length. "
          + "Should be between " + minLength +
          " and " + maxLength + " inclusive.");
    }

    public DisplayNameValidationException toException() {
      return DisplayNameValidationException.of(getReason());
    }
  }
}
