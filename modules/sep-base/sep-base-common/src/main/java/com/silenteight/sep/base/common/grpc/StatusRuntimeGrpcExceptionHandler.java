package com.silenteight.sep.base.common.grpc;

import io.grpc.Metadata;
import io.grpc.StatusRuntimeException;

import javax.annotation.Nullable;

public class StatusRuntimeGrpcExceptionHandler implements GrpcExceptionHandler {

  @Override
  public void handleException(
      RuntimeException exception, @Nullable Metadata metadata, Callback callback) {

    if (exception instanceof StatusRuntimeException) {
      var statusRuntimeException = (StatusRuntimeException) exception;
      callback.closeWithStatus(
          statusRuntimeException.getStatus(),
          statusRuntimeException.getTrailers());
    }
  }
}
