package com.silenteight.sens.webapp.keycloak.configmigration.migrator;

import com.silenteight.sens.webapp.audit.api.AuditLog;
import com.silenteight.sens.webapp.keycloak.configmigration.KeycloakMigrations;
import com.silenteight.sens.webapp.keycloak.configmigration.KeycloakMigrator;
import com.silenteight.sens.webapp.keycloak.configmigration.KeycloakMigrator.CouldNotPerformMigrationException;
import com.silenteight.sens.webapp.keycloak.configmigration.Migration;
import com.silenteight.sens.webapp.keycloak.configmigration.PartialMigration;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.representations.idm.PartialImportRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.sens.webapp.keycloak.configmigration.MigrationPolicy.ON_EVERY_RUN;
import static com.silenteight.sens.webapp.keycloak.configmigration.MigrationPolicy.ON_FIRST_RUN;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeycloakHttpMigratorTest {

  @Mock
  private KeycloakRealmMigrationApi keycloak;
  @Mock
  private KeycloakHttpBodiesParser keycloakHttpBodiesParser;
  @Mock
  private RealmNameExtractor realmNameExtractor;
  @Mock
  private AuditLog auditLog;

  static final String REALM_NAME = "sens-webapp";

  private KeycloakMigrator underTest;

  @BeforeEach
  void setUp() {
    underTest = new KeycloakHttpMigratorConfiguration()
        .keycloakMigrator(keycloakHttpBodiesParser, realmNameExtractor, keycloak, auditLog);
  }

  @Nested
  class RealmDoesntExist {

    @BeforeEach
    void setUp() {
      realmDoesntExist();
    }

    @Test
    void onlyBaseMigration_createsRealm() {
      RealmRepresentation realmRepresentation = mock(RealmRepresentation.class);
      Migration baseMigration = new Migration("migrationName", "data");
      KeycloakMigrations migrations = new KeycloakMigrations(baseMigration, emptyList());
      given(realmNameExtractor.extractRealmName(baseMigration)).willReturn(REALM_NAME);
      given(keycloakHttpBodiesParser.parse(baseMigration)).willReturn(realmRepresentation);

      underTest.migrate(migrations);

      then(keycloak).should().createRealm(realmRepresentation);
    }

    @Test
    void onlyBaseMigration_cantCreateRealm_triesToCreateAndThrows() {
      cantCreateRealm();
      Migration baseMigration = new Migration("migrationName", "data");
      KeycloakMigrations migrations = new KeycloakMigrations(baseMigration, emptyList());
      RealmRepresentation realmRepresentation = mock(RealmRepresentation.class);

      given(realmNameExtractor.extractRealmName(baseMigration)).willReturn(REALM_NAME);
      given(keycloakHttpBodiesParser.parse(baseMigration)).willReturn(realmRepresentation);

      ThrowingCallable when = () -> underTest.migrate(migrations);

      assertThatThrownBy(when).isInstanceOf(CouldNotPerformMigrationException.class);
      then(keycloak).should().createRealm(realmRepresentation);
    }
  }

  @Nested
  class RealmExists {

    @BeforeEach
    void setUp() {
      realmExists();
    }

    private void realmExists() {
      given(keycloak.realmExists(REALM_NAME)).willReturn(true);
    }

    @Test
    void canImport_baseAndThreePartialMigrations_importsOnlyPartial() {
      Migration baseMigration = mockBaseMigration();

      // First ON_EVERY_RUN partial migration
      PartialMigration migration1 = new PartialMigration("migration1", "", ON_EVERY_RUN, 1L);
      PartialImportRepresentation partialImport1 = mockPartialImport(migration1);

      // Second ON_FIRST_RUN partial migration
      PartialMigration migration2 = new PartialMigration("migration2", "", ON_FIRST_RUN, 2L);

      // Third ON_EVERY_RUN partial migration
      PartialMigration migration3 = new PartialMigration("migration3", "", ON_EVERY_RUN, 1L);
      PartialImportRepresentation partialImport3 = mockPartialImport(migration3);

      RealmRepresentation realmRepresentation = mock(RealmRepresentation.class);
      KeycloakMigrations migrations = new KeycloakMigrations(
          baseMigration, List.of(migration1, migration2, migration3));

      underTest.migrate(migrations);

      then(keycloak).should(never()).createRealm(realmRepresentation);
      InOrder inOrder = inOrder(keycloak);
      then(keycloak).should(inOrder).partialImport(REALM_NAME, partialImport1);
      then(keycloak).should(inOrder).partialImport(REALM_NAME, partialImport3);
    }
  }

  private Migration mockBaseMigration() {
    Migration baseMigration = new Migration("baseMigration", "");
    given(realmNameExtractor.extractRealmName(baseMigration)).willReturn(REALM_NAME);
    return baseMigration;
  }

  private PartialImportRepresentation mockPartialImport(PartialMigration migration) {
    PartialImportRepresentation partialImport = mock(PartialImportRepresentation.class);
    given(keycloakHttpBodiesParser.parse(migration)).willReturn(partialImport);
    return partialImport;
  }

  private void cantCreateRealm() {
    willThrow(RuntimeException.class).given(keycloak).createRealm(any());
  }

  private void realmDoesntExist() {
    willReturn(false).given(keycloak).realmExists(REALM_NAME);
  }
}
