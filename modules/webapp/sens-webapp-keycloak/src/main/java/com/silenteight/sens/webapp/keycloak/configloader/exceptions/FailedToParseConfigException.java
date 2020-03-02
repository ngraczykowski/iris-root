package com.silenteight.sens.webapp.keycloak.configloader.exceptions;

import io.vavr.control.Try;

public final class FailedToParseConfigException extends RuntimeException {

  private static final long serialVersionUID = -6178698603456662129L;

  private FailedToParseConfigException(Throwable cause) {
    super(cause);
  }

  public static <T> Try<T> from(Throwable cause) {
    return Try.failure(new FailedToParseConfigException(cause));
  }
}
