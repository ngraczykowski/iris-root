package com.silenteight.sens.webapp.user.domain.validator;

import com.silenteight.sep.usermanagement.api.error.SimpleUserDomainError;

import io.vavr.control.Option;

public interface RegexValidator {

  Option<RegexError> validate(String value);

  class RegexError extends SimpleUserDomainError {

    private static final long serialVersionUID = 1259998410863028001L;

    RegexError(String message) {
      super(message);
    }
  }
}
