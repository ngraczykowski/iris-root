package com.silenteight.sep.usermanagement.keycloak.query;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.sep.usermanagement.keycloak.config.KeycloakConfigurationProperties;
import com.silenteight.sep.usermanagement.keycloak.query.client.ClientQuery;
import com.silenteight.sep.usermanagement.keycloak.query.lastlogintime.LastLoginTimeProvider;
import com.silenteight.sep.usermanagement.keycloak.query.role.RolesProvider;

import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(UsersListFilterProperties.class)
@RequiredArgsConstructor
class KeycloakQueryConfiguration {

  @Bean
  KeycloakUserQuery keycloakUserQuery(
      LastLoginTimeProvider lastLoginTimeProvider,
      RolesProvider rolesProvider,
      UsersResource usersResource,
      ClientsResource clientsResource,
      ClientQuery clientQuery,
      UsersListFilter usersListFilter) {

    return new KeycloakUserQuery(
        usersResource,
        lastLoginTimeProvider,
        rolesProvider,
        DefaultTimeSource.TIME_CONVERTER,
        clientsResource,
        clientQuery,
        usersListFilter);
  }

  @Bean
  UsersListFilter usersListFilter(
      UsersListFilterProperties usersListFilterProperties,
      RolesProvider rolesProvider) {

    AttributeFilter attributeFilter = new AttributeFilter(
        usersListFilterProperties.getAttributeFilters());
    RealmRoleFilter realmRoleFilter = new RealmRoleFilter(
        usersListFilterProperties.getRealmRoleFilterRoles(), rolesProvider);

    return new UsersListFilter(attributeFilter, realmRoleFilter);
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
