package com.silenteight.serp.governance.changerequest.domain.exception;

import lombok.NonNull;

import java.util.UUID;

import static java.lang.String.format;

public class ChangeRequestNotInPendingStateException extends RuntimeException {

  private static final long serialVersionUID = -9142082150798169967L;

  public ChangeRequestNotInPendingStateException(@NonNull UUID changeRequestId) {
    super(format(
        "Change Request with changeRequestId=%s is not in PENDING state.",
        changeRequestId.toString()));
  }
}
