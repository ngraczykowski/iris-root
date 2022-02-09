package com.silenteight.sens.webapp.user.registration.domain;

import com.silenteight.sens.webapp.user.config.RolesProperties;
import com.silenteight.sens.webapp.user.domain.validator.ValidatorConfigurationTestConfiguration;
import com.silenteight.sep.usermanagement.api.role.RoleValidator;
import com.silenteight.sep.usermanagement.api.user.UsernameUniquenessValidator;

public class UserRegistrationDomainTestConfiguration {

  private final UserRegistrationDomainConfiguration configuration =
      new UserRegistrationDomainConfiguration();
  private final ValidatorConfigurationTestConfiguration validationConfiguration =
      new ValidatorConfigurationTestConfiguration();

  public UserRegisteringDomainService userRegisteringDomainService(
      UsernameUniquenessValidator usernameUniquenessValidator,
      RoleValidator roleValidator,
      RolesProperties rolesProperties) {

    return configuration.userRegisteringDomainService(
        validationConfiguration.usernameLengthValidator(),
        validationConfiguration.usernameCharsValidator(),
        validationConfiguration.displayNameLengthValidator(),
        roleValidator,
        usernameUniquenessValidator,
        validationConfiguration.passwordCharsValidator(),
        rolesProperties);
  }
}
