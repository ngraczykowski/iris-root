package com.silenteight.sens.webapp.user.registration;

import com.silenteight.sens.webapp.user.registration.domain.UserRegisteringDomainService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserRegistrationUseCaseConfiguration {

  @Bean
  RegisterInternalUserUseCase registerInternalUserUseCase(
      UserRegisteringDomainService userRegisteringDomainService,
      RegisteredUserRepository registeredUserRepository) {

    return new RegisterInternalUserUseCase(userRegisteringDomainService, registeredUserRepository);
  }

  @Bean
  RegisterExternalUserUseCase registerExternalUserUseCase(
      UserRegisteringDomainService userRegisteringDomainService,
      RegisteredUserRepository registeredUserRepository) {

    return new RegisterExternalUserUseCase(userRegisteringDomainService, registeredUserRepository);
  }
}
