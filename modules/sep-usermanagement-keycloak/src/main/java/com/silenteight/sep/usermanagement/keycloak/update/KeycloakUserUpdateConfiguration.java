package com.silenteight.sep.usermanagement.keycloak.update;


import com.silenteight.sep.usermanagement.keycloak.assignrole.KeycloakUserRoleAssigner;
import com.silenteight.sep.usermanagement.keycloak.retrieval.KeycloakUserRetriever;

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
