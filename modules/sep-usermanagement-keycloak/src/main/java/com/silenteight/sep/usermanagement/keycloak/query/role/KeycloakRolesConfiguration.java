package com.silenteight.sep.usermanagement.keycloak.query.role;

import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
class KeycloakRolesConfiguration {

  @Bean
  RolesProvider rolesProvider(RealmResource realmResource, InternalRoleFilter internalRoleFilter) {
    return new SingleRequestRoleProvider(realmResource, internalRoleFilter);
  }

  @Bean
  InternalRoleFilter internalRoleFilter(RolesResource rolesResource) {
    return new InternalRoleFilter(rolesResource);
  }
}