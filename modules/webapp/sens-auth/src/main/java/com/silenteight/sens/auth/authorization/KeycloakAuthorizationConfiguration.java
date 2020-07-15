package com.silenteight.sens.auth.authorization;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;


@Configuration
class KeycloakAuthorizationConfiguration {

  //this does not appear to be used at all
  //@Bean
  PermissionEvaluator keycloakPermissionEvaluator() {
    return new KeycloakPermissionEvaluator(() -> false);
  }
}
