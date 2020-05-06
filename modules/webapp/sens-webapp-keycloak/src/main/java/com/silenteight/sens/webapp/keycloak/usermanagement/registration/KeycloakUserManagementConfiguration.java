package com.silenteight.sens.webapp.keycloak.usermanagement.registration;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;
import com.silenteight.sens.webapp.keycloak.usermanagement.assignrole.KeycloakUserRoleAssigner;

import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@Configuration
@EnableSpringDataWebSupport
class KeycloakUserManagementConfiguration {

  @Bean
  KeycloakRegisteredUserRepository keycloakUserRegistrationRepository(
      UsersResource usersResource,
      KeycloakUserRoleAssigner keycloakUserRoleAssigner,
      AuditTracer auditTracer) {

    return new KeycloakRegisteredUserRepository(
        new KeycloakUserCreator(usersResource, auditTracer),
        keycloakUserRoleAssigner);
  }

  @Bean
  KeycloakUsernameUniquenessValidator keycloakUsernameValidator(UsersResource usersResource) {
    return new KeycloakUsernameUniquenessValidator(usersResource);
  }

  @Bean
  KeycloakRolesValidator keycloakRolesValidator(RolesResource rolesResource) {
    return new KeycloakRolesValidator(rolesResource);
  }
}
