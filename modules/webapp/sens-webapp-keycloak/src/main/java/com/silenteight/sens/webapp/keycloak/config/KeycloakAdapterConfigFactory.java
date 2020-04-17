package com.silenteight.sens.webapp.keycloak.config;

import org.keycloak.representations.adapters.config.AdapterConfig;

public interface KeycloakAdapterConfigFactory {

  AdapterConfig getAdapter();
}
