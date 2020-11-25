package com.silenteight.serp.governance.app.grpc;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;

class EmptyOrInvalidCorrelationIdException extends StatusRuntimeException {

  private static final long serialVersionUID = 4232934439844034010L;

  EmptyOrInvalidCorrelationIdException() {
    super(Status.NOT_FOUND.withDescription("CorrelationId is empty or invalid"));
  }
}
