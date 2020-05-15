package com.silenteight.sens.webapp.backend.changerequest.domain.exception;

import java.util.UUID;

public class ChangeRequestNotInPendingStateException extends RuntimeException {

  public ChangeRequestNotInPendingStateException(UUID bulkChangeId) {
    super("Change Request with bulkChangeId '" + bulkChangeId.toString()
        + "' is not in PENDING state");
  }
}
