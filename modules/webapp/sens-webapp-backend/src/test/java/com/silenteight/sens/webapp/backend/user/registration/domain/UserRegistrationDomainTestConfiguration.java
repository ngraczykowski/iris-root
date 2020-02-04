package com.silenteight.sens.webapp.backend.user.registration.domain;

public class UserRegistrationDomainTestConfiguration {

  private final UserRegistrationDomainConfiguration configuration =
      new UserRegistrationDomainConfiguration();

  public UserRegisteringDomainService userRegisteringService(
      UsernameUniquenessValidator usernameUniquenessValidator,
      RolesValidator rolesValidator
  ) {
    return configuration.userRegisteringService(
        configuration.internalUserRegisterer(usernameUniquenessValidator, rolesValidator),
        configuration.externalUserRegisterer()
    );
  }
}
