package com.silenteight.sens.webapp.keycloak.configloader.exceptions;

import io.vavr.control.Try;

public final class FailedToCreateRealmException extends RuntimeException {

  private static final long serialVersionUID = -1432876815234794826L;

  private FailedToCreateRealmException(Throwable cause) {
    super(cause);
  }

  public static <T> Try<T> from(Throwable cause) {
    return Try.failure(new FailedToCreateRealmException(cause));
  }
}
