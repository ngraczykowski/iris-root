package com.silenteight.sens.webapp.user.registration.domain;

public class UserRegistrationDomainTestConfiguration {

  private final UserRegistrationDomainConfiguration configuration =
      new UserRegistrationDomainConfiguration();

  public UserRegisteringDomainService userRegisteringDomainService(
      UsernameUniquenessValidator usernameUniquenessValidator,
      RolesValidator rolesValidator) {

    return configuration.userRegisteringDomainService(usernameUniquenessValidator, rolesValidator);
  }
}
