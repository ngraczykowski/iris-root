package com.silenteight.sens.webapp.keycloak.freemarker.exception;

import io.vavr.control.Try;

public class CouldNotLoadTemplateException extends RuntimeException {

  private static final long serialVersionUID = 9097033929843346364L;

  private CouldNotLoadTemplateException(Throwable cause) {
    super(cause);
  }

  public static <T> Try<T> from(Throwable cause) {
    return Try.failure(new CouldNotLoadTemplateException(cause));
  }
}
