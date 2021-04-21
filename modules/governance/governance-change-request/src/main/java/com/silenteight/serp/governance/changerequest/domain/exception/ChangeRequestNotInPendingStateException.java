package com.silenteight.serp.governance.changerequest.domain.exception;

import java.util.UUID;

public class ChangeRequestNotInPendingStateException extends RuntimeException {

  private static final long serialVersionUID = -9142082150798169967L;

  public ChangeRequestNotInPendingStateException(UUID changeRequestId) {
    super("Change Request with changeRequestId '" + changeRequestId.toString()
        + "' is not in PENDING state");
  }
}
