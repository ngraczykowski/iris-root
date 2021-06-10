package com.silenteight.sep.usermanagement.keycloak.query.client;

import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class KeycloakClientConfiguration {

  @Bean
  KeycloakClientQuery keycloakClientQuery(RealmResource realmResource) {
    return new KeycloakClientQuery(realmResource.clients());
  }
}
