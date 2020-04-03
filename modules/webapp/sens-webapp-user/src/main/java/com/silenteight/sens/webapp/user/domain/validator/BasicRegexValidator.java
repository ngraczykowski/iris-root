package com.silenteight.sens.webapp.user.domain.validator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import io.vavr.control.Option;

import java.util.regex.Pattern;

import static io.vavr.control.Option.none;
import static io.vavr.control.Option.of;

@RequiredArgsConstructor
class BasicRegexValidator implements RegexValidator {

  private final Pattern pattern;
  private final String errorMessage;

  @Override
  public Option<RegexError> validate(@NonNull String value) {
    if (!pattern.matcher(value).matches())
      return of(new RegexError(errorMessage));

    return none();
  }
}
