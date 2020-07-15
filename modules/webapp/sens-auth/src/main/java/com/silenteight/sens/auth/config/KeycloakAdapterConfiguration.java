package com.silenteight.sens.auth.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.adapters.action.GlobalRequestResult;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.keycloak.representations.idm.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response;

@Configuration
@EnableConfigurationProperties(KeycloakAdapterConfigurationProperties.class)
public class KeycloakAdapterConfiguration {

  @Bean
  AdapterConfig adapterConfig(KeycloakAdapterConfigFactory keycloakAdapterConfigFactory) {
    return keycloakAdapterConfigFactory.getAdapter();
  }
}
