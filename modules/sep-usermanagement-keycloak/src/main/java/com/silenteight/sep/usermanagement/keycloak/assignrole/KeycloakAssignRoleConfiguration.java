package com.silenteight.sep.usermanagement.keycloak.assignrole;

import com.silenteight.sep.usermanagement.keycloak.client.ClientRoleManager;
import com.silenteight.sep.usermanagement.keycloak.query.client.ClientQuery;

import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class KeycloakAssignRoleConfiguration {

  @Bean
  KeycloakUserRoleAssigner keycloakUserRoleAssigner(
      UsersResource usersResource,
      ClientQuery clientQuery,
      ClientsResource clientsResource,
      ClientRoleManager clientRoleManager) {

    return new KeycloakUserRoleAssigner(
        usersResource, clientQuery, clientsResource, clientRoleManager);
  }
}
