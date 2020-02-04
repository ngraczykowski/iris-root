package com.silenteight.sens.webapp.keycloak.usermanagement.registration;

import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@Configuration
@EnableSpringDataWebSupport
class KeycloakUserManagementConfiguration {

  @Bean
  KeycloakRegisteredUserRepository keycloakUserRegistrationRepository(
      RolesResource rolesResource, UsersResource usersResource) {
    return new KeycloakRegisteredUserRepository(
        new KeycloakUserCreator(usersResource),
        new KeycloakUserRoleAssigner(usersResource, rolesResource)
    );
  }

  @Bean
  KeycloakUsernameUniquenessValidator keycloakUsernameValidator(UsersResource usersResource) {
    return new KeycloakUsernameUniquenessValidator(usersResource);
  }

  @Bean
  KeycloakRolesValidator keycloakRolesValidator(RolesResource rolesResource) {
    return new KeycloakRolesValidator(rolesResource);
  }
}
