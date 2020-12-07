package com.silenteight.sens.webapp.backend.changerequest.domain.exception;

import java.io.Serializable;

public class ChangeRequestNotAllowedException extends RuntimeException implements Serializable {

  private static final long serialVersionUID = 4355597242383617206L;

   public ChangeRequestNotAllowedException(long changeRequestId) {
    super("Change Request cannot be approved by maker. "
        + "Change request with id '" + changeRequestId + "' has been ignored.");
  }
}