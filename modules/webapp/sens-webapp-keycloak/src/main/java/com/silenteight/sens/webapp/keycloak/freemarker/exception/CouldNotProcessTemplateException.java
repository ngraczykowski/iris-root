package com.silenteight.sens.webapp.keycloak.freemarker.exception;

import io.vavr.control.Try;

public class CouldNotProcessTemplateException extends RuntimeException {

  private static final long serialVersionUID = -3222953684584150098L;

  private CouldNotProcessTemplateException(Throwable cause) {
    super(cause);
  }

  public static <T> Try<T> from(Throwable cause) {
    return Try.failure(new CouldNotProcessTemplateException(cause));
  }
}
