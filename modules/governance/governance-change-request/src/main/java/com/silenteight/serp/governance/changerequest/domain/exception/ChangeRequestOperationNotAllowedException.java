package com.silenteight.serp.governance.changerequest.domain.exception;

import lombok.NonNull;

import java.util.UUID;

import static java.lang.String.format;

public class ChangeRequestOperationNotAllowedException extends RuntimeException {

  private static final long serialVersionUID = 4355597242383617206L;

  public ChangeRequestOperationNotAllowedException(@NonNull UUID changeRequestId) {
    super(format(
        "This operation is not allowed. Change Request with changeRequestId=%s has been ignored.",
        changeRequestId.toString()));
  }
}
