package com.silenteight.sens.webapp.keycloak.config.startup;

import com.silenteight.sens.webapp.keycloak.configmigration.KeycloakMigrationsLoader;
import com.silenteight.sens.webapp.keycloak.configmigration.KeycloakMigrator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImportKeycloakConfiguration {

  public static final String IMPORT_KEYCLOAK_CONFIG_BEAN = "importKeycloakConfig";

  @Bean(IMPORT_KEYCLOAK_CONFIG_BEAN)
  ImportKeycloakMigrationsTask importKeycloakConfig(
      KeycloakMigrator migrator,
      KeycloakMigrationsLoader migrationsLoader) {
    return new ImportKeycloakMigrationsTask(migrator, migrationsLoader);
  }
}
