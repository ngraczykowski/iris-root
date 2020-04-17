package com.silenteight.sens.webapp.keycloak.configmigration.migrator;

import com.silenteight.sens.webapp.keycloak.configmigration.Migration;

public interface RealmNameExtractor {

  String extractRealmName(Migration migration) throws CouldNotExtractRealmNameException;

  class CouldNotExtractRealmNameException extends RuntimeException {

    private static final long serialVersionUID = -2963406129341786251L;

    public CouldNotExtractRealmNameException(Throwable cause) {
      super(cause);
    }
  }
}
