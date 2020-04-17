package com.silenteight.sens.webapp.keycloak.config.startup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.keycloak.configmigration.KeycloakMigrationsLoader;
import com.silenteight.sens.webapp.keycloak.configmigration.KeycloakMigrator;

import io.vavr.control.Try;

import javax.annotation.PostConstruct;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.KEYCLOAK_MIGRATION;

@Slf4j
@RequiredArgsConstructor
class ImportKeycloakMigrationsTask {

  private final KeycloakMigrator migrator;
  private final KeycloakMigrationsLoader migrationsLoader;

  @PostConstruct
  public void doImport() {
    log.info(KEYCLOAK_MIGRATION, "Loading Keycloak migrations");

    Try.of(migrationsLoader::load)
        .andThenTry(migrator::migrate)
        .getOrElseThrow(CouldNotImportKeycloakMigrationsOnStartup::new);
  }

  private static class CouldNotImportKeycloakMigrationsOnStartup extends RuntimeException {

    private static final long serialVersionUID = 3542807187955892326L;

    CouldNotImportKeycloakMigrationsOnStartup(Throwable cause) {
      super(cause);
    }
  }
}

