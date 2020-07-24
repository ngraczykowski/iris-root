package com.silenteight.sens.webapp.keycloak.configmigration;

public interface KeycloakMigrationsLoader {

  KeycloakMigrations load();

  class CouldNotLoadMigrationsException extends RuntimeException {

    public CouldNotLoadMigrationsException(Throwable cause) {
      super(cause);
    }

    private static final long serialVersionUID = -7174450674267882778L;

    public CouldNotLoadMigrationsException(String message) {
      super(message);
    }
  }
}
