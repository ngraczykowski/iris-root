package com.silenteight.sens.webapp.user.domain.validator;

import lombok.NonNull;

import io.vavr.control.Option;

import static io.vavr.control.Option.none;

public class OptionalNameLengthValidator extends BasicNameLengthValidator {

  public OptionalNameLengthValidator(int minLength, int maxLength) {
    super(minLength, maxLength);
  }

  @Override
  public Option<InvalidNameLengthError> validate(@NonNull String name) {
    if (name.isEmpty())
      return none();

    return super.validate(name);
  }
}
