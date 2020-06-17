package com.silenteight.sens.webapp.keycloak.configmigration.migrator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.keycloak.configmigration.*;

import io.vavr.control.Try;

import java.util.function.Predicate;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.KEYCLOAK_MIGRATION;
import static one.util.streamex.StreamEx.of;

@Slf4j
@RequiredArgsConstructor
class KeycloakHttpMigrator implements KeycloakMigrator {

  private final KeycloakRealmMigrationApi keycloak;
  private final KeycloakHttpBodiesParser bodyParser;
  private final RealmNameExtractor realmNameExtractor;

  public void migrate(KeycloakMigrations migrations) {
    log.debug(KEYCLOAK_MIGRATION, "Migrating config through HTTP");

    Migration baseMigration = getBaseMigration(migrations);
    String realmName = realmNameExtractor.extractRealmName(baseMigration);

    boolean isFirstRun = !keycloak.realmExists(realmName);

    log.info(KEYCLOAK_MIGRATION, "isFirstMigration={}", isFirstRun);
    if (isFirstRun)
      performBaseMigration(baseMigration);

    Predicate<PartialMigration> firstRunOrPolicyIsNotOnFirstRun =
        migration -> isFirstRun || migration.policy() != MigrationPolicy.ON_FIRST_RUN;

    of(migrations.partialMigrations())
        .sortedByLong(PartialMigration::ordinal)
        .filter(firstRunOrPolicyIsNotOnFirstRun)
        .peek(migration -> log.info(KEYCLOAK_MIGRATION, "Migrating {}", migration.name()))
        .forEach(migration -> performPartialImport(realmName, migration));
  }

  private void performPartialImport(String realmName, PartialMigration migration) {
    Try.of(() -> bodyParser.parse(migration))
        .flatMap(representation -> Try.run(() -> keycloak.partialImport(realmName, representation)))
        .onFailure(problem -> log.error(KEYCLOAK_MIGRATION, "Could not perform partial migration",
            problem))
        .onSuccess(unused -> log.debug("Partial migration successful"))
        .getOrElseThrow(CouldNotPerformMigrationException::new);
  }

  private void performBaseMigration(Migration baseMigration) {
    log.info(KEYCLOAK_MIGRATION, "Migrating realm. migrationName={}", baseMigration.name());
    Try.run(() -> keycloak.createRealm(bodyParser.parse(baseMigration)))
        .onFailure(problem -> log.error(KEYCLOAK_MIGRATION, "Could not create realm", problem))
        .onSuccess(unused -> log.debug("Realm creation successful"))
        .getOrElseThrow(CouldNotPerformMigrationException::new);
  }

  private static Migration getBaseMigration(KeycloakMigrations migrations) {
    if (migrations.baseMigration() == null)
      throw new CouldNotPerformMigrationException("No base migration is provided");

    return migrations.baseMigration();
  }
}
