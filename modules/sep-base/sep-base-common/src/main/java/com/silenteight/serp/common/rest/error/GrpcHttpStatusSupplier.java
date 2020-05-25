package com.silenteight.serp.common.rest.error;

import io.grpc.Status;
import io.grpc.Status.Code;
import io.grpc.StatusRuntimeException;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

class GrpcHttpStatusSupplier {

  HttpStatus apply(Throwable throwable) {
    StatusRuntimeException error = (StatusRuntimeException) throwable;
    Status errorStatus = error.getStatus();

    return toHttpStatus(errorStatus.getCode());
  }

  @SuppressWarnings("PMD.CyclomaticComplexity")
  private static HttpStatus toHttpStatus(Code errorCode) {
    switch (errorCode) {
      case OK:
        return HttpStatus.OK;
      case CANCELLED:
        return REQUEST_TIMEOUT;
      case INVALID_ARGUMENT:
      case OUT_OF_RANGE:
      case FAILED_PRECONDITION:
        return BAD_REQUEST;
      case DEADLINE_EXCEEDED:
        return GATEWAY_TIMEOUT;
      case NOT_FOUND:
        return HttpStatus.NOT_FOUND;
      case ALREADY_EXISTS:
      case ABORTED:
        return CONFLICT;
      case PERMISSION_DENIED:
        return FORBIDDEN;
      case RESOURCE_EXHAUSTED:
        return TOO_MANY_REQUESTS;
      case UNIMPLEMENTED:
        return NOT_IMPLEMENTED;
      case UNAVAILABLE:
        return SERVICE_UNAVAILABLE;
      case UNAUTHENTICATED:
        return UNAUTHORIZED;
      default:
        return INTERNAL_SERVER_ERROR;
    }
  }
}
