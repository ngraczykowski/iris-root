package com.silenteight.sens.webapp.user.registration;

import com.silenteight.sens.webapp.audit.api.trace.AuditTracer;
import com.silenteight.sens.webapp.user.config.RolesProperties;
import com.silenteight.sens.webapp.user.registration.domain.UserRegisteringDomainService;
import com.silenteight.sep.usermanagement.api.user.UserCreator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserRegistrationUseCaseConfiguration {

  @Bean
  RegisterInternalUserUseCase registerInternalUserUseCase(
      UserRegisteringDomainService userRegisteringDomainService,
      UserCreator userCreator,
      AuditTracer auditTracer,
      RolesProperties rolesProperties) {

    return new RegisterInternalUserUseCase(
        userRegisteringDomainService,
        userCreator,
        auditTracer,
        rolesProperties.getRolesScope(),
        rolesProperties.getCountryGroupsScope());
  }

  @Bean
  RegisterExternalUserUseCase registerExternalUserUseCase(
      UserRegisteringDomainService userRegisteringDomainService,
      UserCreator userCreator,
      AuditTracer auditTracer,
      RolesProperties rolesProperties) {

    return new RegisterExternalUserUseCase(
        userRegisteringDomainService,
        userCreator,
        auditTracer,
        rolesProperties.getRolesScope(),
        rolesProperties.getCountryGroupsScope());
  }
}
