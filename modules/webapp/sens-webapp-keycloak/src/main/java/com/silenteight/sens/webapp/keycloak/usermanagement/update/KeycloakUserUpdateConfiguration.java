package com.silenteight.sens.webapp.keycloak.usermanagement.update;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;
import com.silenteight.sens.webapp.keycloak.usermanagement.assignrole.KeycloakUserRoleAssigner;
import com.silenteight.sens.webapp.keycloak.usermanagement.query.role.RolesProvider;
import com.silenteight.sens.webapp.keycloak.usermanagement.retrieval.KeycloakUserRetriever;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class KeycloakUserUpdateConfiguration {

  @Bean
  KeycloakUpdatedUserRepository keycloakUpdatedUserRepository(
      KeycloakUserRetriever keycloakUserRetriever,
      KeycloakUserRoleAssigner keycloakUserRoleAssigner,
      RolesProvider rolesProvider,
      AuditTracer auditTracer) {

    return new KeycloakUpdatedUserRepository(
        new KeycloakUserUpdater(keycloakUserRetriever, keycloakUserRoleAssigner, auditTracer));
  }
}
