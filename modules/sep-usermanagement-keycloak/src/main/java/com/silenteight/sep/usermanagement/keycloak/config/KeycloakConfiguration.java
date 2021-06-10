package com.silenteight.sep.usermanagement.keycloak.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KeycloakConfigurationProperties.class)
public class KeycloakConfiguration {

  public static final String KEYCLOAK_WEBAPP_CLIENT = "keycloakWebappClient";

  @Bean(KEYCLOAK_WEBAPP_CLIENT)
  Keycloak keycloakWebappClient(KeycloakConfigurationProperties keycloakConfigurationProperties) {
    AdapterConfig adapterConfig = keycloakConfigurationProperties.getAdapter();
    return KeycloakBuilder.builder()
        .clientId(adapterConfig.getResource())
        .clientSecret((String) adapterConfig.getCredentials().get("secret"))
        .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
        .realm(adapterConfig.getRealm())
        .serverUrl(adapterConfig.getAuthServerUrl())
        .build();
  }

  @Bean
  RealmResource realmResource(@Qualifier(KEYCLOAK_WEBAPP_CLIENT) Keycloak keycloak) {
    return keycloak.realm("sens-webapp");
  }

  @Bean
  UsersResource usersResource(RealmResource realmResource) {
    return realmResource.users();
  }

  @Bean
  ClientsResource clientsResource(RealmResource realmResource) {
    return realmResource.clients();
  }

  @Bean
  RolesResource rolesResource(RealmResource realmResource) {
    return realmResource.roles();
  }
}
