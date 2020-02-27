package com.silenteight.sens.webapp.user.registration.domain;

import lombok.RequiredArgsConstructor;

import io.vavr.control.Option;

import java.util.regex.Pattern;

@RequiredArgsConstructor
class BasicRegexValidator implements RegexValidator {

  private final Pattern pattern;

  @Override
  public Option<InvalidNameCharsError> validate(String name) {
    if (!pattern.matcher(name).matches())
      return Option.of(new InvalidNameCharsError(name));

    return Option.none();
  }
}
