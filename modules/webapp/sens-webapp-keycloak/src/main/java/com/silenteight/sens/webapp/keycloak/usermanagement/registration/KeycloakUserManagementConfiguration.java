package com.silenteight.sens.webapp.keycloak.usermanagement.registration;

import com.silenteight.sens.webapp.audit.api.AuditLog;
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
      UsersResource usersResource, KeycloakUserRoleAssigner keycloakUserRoleAssigner,
      AuditLog auditLog) {

    return new KeycloakRegisteredUserRepository(
        new KeycloakUserCreator(usersResource, auditLog),
        keycloakUserRoleAssigner,
        auditLog);
  }

  @Bean
  KeycloakUsernameUniquenessValidator keycloakUsernameValidator(
      UsersResource usersResource, AuditLog auditLog) {
    return new KeycloakUsernameUniquenessValidator(usersResource, auditLog);
  }

  @Bean
  KeycloakRolesValidator keycloakRolesValidator(RolesResource rolesResource) {
    return new KeycloakRolesValidator(rolesResource);
  }
}
