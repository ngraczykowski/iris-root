package com.silenteight.sep.usermanagement.keycloak.client;

import org.keycloak.admin.client.resource.ClientsResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ClientRoleManagerConfiguration {

  @Bean
  ClientRoleManager clientRoleManager(ClientsResource clientsResource) {
    return new ClientRoleManager(clientsResource);
  }
}
