package com.silenteight.sens.webapp.keycloak.usermanagement.assignrole;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;

import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class KeycloakAssignRoleConfiguration {

  @Bean
  KeycloakUserRoleAssigner keycloakUserRoleAssigner(
      RolesResource rolesResource, UsersResource usersResource, AuditTracer auditTracer) {

    return new KeycloakUserRoleAssigner(usersResource, rolesResource, auditTracer);
  }
}
