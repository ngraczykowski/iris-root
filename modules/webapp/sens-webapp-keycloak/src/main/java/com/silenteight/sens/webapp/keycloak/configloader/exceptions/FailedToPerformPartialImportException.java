package com.silenteight.sens.webapp.keycloak.configloader.exceptions;

import io.vavr.control.Try;

public final class FailedToPerformPartialImportException extends RuntimeException {

  private static final long serialVersionUID = -6564291696060401788L;

  private FailedToPerformPartialImportException(Throwable cause) {
    super(cause);
  }

  public static <T> Try<T> from(Throwable cause) {
    return Try.failure(new FailedToPerformPartialImportException(cause));
  }
}
