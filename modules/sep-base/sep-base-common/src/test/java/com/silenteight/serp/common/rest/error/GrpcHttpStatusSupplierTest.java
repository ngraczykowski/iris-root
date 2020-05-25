package com.silenteight.serp.common.rest.error;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.grpc.Status.FAILED_PRECONDITION;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

class GrpcHttpStatusSupplierTest {

  private final GrpcHttpStatusSupplier errorSupplier = new GrpcHttpStatusSupplier();

  private final Fixtures fixtures = new Fixtures();

  @Test
  void mapGrpcExceptionToHttpStatus() {
    // when
    HttpStatus httpStatus = errorSupplier.apply(fixtures.failsPreconditionGrpcException);

    // then
    assertThat(httpStatus).isEqualTo(fixtures.badRequest);
  }

  private static class Fixtures {

    HttpStatus badRequest = BAD_REQUEST;

    Status status = FAILED_PRECONDITION.withDescription(badRequest.getReasonPhrase());
    Throwable failsPreconditionGrpcException = new StatusRuntimeException(status);
  }
}
