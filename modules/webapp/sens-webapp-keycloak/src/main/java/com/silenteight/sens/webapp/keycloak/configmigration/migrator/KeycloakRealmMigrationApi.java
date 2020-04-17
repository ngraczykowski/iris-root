package com.silenteight.sens.webapp.keycloak.configmigration.migrator;

import org.keycloak.representations.idm.PartialImportRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;

public interface KeycloakRealmMigrationApi {

  void partialImport(String realmName, PartialImportRepresentation partialImportRepresentation);

  boolean realmExists(String realmName);

  void createRealm(RealmRepresentation realmRepresentation);
}
