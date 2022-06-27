package com.silenteight.sens.webapp.user.remove;

import com.silenteight.sens.webapp.audit.api.trace.AuditTracer;
import com.silenteight.sens.webapp.user.config.RolesProperties;
import com.silenteight.sens.webapp.user.roles.UserRolesRetriever;
import com.silenteight.sep.usermanagement.api.user.UserQuery;
import com.silenteight.sep.usermanagement.api.user.UserRemover;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserRemoveUseCaseConfiguration {

  @Bean
  RemoveUserUseCase removeUserUseCase(
      UserQuery userQuery,
      UserRemover userRemover,
      AuditTracer auditTracer,
      UserRolesRetriever userRolesRetriever,
      RolesProperties rolesProperties) {

    return new RemoveUserUseCase(
        userQuery,
        userRemover,
        auditTracer,
        userRolesRetriever,
        rolesProperties.getRolesScope(),
        rolesProperties.getCountryGroupsScope());
  }
}
