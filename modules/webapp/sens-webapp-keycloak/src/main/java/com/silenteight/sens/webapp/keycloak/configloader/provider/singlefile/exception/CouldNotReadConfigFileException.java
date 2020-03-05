package com.silenteight.sens.webapp.keycloak.configloader.provider.singlefile.exception;

import io.vavr.control.Try;

public class CouldNotReadConfigFileException extends RuntimeException {

  private static final long serialVersionUID = 5924263306397707771L;

  private CouldNotReadConfigFileException(Throwable cause) {
    super(cause);
  }

  public static <T> Try<T> from(Throwable cause) {
    return Try.failure(new CouldNotReadConfigFileException(cause));
  }
}
