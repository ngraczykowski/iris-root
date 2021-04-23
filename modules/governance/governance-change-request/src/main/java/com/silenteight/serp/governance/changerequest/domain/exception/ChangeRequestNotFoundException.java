package com.silenteight.serp.governance.changerequest.domain.exception;

import lombok.NonNull;

import java.util.UUID;

import static java.lang.String.format;

public class ChangeRequestNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 3534543480226338998L;

  public ChangeRequestNotFoundException(@NonNull UUID changeRequestId) {
    super(format("Change Request with changeRequestId=%s not found.", changeRequestId.toString()));
  }
}
