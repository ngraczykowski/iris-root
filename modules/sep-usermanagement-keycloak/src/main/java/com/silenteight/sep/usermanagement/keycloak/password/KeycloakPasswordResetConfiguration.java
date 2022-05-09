package com.silenteight.sep.usermanagement.keycloak.password;


import com.silenteight.sep.usermanagement.keycloak.id.KeycloakUserIdProvider;

import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class KeycloakPasswordResetConfiguration {

  @Bean
  KeycloakUserCredentialsRepository keycloakUserCredentialsRepository(
      KeycloakUserIdProvider keycloakUserIdProvider, UsersResource usersResource) {
    return new KeycloakUserCredentialsRepository(usersResource, keycloakUserIdProvider);
  }
}
