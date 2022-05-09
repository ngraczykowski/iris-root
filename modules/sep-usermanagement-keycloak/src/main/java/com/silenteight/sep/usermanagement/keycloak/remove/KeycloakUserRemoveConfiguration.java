package com.silenteight.sep.usermanagement.keycloak.remove;

import com.silenteight.sep.usermanagement.api.user.UserRemover;
import com.silenteight.sep.usermanagement.keycloak.retrieval.KeycloakUserRetriever;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class KeycloakUserRemoveConfiguration {

  @Bean
  UserRemover keycloakUserRemover(KeycloakUserRetriever keycloakUserRetriever) {
    return new KeycloakUserRemover(keycloakUserRetriever);
  }
}
