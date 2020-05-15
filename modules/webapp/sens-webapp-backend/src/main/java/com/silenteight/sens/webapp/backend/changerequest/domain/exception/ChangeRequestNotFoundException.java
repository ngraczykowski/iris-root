package com.silenteight.sens.webapp.backend.changerequest.domain.exception;

import java.util.UUID;

public class ChangeRequestNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 3534543480226338998L;

  public ChangeRequestNotFoundException(UUID bulkChangeId) {
    super("Change Request with bulkChangeId '" + bulkChangeId.toString() + "' not found");
  }
}
