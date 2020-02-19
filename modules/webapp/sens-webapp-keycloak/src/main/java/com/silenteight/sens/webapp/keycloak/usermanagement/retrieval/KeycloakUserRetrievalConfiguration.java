package com.silenteight.sens.webapp.keycloak.usermanagement.retrieval;

import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class KeycloakUserRetrievalConfiguration {

  @Bean
  KeycloakUserRetriever keycloakUserRetriever(UsersResource usersResource) {
    return new KeycloakUserRetriever(usersResource);
  }
}
