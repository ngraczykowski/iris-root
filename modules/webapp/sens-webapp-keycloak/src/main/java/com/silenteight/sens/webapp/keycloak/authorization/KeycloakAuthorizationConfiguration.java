package com.silenteight.sens.webapp.keycloak.authorization;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;

@Configuration
class KeycloakAuthorizationConfiguration {

  @Bean
  PermissionEvaluator keycloakPermissionEvaluator() {
    return new KeycloakPermissionEvaluator(() -> false);
  }
}
