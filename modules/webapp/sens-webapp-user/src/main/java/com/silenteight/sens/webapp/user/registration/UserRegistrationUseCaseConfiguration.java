package com.silenteight.sens.webapp.user.registration;

import com.silenteight.sens.webapp.audit.api.trace.AuditTracer;
import com.silenteight.sens.webapp.user.config.RolesProperties;
import com.silenteight.sens.webapp.user.registration.domain.UserRegisteringDomainService;
import com.silenteight.sep.usermanagement.api.RegisteredUserRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserRegistrationUseCaseConfiguration {

  @Bean
  RegisterInternalUserUseCase registerInternalUserUseCase(
      UserRegisteringDomainService userRegisteringDomainService,
      RegisteredUserRepository registeredUserRepository,
      AuditTracer auditTracer,
      RolesProperties rolesProperties) {

    return new RegisterInternalUserUseCase(
        userRegisteringDomainService,
        registeredUserRepository,
        auditTracer,
        rolesProperties.getRolesScope(),
        rolesProperties.getCountryGroupsScope());
  }

  @Bean
  RegisterExternalUserUseCase registerExternalUserUseCase(
      UserRegisteringDomainService userRegisteringDomainService,
      RegisteredUserRepository registeredUserRepository,
      AuditTracer auditTracer,
      RolesProperties rolesProperties) {

    return new RegisterExternalUserUseCase(
        userRegisteringDomainService,
        registeredUserRepository,
        auditTracer,
        rolesProperties.getRolesScope(),
        rolesProperties.getCountryGroupsScope());
  }
}
