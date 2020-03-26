package com.silenteight.sens.webapp.user.domain.validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.regex.Pattern;

@Configuration
class ValidatorConfiguration {

  private static final int MIN_USERNAME_LENGTH = 3;
  private static final int MAX_USERNAME_LENGTH = 30;
  private static final int MIN_DISPLAY_NAME_LENGTH = 3;
  private static final int MAX_DISPLAY_NAME_LENGTH = 50;
  private static final String REGEX = "[a-z0-9._@\\-]*";
  private static final Pattern PATTERN = Pattern.compile(REGEX);

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
    return new BasicRegexValidator(PATTERN);
  }
}
