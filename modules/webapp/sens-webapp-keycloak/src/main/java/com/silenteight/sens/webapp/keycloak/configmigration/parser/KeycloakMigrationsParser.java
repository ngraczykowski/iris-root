package com.silenteight.sens.webapp.keycloak.configmigration.parser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.keycloak.configmigration.Migration;
import com.silenteight.sens.webapp.keycloak.configmigration.PartialMigration;
import com.silenteight.sens.webapp.keycloak.configmigration.migrator.KeycloakHttpBodiesParser;
import com.silenteight.sens.webapp.keycloak.configmigration.migrator.RealmNameExtractor;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import org.keycloak.representations.idm.PartialImportRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.KEYCLOAK_MIGRATION;

@RequiredArgsConstructor
@Slf4j
class KeycloakMigrationsParser implements KeycloakHttpBodiesParser, RealmNameExtractor {

  private final ObjectMapper objectMapper;

  @Override
  public RealmRepresentation parse(Migration migration) {
    log.debug(KEYCLOAK_MIGRATION, "Parsing base migration. migrationName={}", migration.name());
    return parseToRealmRepresentation(migration.json()).get();
  }

  private <T> Try<T> process(String json, Class<T> type) {
    return Try.of(() -> objectMapper.readValue(json, type))
        .recoverWith(throwable -> Try.failure(new CouldNotParseMigrationException(throwable)));
  }

  @Override
  public PartialImportRepresentation parse(PartialMigration partialMigration) {
    log.debug(
        KEYCLOAK_MIGRATION, "Parsing partial migration. migrationName={}", partialMigration.name());
    return process(partialMigration.json(), PartialImportRepresentation.class).get();
  }

  private Try<RealmRepresentation> parseToRealmRepresentation(String json) {
    return process(json, RealmRepresentation.class);
  }

  @Override
  public String extractRealmName(Migration migration) throws CouldNotExtractRealmNameException {
    log.debug(KEYCLOAK_MIGRATION, "Extracting realm name from migration. migrationName={}",
        migration.name());
    return parseToRealmRepresentation(migration.json())
        .map(RealmRepresentation::getRealm)
        .recoverWith(problem -> Try.failure(new CouldNotExtractRealmNameException(problem)))
        .get();
  }
}
