package com.silenteight.sens.webapp.keycloak.configmigration;

public interface KeycloakMigrator {

  void migrate(KeycloakMigrations migrations);

  class CouldNotPerformMigrationException extends RuntimeException {

    private static final long serialVersionUID = -4227927561096265460L;

    public CouldNotPerformMigrationException(Throwable cause) {
      super(cause);
    }

    public CouldNotPerformMigrationException(String message) {
      super(message);
    }
  }
}
