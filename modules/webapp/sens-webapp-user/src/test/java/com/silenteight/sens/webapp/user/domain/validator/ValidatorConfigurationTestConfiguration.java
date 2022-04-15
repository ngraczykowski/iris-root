package com.silenteight.sens.webapp.user.domain.validator;

public class ValidatorConfigurationTestConfiguration {

  ValidatorConfiguration validatorConfiguration = new ValidatorConfiguration();

  public NameLengthValidator usernameLengthValidator() {
    return validatorConfiguration.usernameLengthValidator();
  }

  public NameLengthValidator displayNameLengthValidator() {
    return validatorConfiguration.displayNameLengthValidator();
  }

  public RegexValidator usernameCharsValidator() {
    return validatorConfiguration.usernameCharsValidator();
  }

  public RegexValidator passwordCharsValidator() {
    return validatorConfiguration.passwordCharsValidator();
  }
}
