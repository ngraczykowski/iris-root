package com.silenteight.sep.usermanagement.keycloak.config;

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

import static org.keycloak.OAuth2Constants.CLIENT_CREDENTIALS;

@Configuration
@EnableConfigurationProperties(KeycloakConfigurationProperties.class)
public class KeycloakConfiguration {

  public static final String KEYCLOAK_WEBAPP_CLIENT = "keycloakWebappClient";

  @Bean(KEYCLOAK_WEBAPP_CLIENT)
  Keycloak keycloakWebappClient(KeycloakConfigurationProperties properties) {
    AdapterConfig adapterConfig = properties.getAdapter();
    return KeycloakBuilder.builder()
        .clientId(adapterConfig.getResource())
        .clientSecret((String) adapterConfig.getCredentials().get("secret"))
        .grantType(CLIENT_CREDENTIALS)
        .realm(adapterConfig.getRealm())
        .serverUrl(adapterConfig.getAuthServerUrl())
        .build();
  }

  @Bean
  RealmResource realmResource(
      @Qualifier(KEYCLOAK_WEBAPP_CLIENT) Keycloak keycloak,
      KeycloakConfigurationProperties properties) {

    return keycloak.realm(properties.getRealm());
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
