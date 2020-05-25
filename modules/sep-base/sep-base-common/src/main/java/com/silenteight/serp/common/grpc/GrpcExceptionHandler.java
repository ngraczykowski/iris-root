package com.silenteight.serp.common.grpc;

import io.grpc.Metadata;
import io.grpc.Status;

import javax.annotation.Nullable;

public interface GrpcExceptionHandler {

  void handleException(RuntimeException exception, @Nullable Metadata metadata, Callback callback);

  interface Callback {

    void closeWithStatus(Status status, @Nullable Metadata trailers);
  }
}
