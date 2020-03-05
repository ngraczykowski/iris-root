package com.silenteight.sens.webapp.keycloak.authorization;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.access.PermissionEvaluator;

import static com.silenteight.sens.webapp.keycloak.config.startup.ImportKeycloakConfiguration.IMPORT_KEYCLOAK_CONFIG_BEAN;

@Configuration
@DependsOn(IMPORT_KEYCLOAK_CONFIG_BEAN)
class KeycloakAuthorizationConfiguration {

  @Bean
  PermissionEvaluator keycloakPermissionEvaluator() {
    return new KeycloakPermissionEvaluator(() -> false);
  }
}
