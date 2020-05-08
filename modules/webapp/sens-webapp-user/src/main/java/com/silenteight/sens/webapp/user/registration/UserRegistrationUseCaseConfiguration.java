package com.silenteight.sens.webapp.user.registration;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;
import com.silenteight.sens.webapp.user.registration.domain.UserRegisteringDomainService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserRegistrationUseCaseConfiguration {

  @Bean
  RegisterInternalUserUseCase registerInternalUserUseCase(
      UserRegisteringDomainService userRegisteringDomainService,
      RegisteredUserRepository registeredUserRepository,
      AuditTracer auditTracer) {

    return new RegisterInternalUserUseCase(
        userRegisteringDomainService, registeredUserRepository, auditTracer);
  }

  @Bean
  RegisterExternalUserUseCase registerExternalUserUseCase(
      UserRegisteringDomainService userRegisteringDomainService,
      RegisteredUserRepository registeredUserRepository,
      AuditTracer auditTracer) {

    return new RegisterExternalUserUseCase(
        userRegisteringDomainService, registeredUserRepository, auditTracer);
  }
}
