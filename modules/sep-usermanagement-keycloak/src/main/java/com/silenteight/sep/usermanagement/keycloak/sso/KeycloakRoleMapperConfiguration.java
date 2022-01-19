package com.silenteight.sep.usermanagement.keycloak.sso;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.admin.client.resource.IdentityProvidersResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class KeycloakRoleMapperConfiguration {

  @Bean
  KeycloakSsoRoleMapper ssoRoleMapper(IdentityProvidersResource identityProvidersResource,
      ObjectMapper objectMapper) {
    return new KeycloakSsoRoleMapper(identityProvidersResource, objectMapper);
  }
}
