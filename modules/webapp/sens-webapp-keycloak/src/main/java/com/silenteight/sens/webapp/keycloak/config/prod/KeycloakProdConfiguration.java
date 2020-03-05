package com.silenteight.sens.webapp.keycloak.config.prod;

import com.silenteight.sens.webapp.keycloak.config.KeycloakAdminClientFactory;
import com.silenteight.sens.webapp.keycloak.configloader.KeycloakConfigProvider;
import com.silenteight.sens.webapp.keycloak.configloader.provider.template.KeycloakConfigTemplateProvider;
import com.silenteight.sens.webapp.keycloak.configloader.provider.template.TemplateKeycloakConfigProvider;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
@EnableConfigurationProperties(KeycloakProdProperties.class)
public class KeycloakProdConfiguration {

  public static final String ADMIN_CLIENT_ID = "admin-cli";
  public static final String ADMIN_REALM = "master";

  @Bean
  TemplateKeycloakConfigProvider prodKeycloakConfigProvider(
      KeycloakProdProperties keycloakProdProperties,
      KeycloakConfigTemplateProvider keycloakConfigTemplateProvider) {
    return new TemplateKeycloakConfigProvider(
        keycloakProdProperties.getConfigTemplateName(),
        keycloakConfigTemplateProvider,
        keycloakProdProperties.getConfigValues()
    );
  }

  @Bean
  KeycloakAdminClientFactory prodKeycloakAdminClientFactory(
      KeycloakProdProperties keycloakProdProperties) {
    return () -> KeycloakBuilder.builder()
        .username(keycloakProdProperties.getUser())
        .password(keycloakProdProperties.getPassword())
        .clientId(ADMIN_CLIENT_ID)
        .realm(ADMIN_REALM)
        .serverUrl(keycloakProdProperties.getAuthServerUrl())
        .build();
  }

  @Bean
  ProdAdapterConfigFactory prodKeycloakClientAndAdapterProvider(
      KeycloakProdProperties keycloakProdProperties,
      ObjectMapper objectMapper,
      KeycloakConfigProvider keycloakConfigProvider
  ) {
    return new ProdAdapterConfigFactory(
        objectMapper,
        keycloakConfigProvider,
        keycloakProdProperties
    );
  }
}
