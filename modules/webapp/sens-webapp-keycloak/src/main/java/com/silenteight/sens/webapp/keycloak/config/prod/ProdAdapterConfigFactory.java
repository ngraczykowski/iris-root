package com.silenteight.sens.webapp.keycloak.config.prod;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.keycloak.config.KeycloakAdapterConfigFactory;
import com.silenteight.sens.webapp.keycloak.configloader.KeycloakConfigProvider;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.keycloak.representations.idm.RealmRepresentation;

import static java.util.Map.of;

@RequiredArgsConstructor
public class ProdAdapterConfigFactory implements KeycloakAdapterConfigFactory {

  private final ObjectMapper objectMapper;
  private final KeycloakConfigProvider keycloakConfigProvider;
  private final KeycloakProdProperties keycloakProdProperties;

  // TODO(bgulowaty): made in a hurry, improve code in general
  @Override
  public AdapterConfig getAdapterConfig() {
    RealmRepresentation realmRepresentation = getRealmRepresentation();

    var adapterConfig = new AdapterConfig();
    adapterConfig.setAuthServerUrl(keycloakProdProperties.getAuthServerUrl());
    adapterConfig.setRealm(realmRepresentation.getRealm());
    adapterConfig.setResource("backend");
    adapterConfig.setSslRequired("external");
    adapterConfig.setPublicClient(false);
    adapterConfig.setConfidentialPort(0);
    adapterConfig.setPrincipalAttribute("preferred_username");
    adapterConfig.setCredentials(of("secret", keycloakProdProperties.getBackend().getSecret()));
    adapterConfig.setUseResourceRoleMappings(true);
    return adapterConfig;

  }

  private RealmRepresentation getRealmRepresentation() {
    return Try.of(() ->
        objectMapper.readValue(keycloakConfigProvider.json(), RealmRepresentation.class)
    ).get();
  }
}
