package com.silenteight.sens.webapp.backend.changerequest.domain.exception;

import java.io.Serializable;

public class ChangeRequestOperationNotAllowedException extends RuntimeException
    implements Serializable {

  private static final long serialVersionUID = 4355597242383617206L;

  public ChangeRequestOperationNotAllowedException(long changeRequestId) {
    super("This operation is not allowed to be made by maker. "
        + "Change request with id '" + changeRequestId + "' has been ignored.");
  }
}