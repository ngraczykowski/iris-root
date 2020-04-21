package com.silenteight.sens.webapp.keycloak.usermanagement.update;

import com.silenteight.sens.webapp.audit.api.AuditLog;
import com.silenteight.sens.webapp.keycloak.usermanagement.assignrole.KeycloakUserRoleAssigner;
import com.silenteight.sens.webapp.keycloak.usermanagement.retrieval.KeycloakUserRetriever;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class KeycloakUserUpdateConfiguration {

  @Bean
  KeycloakUpdatedUserRepository keycloakUpdatedUserRepository(
      KeycloakUserRetriever keycloakUserRetriever,
      KeycloakUserRoleAssigner keycloakUserRoleAssigner,
      AuditLog auditLog) {

    return new KeycloakUpdatedUserRepository(
        new KeycloakUserUpdater(keycloakUserRetriever, keycloakUserRoleAssigner, auditLog));
  }
}
