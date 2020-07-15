package com.silenteight.sens.auth.config;

import org.keycloak.representations.adapters.config.AdapterConfig;

public interface KeycloakAdapterConfigFactory {

  AdapterConfig getAdapter();
}
