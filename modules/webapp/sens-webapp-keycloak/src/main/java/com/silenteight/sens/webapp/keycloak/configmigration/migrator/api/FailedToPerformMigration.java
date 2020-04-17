package com.silenteight.sens.webapp.keycloak.configmigration.migrator.api;

import io.vavr.control.Try;

import javax.ws.rs.core.Response;

final class FailedToPerformMigration extends RuntimeException {

  private static final long serialVersionUID = -6564291696060401788L;

  FailedToPerformMigration(Response response) {
    super("Keycloak HTTP response is " + response.getStatusInfo());
  }

  FailedToPerformMigration(Throwable cause) {
    super(cause);
  }

  public static <T> Try<T> from(Throwable cause) {
    return Try.failure(new FailedToPerformMigration(cause));
  }
}
