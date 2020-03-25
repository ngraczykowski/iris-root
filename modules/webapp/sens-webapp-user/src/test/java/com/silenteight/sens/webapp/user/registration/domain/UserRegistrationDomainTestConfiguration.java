package com.silenteight.sens.webapp.user.registration.domain;

import com.silenteight.sens.webapp.user.domain.validator.RolesValidator;
import com.silenteight.sens.webapp.user.domain.validator.UsernameUniquenessValidator;
import com.silenteight.sens.webapp.user.domain.validator.ValidatorConfigurationTestConfiguration;

public class UserRegistrationDomainTestConfiguration {

  private final UserRegistrationDomainConfiguration configuration =
      new UserRegistrationDomainConfiguration();
  private final ValidatorConfigurationTestConfiguration validationConfiguration =
      new ValidatorConfigurationTestConfiguration();

  public UserRegisteringDomainService userRegisteringDomainService(
      UsernameUniquenessValidator usernameUniquenessValidator,
      RolesValidator rolesValidator) {

    return configuration.userRegisteringDomainService(
        usernameUniquenessValidator,
        rolesValidator,
        validationConfiguration.usernameLengthValidator(),
        validationConfiguration.displayNameLengthValidator(),
        validationConfiguration.usernameCharsValidator());
  }
}
