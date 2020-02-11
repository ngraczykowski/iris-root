package com.silenteight.sens.webapp.keycloak;

import org.keycloak.OAuth2Constants;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class KeycloakConfiguration {

  @Bean
  Keycloak keycloak(KeycloakSpringBootProperties props) {
    return KeycloakBuilder.builder()
        .serverUrl(props.getAuthServerUrl())
        .realm(props.getRealm())
        .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
        .clientId(props.getResource())
        .clientSecret((String) props.getCredentials().get("secret"))
        .build();
  }

  @Bean
  KeycloakSpringBootConfigResolver keycloakConfigResolver() {
    return new KeycloakSpringBootConfigResolver();
  }

  @Bean
  RealmResource realmResource(Keycloak keycloak, KeycloakSpringBootProperties props) {
    return keycloak.realm(props.getRealm());
  }

  @Bean
  UsersResource usersResource(RealmResource realmResource) {
    return realmResource.users();
  }

  @Bean
  RolesResource rolesResource(RealmResource realmResource) {
    return realmResource.roles();
  }
}
