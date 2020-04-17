package com.silenteight.sens.webapp.keycloak.configmigration.migrator;

import com.silenteight.sens.webapp.keycloak.configmigration.Migration;
import com.silenteight.sens.webapp.keycloak.configmigration.PartialMigration;

import org.keycloak.representations.idm.PartialImportRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;

public interface KeycloakHttpBodiesParser {

  RealmRepresentation parse(Migration baseMigration) throws CouldNotParseMigrationException;

  PartialImportRepresentation parse(PartialMigration partialMigration) throws
      CouldNotParseMigrationException;

  class CouldNotParseMigrationException extends RuntimeException {

    private static final long serialVersionUID = -7637456223603413508L;

    public CouldNotParseMigrationException(Throwable cause) {
      super(cause);
    }
  }
}
