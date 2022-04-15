package com.silenteight.sens.webapp.user.domain.validator;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import io.vavr.control.Option;

@RequiredArgsConstructor
@Getter
class BasicNameLengthValidator implements NameLengthValidator {

  private final int minLength;
  private final int maxLength;

  @Override
  public Option<InvalidNameLengthError> validate(@NonNull String name) {
    int length = name.length();
    boolean isValid = length >= minLength && length <= maxLength;

    if (!isValid)
      return Option.of(new InvalidNameLengthError(name, minLength, maxLength));

    return Option.none();
  }
}
