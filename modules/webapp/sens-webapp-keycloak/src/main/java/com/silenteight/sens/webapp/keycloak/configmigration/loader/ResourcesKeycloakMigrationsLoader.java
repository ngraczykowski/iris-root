package com.silenteight.sens.webapp.keycloak.configmigration.loader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.keycloak.configmigration.KeycloakMigrations;
import com.silenteight.sens.webapp.keycloak.configmigration.KeycloakMigrationsLoader;
import com.silenteight.sens.webapp.keycloak.configmigration.Migration;
import com.silenteight.sens.webapp.keycloak.configmigration.PartialMigration;
import com.silenteight.sens.webapp.keycloak.configmigration.loader.template.KeycloakConfigTemplateParser;

import io.vavr.control.Try;

import java.util.List;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.KEYCLOAK_MIGRATION;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
class ResourcesKeycloakMigrationsLoader implements KeycloakMigrationsLoader {

  private final KeycloakResourcesMigrationsLoaderProperties config;
  private final KeycloakConfigTemplateParser keycloakConfigTemplateParser;
  private final MigrationFilesLoader migrationFilesLoader;

  @Override
  public KeycloakMigrations load() {
    log.debug(KEYCLOAK_MIGRATION, "Reading Keycloak migration files");
    MigrationFiles migrationFiles = migrationFilesLoader.load(config.getMigrationsPath());

    if (migrationFiles.empty())
      return KeycloakMigrations.empty();

    log.info(KEYCLOAK_MIGRATION, "Loaded migration files. migrationFiles={}", migrationFiles);
    MigrationFile baseMigrationFile = getBaseMigration(migrationFiles);

    return new KeycloakMigrations(
        toMigration(baseMigrationFile),
        toPartialMigrations(migrationFiles.getPartialMigrations())
    );
  }

  private static MigrationFile getBaseMigration(MigrationFiles migrationFiles) {
    return migrationFiles
        .getBaseMigration()
        .orElseThrow(() -> new CouldNotLoadMigrationsException("No base migration found"));
  }

  private Migration toMigration(MigrationFile migrationFile) {
    return new Migration(migrationFile.getFilename(), parseIfTemplate(migrationFile));
  }

  private List<PartialMigration> toPartialMigrations(List<MigrationFile> partialMigrationFiles) {
    return partialMigrationFiles.stream().map(this::toPartialMigration).collect(toList());
  }

  private PartialMigration toPartialMigration(MigrationFile migrationFile) {
    return Try.of(() -> new PartialMigration(
        migrationFile.getFilename(),
        parseIfTemplate(migrationFile),
        migrationFile.getPolicy(),
        migrationFile.getOrder()
    )).getOrElseThrow(CouldNotLoadMigrationsException::new);
  }

  private String parseIfTemplate(MigrationFile migrationFile) {
    String migration = migrationFile.readToString()
        .getOrElseThrow(CouldNotLoadMigrationsException::new);

    return migrationFile.isTemplate() ? parseTemplate(migration) : migration;
  }

  private String parseTemplate(String migration) {
    log.debug(KEYCLOAK_MIGRATION, "Parsing template file");

    return keycloakConfigTemplateParser.parse(migration, config.getConfigTemplateProperties());
  }
}
