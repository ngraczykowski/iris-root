package com.silenteight.sens.auth.authorization;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;

@Configuration
@EnableConfigurationProperties(AuthorizationProperties.class)
class KeycloakAuthorizationConfiguration {

  //this does not appear to be used at all
  //@Bean
  PermissionEvaluator keycloakPermissionEvaluator() {
    return new KeycloakPermissionEvaluator(() -> false);
  }
}
