package com.silenteight.serp.governance.changerequest.domain.exception;

import java.util.UUID;

public class ChangeRequestOperationNotAllowedException extends RuntimeException {

  private static final long serialVersionUID = 4355597242383617206L;

  public ChangeRequestOperationNotAllowedException(UUID changeRequestId) {
    super("This operation is not allowed. "
        + "Change request with id '" + changeRequestId.toString() + "' has been ignored.");
  }
}
