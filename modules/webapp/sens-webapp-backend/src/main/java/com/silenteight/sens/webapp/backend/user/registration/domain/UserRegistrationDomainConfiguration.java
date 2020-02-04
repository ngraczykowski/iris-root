package com.silenteight.sens.webapp.backend.user.registration.domain;

import com.silenteight.sens.webapp.common.time.DefaultTimeSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserRegistrationDomainConfiguration {

  private static final int MIN_INTERNAL_USER_USERNAME_LENGTH = 3;
  private static final int MAX_INTERNAL_USER_USERNAME_LENGTH = 30;
  private static final int MIN_INTERNAL_USER_DISPLAYNAME_LENGTH = 3;
  private static final int MAX_INTERNAL_USER_DISPLAYNAME_LENGTH = 50;

  @Bean
  InternalUserRegisterer internalUserRegisterer(
      UsernameUniquenessValidator usernameUniquenessValidator,
      RolesValidator rolesValidator) {
    return new InternalUserRegisterer(
        DefaultTimeSource.INSTANCE,
        internalUsernameLengthValidator(),
        internalDisplayNameLengthValidator(),
        rolesValidator,
        usernameUniquenessValidator
    );
  }

  private static BasicNameLengthValidator internalUsernameLengthValidator() {
    return new BasicNameLengthValidator(
        MIN_INTERNAL_USER_USERNAME_LENGTH, MAX_INTERNAL_USER_USERNAME_LENGTH);
  }

  private static BasicNameLengthValidator internalDisplayNameLengthValidator() {
    return new BasicNameLengthValidator(
        MIN_INTERNAL_USER_DISPLAYNAME_LENGTH, MAX_INTERNAL_USER_DISPLAYNAME_LENGTH);
  }

  @Bean
  ExternalUserRegisterer externalUserRegisterer() {
    return new ExternalUserRegisterer();
  }

  @Bean
  UserRegisteringDomainService userRegisteringService(
      InternalUserRegisterer internalUserRegisterer,
      ExternalUserRegisterer externalUserRegisterer) {
    return new UserRegisteringDomainService(internalUserRegisterer, externalUserRegisterer);
  }
}
