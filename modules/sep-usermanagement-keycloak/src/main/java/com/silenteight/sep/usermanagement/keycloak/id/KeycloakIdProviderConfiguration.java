package com.silenteight.sep.usermanagement.keycloak.id;

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
