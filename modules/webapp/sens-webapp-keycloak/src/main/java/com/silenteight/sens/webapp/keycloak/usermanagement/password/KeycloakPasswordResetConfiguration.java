package com.silenteight.sens.webapp.keycloak.usermanagement.password;

import com.silenteight.sens.webapp.audit.api.AuditLog;
import com.silenteight.sens.webapp.keycloak.usermanagement.id.KeycloakUserIdProvider;

import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class KeycloakPasswordResetConfiguration {

  @Bean
  KeycloakUserCredentialsRepository keycloakUserCredentialsRepository(
      KeycloakUserIdProvider keycloakUserIdProvider, UsersResource usersResource,
      AuditLog auditLog) {
    return new KeycloakUserCredentialsRepository(usersResource, keycloakUserIdProvider, auditLog);
  }
}
