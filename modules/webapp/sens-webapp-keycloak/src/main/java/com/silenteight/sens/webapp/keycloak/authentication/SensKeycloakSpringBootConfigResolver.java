package com.silenteight.sens.webapp.keycloak.authentication;

import lombok.RequiredArgsConstructor;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.spi.HttpFacade.Request;
import org.keycloak.representations.adapters.config.AdapterConfig;

@RequiredArgsConstructor
class SensKeycloakSpringBootConfigResolver implements KeycloakConfigResolver {

  private final AdapterConfig keycloakAdapterConfig;

  @Override
  public KeycloakDeployment resolve(Request facade) {
    return KeycloakDeploymentBuilder.build(keycloakAdapterConfig);
  }
}
