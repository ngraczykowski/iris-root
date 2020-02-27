package com.silenteight.sens.webapp.user.registration.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.regex.Pattern;

import static com.silenteight.sens.webapp.common.time.DefaultTimeSource.INSTANCE;

@Configuration
class UserRegistrationDomainConfiguration {

  private static final int MIN_USERNAME_LENGTH = 3;
  private static final int MAX_USERNAME_LENGTH = 30;
  private static final int MIN_DISPLAY_NAME_LENGTH = 3;
  private static final int MAX_DISPLAY_NAME_LENGTH = 50;
  private static final String REGEX = "[a-z0-9._@\\-]*";
  private static final Pattern PATTERN = Pattern.compile(REGEX);

  @Bean
  UserRegisteringDomainService userRegisteringDomainService(
      UsernameUniquenessValidator usernameUniquenessValidator,
      RolesValidator rolesValidator) {

    return new UserRegisteringDomainService(
        INSTANCE,
        usernameLengthValidator(),
        usernameCharsValidator(),
        displayNameLengthValidator(),
        rolesValidator,
        usernameUniquenessValidator);
  }

  private static BasicNameLengthValidator usernameLengthValidator() {
    return new BasicNameLengthValidator(
        MIN_USERNAME_LENGTH, MAX_USERNAME_LENGTH);
  }

  private static BasicNameLengthValidator displayNameLengthValidator() {
    return new BasicNameLengthValidator(
        MIN_DISPLAY_NAME_LENGTH, MAX_DISPLAY_NAME_LENGTH);
  }

  private static BasicRegexValidator usernameCharsValidator() {
    return new BasicRegexValidator(PATTERN);
  }
}
