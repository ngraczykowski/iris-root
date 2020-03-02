package com.silenteight.sens.webapp.keycloak.configloader.exceptions;

import io.vavr.control.Try;

public final class FailedToFindRealmException extends RuntimeException {

  private static final long serialVersionUID = -4191581098632966390L;

  private FailedToFindRealmException(Throwable cause) {
    super(cause);
  }

  public static <T> Try<T> from(Throwable cause) {
    return Try.failure(new FailedToFindRealmException(cause));
  }
}
