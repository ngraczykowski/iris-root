package com.silenteight.adjudication.engine.common.grpc;

import lombok.extern.slf4j.Slf4j;

import io.grpc.Status;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@Slf4j
@GrpcAdvice
public class GrpcExceptionAdvice {

  @GrpcExceptionHandler(InvalidAnalysisException.class)
  public Status handleCategoryValueException(InvalidAnalysisException e) {
    return getStatusWithDescription(Status.INVALID_ARGUMENT, e);
  }

  private static Status getStatusWithDescription(Status status, Exception e) {
    log.error(status.getDescription(), e);
    return status.withDescription(e.getMessage()).withCause(e);
  }
}
