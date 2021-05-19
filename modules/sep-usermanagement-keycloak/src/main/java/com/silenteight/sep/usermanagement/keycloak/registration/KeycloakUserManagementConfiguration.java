package com.silenteight.sep.usermanagement.keycloak.registration;


import com.silenteight.sep.usermanagement.keycloak.assignrole.KeycloakUserRoleAssigner;
import com.silenteight.sep.usermanagement.keycloak.query.client.ClientQuery;

import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@Configuration
@EnableSpringDataWebSupport
class KeycloakUserManagementConfiguration {

  @Bean
  KeycloakRegisteredUserRepository keycloakUserRegistrationRepository(
      UsersResource usersResource,
      KeycloakUserRoleAssigner keycloakUserRoleAssigner) {

    return new KeycloakRegisteredUserRepository(
        new KeycloakUserCreator(usersResource),
        keycloakUserRoleAssigner);
  }

  @Bean
  KeycloakUsernameUniquenessValidator keycloakUsernameValidator(UsersResource usersResource) {
    return new KeycloakUsernameUniquenessValidator(usersResource);
  }

  @Bean
  KeycloakRolesValidator keycloakRolesValidator(
      ClientQuery clientQuery, RoleMappingResource roleMappingResource) {

    return new KeycloakRolesValidator(clientQuery, roleMappingResource);
  }
}
