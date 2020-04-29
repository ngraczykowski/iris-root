package com.silenteight.sens.webapp.keycloak.configmigration.migrator.api;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.PartialImportRepresentation.Policy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.sens.webapp.keycloak.config.KeycloakConfiguration.KEYCLOAK_ADMIN_CLIENT;

@Configuration
class MigrationApiFacadeConfiguration {

  @Bean
  KeycloakRealmMigrationApiFacade keycloakRealmMigrationApiFacade(
      @Qualifier(KEYCLOAK_ADMIN_CLIENT) Keycloak apiClient) {
    return new KeycloakRealmMigrationApiFacade(Policy.OVERWRITE, apiClient.realms());
  }
}
