package com.silenteight.sep.usermanagement.keycloak.query.client;

import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class KeycloakClientConfiguration {

  @Bean
  KeycloakClientQuery keycloakClientQuery(
      ClientsResource clientsResource, RealmResource realmResource) {

    return new KeycloakClientQuery(clientsResource, realmResource);
  }
}
