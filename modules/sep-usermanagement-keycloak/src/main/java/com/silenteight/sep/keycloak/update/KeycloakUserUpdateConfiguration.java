package com.silenteight.sep.keycloak.update;


import com.silenteight.sep.keycloak.assignrole.KeycloakUserRoleAssigner;
import com.silenteight.sep.keycloak.retrieval.KeycloakUserRetriever;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class KeycloakUserUpdateConfiguration {

  @Bean
  KeycloakUpdatedUserRepository keycloakUpdatedUserRepository(
      KeycloakUserRetriever keycloakUserRetriever,
      KeycloakUserRoleAssigner keycloakUserRoleAssigner) {

    return new KeycloakUpdatedUserRepository(
        new KeycloakUserUpdater(keycloakUserRetriever, keycloakUserRoleAssigner));
  }
}
