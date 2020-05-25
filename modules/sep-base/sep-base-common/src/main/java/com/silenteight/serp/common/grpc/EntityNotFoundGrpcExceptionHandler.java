package com.silenteight.serp.common.grpc;

import io.grpc.Metadata;
import io.grpc.Status;

import javax.annotation.Nullable;
import javax.persistence.EntityNotFoundException;

public class EntityNotFoundGrpcExceptionHandler implements GrpcExceptionHandler {

  @Override
  public void handleException(
      RuntimeException exception, @Nullable Metadata metadata, Callback callback) {

    if (exception instanceof EntityNotFoundException) {
      callback.closeWithStatus(Status.NOT_FOUND.withCause(exception), null);
    }
  }
}
