package com.silenteight.sens.webapp.backend.changerequest.domain.exception;

import java.util.UUID;

public class ChangeRequestNotInPendingStateException extends RuntimeException {

  private static final long serialVersionUID = -9142082150798169967L;

  public ChangeRequestNotInPendingStateException(UUID bulkChangeId) {
    super("Change Request with bulkChangeId '" + bulkChangeId.toString()
        + "' is not in PENDING state");
  }
}
