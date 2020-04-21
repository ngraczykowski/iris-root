package com.silenteight.sens.webapp.user.registration;

import com.silenteight.sens.webapp.audit.api.AuditLog;
import com.silenteight.sens.webapp.user.registration.domain.UserRegisteringDomainService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserRegistrationUseCaseConfiguration {

  @Bean
  RegisterInternalUserUseCase registerInternalUserUseCase(
      UserRegisteringDomainService userRegisteringDomainService,
      RegisteredUserRepository registeredUserRepository,
      AuditLog auditLog) {

    return new RegisterInternalUserUseCase(
        userRegisteringDomainService, registeredUserRepository, auditLog);
  }

  @Bean
  RegisterExternalUserUseCase registerExternalUserUseCase(
      UserRegisteringDomainService userRegisteringDomainService,
      RegisteredUserRepository registeredUserRepository,
      AuditLog auditLog) {

    return new RegisterExternalUserUseCase(userRegisteringDomainService, registeredUserRepository,
        auditLog);
  }
}
