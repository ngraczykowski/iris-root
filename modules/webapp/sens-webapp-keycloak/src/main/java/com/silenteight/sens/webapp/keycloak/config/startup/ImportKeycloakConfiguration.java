package com.silenteight.sens.webapp.keycloak.config.startup;

import com.silenteight.sens.webapp.keycloak.configloader.KeycloakConfigProvider;
import com.silenteight.sens.webapp.keycloak.configloader.KeycloakHttpConfigLoader;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImportKeycloakConfiguration {

  public static final String IMPORT_KEYCLOAK_CONFIG_BEAN = "importKeycloakConfig";

  @Bean(IMPORT_KEYCLOAK_CONFIG_BEAN)
  ImportKeycloakConfigTask importKeycloakConfig(
      KeycloakHttpConfigLoader keycloakHttpConfigLoader,
      KeycloakConfigProvider keycloakConfigProvider) {
    return new ImportKeycloakConfigTask(keycloakHttpConfigLoader, keycloakConfigProvider);
  }
}
