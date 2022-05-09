package com.silenteight.sep.usermanagement.keycloak.config;

import com.silenteight.sep.usermanagement.keycloak.query.KeycloakIdentityProviderQuery;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.*;
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

  @Bean
  IdentityProvidersResource identityProvidersResource(RealmResource realmResource) {
    return realmResource.identityProviders();
  }

  @Bean
  KeycloakIdentityProviderQuery identityProviderQuery(
      IdentityProvidersResource identityProvidersResource) {
    return new KeycloakIdentityProviderQuery(identityProvidersResource);
  }

  @Bean
  ObjectMapper sepUserManagementKeycloakObjectMapper() {
    return new ObjectMapper().registerModule(new JavaTimeModule());
  }
}
