package com.silenteight.sens.webapp.backend.user.registration;

import com.silenteight.sens.webapp.backend.user.registration.domain.UserRegisteringDomainService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserRegistrationUseCaseConfiguration {

  @Bean
  RegisterInternalUserUseCase registerInternalUserUseCase(
      UserRegisteringDomainService userRegisteringService,
      RegisteredUserRepository registeredUserRepository) {
    return new RegisterInternalUserUseCase(userRegisteringService, registeredUserRepository);
  }
}
