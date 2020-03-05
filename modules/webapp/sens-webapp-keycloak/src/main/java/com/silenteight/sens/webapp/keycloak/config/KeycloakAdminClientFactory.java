package com.silenteight.sens.webapp.keycloak.config;

import org.keycloak.admin.client.Keycloak;

public interface KeycloakAdminClientFactory {

  Keycloak getAdminClient();
}
