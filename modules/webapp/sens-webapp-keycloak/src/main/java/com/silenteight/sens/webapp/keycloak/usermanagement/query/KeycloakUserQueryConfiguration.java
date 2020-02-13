package com.silenteight.sens.webapp.keycloak.usermanagement.query;

import com.silenteight.sens.webapp.common.time.DefaultTimeSource;
import com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime.LastLoginTimeProvider;
import com.silenteight.sens.webapp.keycloak.usermanagement.query.role.RolesProvider;

import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class KeycloakUserQueryConfiguration {

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
}
