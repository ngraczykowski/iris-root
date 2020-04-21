package com.silenteight.sens.webapp.user.domain.validator;

import com.silenteight.sens.webapp.user.domain.validator.RegexValidator.RegexError;

import io.vavr.control.Option;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.*;

class BasicRegexValidatorTest {

  private static final Pattern DIGITS_PATTERN = Pattern.compile("\\d+");

  @Test
  void validatesOK() {
    BasicRegexValidator regexValidator = new BasicRegexValidator(DIGITS_PATTERN, " ");

    Option<RegexError> regexError = regexValidator.validate("123");
    assertThat(regexError).isEmpty();
  }

  @Test
  void containsValueInTheErrorMessage() {
    BasicRegexValidator regexValidator = new BasicRegexValidator(DIGITS_PATTERN, "%s is invalid");

    Option<RegexError> regexError = regexValidator.validate("abc");
    assertThat(regexError)
        .isNotEmpty()
        .allMatch(e -> e.getReason().equals("abc is invalid"));
  }

  @Test
  void doesNotContainsValueInTheErrorMessage() {
    BasicRegexValidator regexValidator =
        new BasicRegexValidator(DIGITS_PATTERN, "value should be numeric");

    Option<RegexError> regexError = regexValidator.validate("aaa");
    assertThat(regexError)
        .isNotEmpty()
        .allMatch(e -> e.getReason().equals("value should be numeric"));
  }

}
