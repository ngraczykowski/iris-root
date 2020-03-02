package com.silenteight.sens.webapp.keycloak.configloader;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.PartialImportRepresentation.Policy;
import org.springframework.context.annotation.Configuration;

@Configuration
@NoArgsConstructor(access = AccessLevel.PACKAGE)
class KeycloakConfigLoaderConfiguration {

  // TODO(bgulowaty): Wire up (WA-410)
  KeycloakHttpConfigLoader keycloakHttpConfigLoader(Keycloak keycloak, ObjectMapper objectMapper) {
    return new KeycloakHttpConfigLoader(
        Policy.OVERWRITE,
        new KeycloakRealmApiFacade(keycloak),
        new KeycloakConfigParser(objectMapper)
    );
  }
}
