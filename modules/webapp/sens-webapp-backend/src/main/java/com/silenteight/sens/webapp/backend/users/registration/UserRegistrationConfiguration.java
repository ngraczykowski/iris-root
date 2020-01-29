package com.silenteight.sens.webapp.backend.users.registration;

import com.silenteight.sens.webapp.backend.users.registration.domain.RolesValidator;
import com.silenteight.sens.webapp.backend.users.registration.domain.UserRegisteringDomainService;
import com.silenteight.sens.webapp.backend.users.registration.domain.UsernameValidator;
import com.silenteight.sens.webapp.common.time.DefaultTimeSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserRegistrationConfiguration {

  @Bean
  UserRegisteringDomainService userRegisteringService(
      UsernameValidator usernameValidator, RolesValidator rolesValidator) {
    return new UserRegisteringDomainService(
        rolesValidator,
        usernameValidator,
        DefaultTimeSource.INSTANCE
    );
  }

  @Bean
  RegisterUserUseCase registerUserUseCase(
      UserRegisteringDomainService userRegisteringService,
      RegisteredUserRepository registeredUserRepository) {
    return new RegisterUserUseCase(userRegisteringService, registeredUserRepository);
  }
}
