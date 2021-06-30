package com.silenteight.sep.usermanagement.keycloak.query;

import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.sep.usermanagement.keycloak.config.KeycloakConfigurationProperties;
import com.silenteight.sep.usermanagement.keycloak.query.client.ClientQuery;
import com.silenteight.sep.usermanagement.keycloak.query.lastlogintime.LastLoginTimeProvider;
import com.silenteight.sep.usermanagement.keycloak.query.role.RolesProvider;

import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.RealmResource;
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
      ClientQuery clientQuery, ClientsResource clientsResource) {

    return new KeycloakRolesQuery(clientQuery, clientsResource);
  }

  @Bean
  KeycloakEventQuery keycloakEventQuery(RealmResource realmResource) {
    return new KeycloakEventQuery(realmResource);
  }

  @Bean
  KeycloakConfigurationQuery keycloakConfigurationQuery(
      RealmResource realmResource,
      KeycloakConfigurationProperties keycloakConfigurationProperties) {

    return new KeycloakConfigurationQuery(realmResource, keycloakConfigurationProperties);
  }
}
