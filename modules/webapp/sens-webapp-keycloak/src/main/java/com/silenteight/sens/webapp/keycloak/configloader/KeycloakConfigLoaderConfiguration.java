package com.silenteight.sens.webapp.keycloak.configloader;

import lombok.NoArgsConstructor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.PartialImportRepresentation.Policy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.sens.webapp.keycloak.config.KeycloakConfiguration.KEYCLOAK_ADMIN_CLIENT;

@Configuration
@NoArgsConstructor
class KeycloakConfigLoaderConfiguration {

  @Bean
  public KeycloakHttpConfigLoader keycloakHttpConfigLoader(
      @Qualifier(KEYCLOAK_ADMIN_CLIENT) Keycloak apiClient,
      ObjectMapper objectMapper) {
    return new KeycloakHttpConfigLoader(
        Policy.OVERWRITE,
        new KeycloakRealmApiFacade(apiClient),
        new KeycloakConfigParser(objectMapper)
    );
  }
}
