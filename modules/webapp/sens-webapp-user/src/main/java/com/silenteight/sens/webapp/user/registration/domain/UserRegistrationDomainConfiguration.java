package com.silenteight.sens.webapp.user.registration.domain;

import com.silenteight.sens.webapp.user.config.RolesProperties;
import com.silenteight.sens.webapp.user.domain.validator.NameLengthValidator;
import com.silenteight.sens.webapp.user.domain.validator.RegexValidator;
import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.sep.usermanagement.api.role.RoleValidator;
import com.silenteight.sep.usermanagement.api.user.UsernameUniquenessValidator;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserRegistrationDomainConfiguration {

  @Bean
  UserRegisteringDomainService userRegisteringDomainService(
      @Qualifier("usernameLengthValidator") NameLengthValidator usernameLengthValidator,
      @Qualifier("usernameCharsValidator") RegexValidator usernameCharsValidator,
      @Qualifier("displayNameLengthValidator") NameLengthValidator displayNameLengthValidator,
      RoleValidator roleValidator,
      UsernameUniquenessValidator usernameUniquenessValidator,
      @Qualifier("passwordCharsValidator") RegexValidator passwordCharsValidator,
      RolesProperties rolesProperties) {

    return new UserRegisteringDomainService(
        DefaultTimeSource.INSTANCE,
        usernameLengthValidator,
        usernameCharsValidator,
        displayNameLengthValidator,
        roleValidator,
        usernameUniquenessValidator,
        passwordCharsValidator,
        rolesProperties.getRolesScope());
  }
}
