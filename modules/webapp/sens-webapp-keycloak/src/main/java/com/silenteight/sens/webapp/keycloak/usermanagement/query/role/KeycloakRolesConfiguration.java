package com.silenteight.sens.webapp.keycloak.usermanagement.query.role;

import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Duration;

import static java.time.Duration.ofMinutes;

@Configuration
@EnableScheduling
class KeycloakRolesConfiguration {

  static final String CACHE_UPDATE_INTERVAL = "PT15M";

  static final Duration CACHE_EXPIRATION_DURATION = ofMinutes(30);

  @Bean
  RolesFetcher rolesFetcher(RolesResource rolesResource) {
    return new RolesFetcher(rolesResource);
  }

  @Bean
  CachedRolesProvider cachedRolesProvider(
      RealmResource realmResource, RolesFetcher rolesFetcher) {
    SingleRequestRoleProvider defaultProvider =
        new SingleRequestRoleProvider(realmResource);

    return new CachedRolesProvider(
        defaultProvider,
        rolesFetcher,
        10_000,
        CACHE_EXPIRATION_DURATION);
  }

  @Bean
  RolesCacheUpdater rolesCacheUpdater(
      CachedRolesProvider rolesProvider,
      RolesFetcher rolesFetcher) {
    return new RolesCacheUpdater(rolesFetcher, rolesProvider);
  }
}
