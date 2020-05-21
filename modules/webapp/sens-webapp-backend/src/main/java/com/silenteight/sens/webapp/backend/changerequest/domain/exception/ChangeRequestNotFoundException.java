package com.silenteight.sens.webapp.backend.changerequest.domain.exception;

public class ChangeRequestNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 3534543480226338998L;

  public ChangeRequestNotFoundException(long changeRequestId) {
    super("Change Request with id '" + changeRequestId + "' not found");
  }
}
