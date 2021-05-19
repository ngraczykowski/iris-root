package com.silenteight.sep.usermanagement.keycloak.assignrole;

import com.silenteight.sep.usermanagement.keycloak.query.client.ClientQuery;

import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class KeycloakAssignRoleConfiguration {

  @Bean
  KeycloakUserRoleAssigner keycloakUserRoleAssigner(
      RolesResource rolesResource, UsersResource usersResource, ClientQuery clientQuery) {

    return new KeycloakUserRoleAssigner(usersResource, rolesResource, clientQuery);
  }
}
