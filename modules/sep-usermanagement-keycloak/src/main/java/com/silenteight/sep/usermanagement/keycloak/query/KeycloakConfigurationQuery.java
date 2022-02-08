package com.silenteight.sep.usermanagement.keycloak.query;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import com.silenteight.sep.usermanagement.api.configuration.ConfigurationQuery;
import com.silenteight.sep.usermanagement.api.configuration.dto.AuthConfigurationDto;
import com.silenteight.sep.usermanagement.keycloak.config.KeycloakConfigurationProperties;
import com.silenteight.sep.usermanagement.keycloak.query.dto.KeycloakAuthConfigurationDto;
import com.silenteight.sep.usermanagement.keycloak.query.dto.KeycloakDto;

import org.keycloak.admin.client.resource.RealmResource;

import static java.util.Optional.ofNullable;

@Slf4j
@RequiredArgsConstructor
class KeycloakConfigurationQuery implements ConfigurationQuery {

  private static final int DEFAULT_SESSION_IDLE_TIMEOUT_SECONDS = 1800;

  @NonNull
  private final RealmResource realmResource;

  @NonNull
  private final KeycloakConfigurationProperties keycloakConfigurationProperties;

  @Override
  public int getSessionIdleTimeoutSeconds() {
    try {
      return ofNullable(realmResource.toRepresentation().getSsoSessionIdleTimeout())
          .orElse(DEFAULT_SESSION_IDLE_TIMEOUT_SECONDS);
    } catch (Exception e) {
      log.error("Could not get session idle timeout", e);
      return DEFAULT_SESSION_IDLE_TIMEOUT_SECONDS;
    }
  }

  @Override
  public AuthConfigurationDto getAuthConfiguration() {
    KeycloakDto keycloak = KeycloakDto.builder()
        .url(keycloakConfigurationProperties.getAuthServerUrl())
        .realm(keycloakConfigurationProperties.getRealm())
        .clientId(keycloakConfigurationProperties.getFrontendClientId())
        .build();

    return new KeycloakAuthConfigurationDto(keycloak);
  }
}
