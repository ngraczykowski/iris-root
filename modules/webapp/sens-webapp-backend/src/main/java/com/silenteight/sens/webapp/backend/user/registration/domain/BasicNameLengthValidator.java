package com.silenteight.sens.webapp.backend.user.registration.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import io.vavr.control.Option;

import static io.vavr.control.Option.*;

@RequiredArgsConstructor
@Getter
class BasicNameLengthValidator implements NameLengthValidator {

  private final int minLength;
  private final int maxLength;

  @Override
  public Option<InvalidNameLength> validate(@NonNull String name) {
    int length = name.length();
    boolean isValid = length >= minLength && length <= maxLength;

    if (!isValid)
      return of(new InvalidNameLength(name, minLength, maxLength));

    return none();
  }
}
