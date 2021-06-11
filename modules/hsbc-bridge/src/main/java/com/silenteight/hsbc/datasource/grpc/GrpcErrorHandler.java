package com.silenteight.hsbc.datasource.grpc;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.json.JsonConversionException;
import com.silenteight.hsbc.bridge.match.MatchDataNoLongerAvailableException;
import com.silenteight.hsbc.bridge.match.MatchNotFoundException;
import com.silenteight.hsbc.datasource.provider.FeatureNotAllowedException;

import io.grpc.Status;

import static io.grpc.Status.DATA_LOSS;
import static io.grpc.Status.INVALID_ARGUMENT;
import static io.grpc.Status.NOT_FOUND;
import static io.grpc.Status.UNKNOWN;

@Slf4j
class GrpcErrorHandler {

  Status handle(RuntimeException exception) {
    log.error("Grpc error", exception);

    var status = determineStatus(exception);

    return status
        .withCause(exception)
        .withDescription(exception.getLocalizedMessage());
  }

  private static Status determineStatus(RuntimeException ex) {
    if (ex instanceof FeatureNotAllowedException) {
      return INVALID_ARGUMENT;
    } else if (ex instanceof MatchNotFoundException) {
      return NOT_FOUND;
    } else if (ex instanceof MatchDataNoLongerAvailableException
        || ex instanceof JsonConversionException) {
      return DATA_LOSS;
    }

    return UNKNOWN;
  }
}
