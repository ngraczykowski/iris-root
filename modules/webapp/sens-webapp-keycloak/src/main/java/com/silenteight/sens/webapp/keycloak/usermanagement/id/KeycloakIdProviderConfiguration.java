package com.silenteight.sens.webapp.keycloak.usermanagement.id;

import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class KeycloakIdProviderConfiguration {

  @Bean
  SingleRequestKeycloakUserIdProvider keycloakUserIdProvider(UsersResource usersResource) {
    return new SingleRequestKeycloakUserIdProvider(usersResource);
  }
}
