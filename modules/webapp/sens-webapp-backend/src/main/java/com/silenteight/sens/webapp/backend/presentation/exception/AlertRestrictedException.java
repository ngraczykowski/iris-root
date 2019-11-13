package com.silenteight.sens.webapp.backend.presentation.exception;

public class AlertRestrictedException extends RuntimeException {

  private static final long serialVersionUID = -1572883379901011863L;

  public AlertRestrictedException(String externalId, long userId) {
    super(String.format("User %s doesn't have access to alert %s", userId, externalId));
  }
}
