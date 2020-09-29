package com.silenteight.sep.usermanagement.keycloak.query;

import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.sep.usermanagement.keycloak.query.lastlogintime.LastLoginTimeProvider;
import com.silenteight.sep.usermanagement.keycloak.query.role.InternalRoleFilter;
import com.silenteight.sep.usermanagement.keycloak.query.role.RolesProvider;

import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class KeycloakQueryConfiguration {

  @Bean
  KeycloakUserQuery keycloakUserQuery(
      LastLoginTimeProvider lastLoginTimeProvider,
      RolesProvider rolesProvider,
      UsersResource usersResource) {

    return new KeycloakUserQuery(
        usersResource,
        lastLoginTimeProvider,
        rolesProvider,
        DefaultTimeSource.TIME_CONVERTER);
  }

  @Bean
  KeycloakRolesQuery keycloakRolesQuery(
      RolesResource rolesResource, InternalRoleFilter internalRoleFilter) {

    return new KeycloakRolesQuery(rolesResource, internalRoleFilter);
  }

  @Bean
  KeycloakEventQuery keycloakEventQuery(RealmResource realmResource) {
    return new KeycloakEventQuery(realmResource);
  }

  @Bean
  KeycloakConfigurationQuery keycloakConfigurationQuery(RealmResource realmResource) {
    return new KeycloakConfigurationQuery(realmResource);
  }
}