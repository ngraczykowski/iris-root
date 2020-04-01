package com.silenteight.sens.webapp.user.domain.validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.regex.Pattern;

@SuppressWarnings("squid:S2068")
@Configuration
class ValidatorConfiguration {

  private static final int MIN_USERNAME_LENGTH = 3;
  private static final int MAX_USERNAME_LENGTH = 30;
  private static final int MIN_DISPLAY_NAME_LENGTH = 3;
  private static final int MAX_DISPLAY_NAME_LENGTH = 50;
  private static final String USERNAME_CHARS_REGEX = "[a-z0-9._@\\-]*";
  private static final Pattern USERNAME_CHARS_PATTERN = Pattern.compile(USERNAME_CHARS_REGEX);
  private static final String USERNAME_CHARS_MESSAGE =
      "Only lowercase letters, numbers and -_@. chars allowed.";
  private static final String PASSWORD_CHARS_REGEX = "^(?=.*?[0-9])(?=.*?[A-Za-z]).{8,}$";
  private static final Pattern PASSWORD_CHARS_PATTERN = Pattern.compile(PASSWORD_CHARS_REGEX);
  private static final String PASSWORD_CHARS_MESSAGE =
      "It should have at least 8 chars and contain at least one letter and one digit.";

  @Bean
  BasicNameLengthValidator usernameLengthValidator() {
    return new BasicNameLengthValidator(MIN_USERNAME_LENGTH, MAX_USERNAME_LENGTH);
  }

  @Bean
  BasicNameLengthValidator displayNameLengthValidator() {
    return new OptionalNameLengthValidator(MIN_DISPLAY_NAME_LENGTH, MAX_DISPLAY_NAME_LENGTH);
  }

  @Bean
  BasicRegexValidator usernameCharsValidator() {
    return new BasicRegexValidator(USERNAME_CHARS_PATTERN, USERNAME_CHARS_MESSAGE);
  }

  @Bean
  BasicRegexValidator passwordCharsValidator() {
    return new BasicRegexValidator(PASSWORD_CHARS_PATTERN, PASSWORD_CHARS_MESSAGE);
  }
}
