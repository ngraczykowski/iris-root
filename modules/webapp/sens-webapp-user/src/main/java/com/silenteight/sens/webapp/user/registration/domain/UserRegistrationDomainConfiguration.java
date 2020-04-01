package com.silenteight.sens.webapp.user.registration.domain;

import com.silenteight.sens.webapp.user.domain.validator.NameLengthValidator;
import com.silenteight.sens.webapp.user.domain.validator.RegexValidator;
import com.silenteight.sens.webapp.user.domain.validator.RolesValidator;
import com.silenteight.sens.webapp.user.domain.validator.UsernameUniquenessValidator;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.sens.webapp.common.time.DefaultTimeSource.INSTANCE;

@Configuration
class UserRegistrationDomainConfiguration {

  @Bean
  UserRegisteringDomainService userRegisteringDomainService(
      @Qualifier("usernameLengthValidator") NameLengthValidator usernameLengthValidator,
      @Qualifier("usernameCharsValidator") RegexValidator usernameCharsValidator,
      @Qualifier("displayNameLengthValidator") NameLengthValidator displayNameLengthValidator,
      RolesValidator rolesValidator,
      UsernameUniquenessValidator usernameUniquenessValidator,
      @Qualifier("passwordCharsValidator") RegexValidator passwordCharsValidator) {

    return new UserRegisteringDomainService(
        INSTANCE,
        usernameLengthValidator,
        usernameCharsValidator,
        displayNameLengthValidator,
        rolesValidator,
        usernameUniquenessValidator,
        passwordCharsValidator);
  }
}
