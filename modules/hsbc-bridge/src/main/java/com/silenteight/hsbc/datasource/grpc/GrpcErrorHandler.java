package com.silenteight.hsbc.datasource.grpc;

import com.silenteight.hsbc.bridge.match.MatchNotFoundException;
import com.silenteight.hsbc.datasource.provider.FeatureNotAllowedException;

import io.grpc.Status;

import static io.grpc.Status.INVALID_ARGUMENT;
import static io.grpc.Status.NOT_FOUND;
import static io.grpc.Status.UNKNOWN;

class GrpcErrorHandler {

  Status handle(RuntimeException exception) {
    var status = determineStatus(exception);

    return status
        .withCause(exception)
        .withDescription(exception.getLocalizedMessage());
  }

  private static Status determineStatus(RuntimeException exception) {
    if (exception instanceof FeatureNotAllowedException) {
      return INVALID_ARGUMENT;
    } else if (exception instanceof MatchNotFoundException) {
      return NOT_FOUND;
    }

    return UNKNOWN;
  }
}
