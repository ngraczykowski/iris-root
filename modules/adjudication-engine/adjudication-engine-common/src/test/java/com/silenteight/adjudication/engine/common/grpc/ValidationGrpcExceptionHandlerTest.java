package com.silenteight.adjudication.engine.common.grpc;

import com.silenteight.sep.base.common.grpc.GrpcExceptionHandler.Callback;

import com.google.rpc.BadRequest;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.Status.Code;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import static com.silenteight.adjudication.engine.common.grpc.ValidationGrpcExceptionHandler.makeKey;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidationGrpcExceptionHandlerTest {

  private static final String TEST_PATH = "test.path";
  private static final String TEST_MESSAGE = "test message";

  private final ValidationGrpcExceptionHandler handler = new ValidationGrpcExceptionHandler();

  @Mock
  private Callback callback;
  @Captor
  private ArgumentCaptor<Status> statusCaptor;
  @Captor
  private ArgumentCaptor<Metadata> metadataCaptor;

  private RuntimeException exception;

  @Test
  void doesNothingWhenNotConstraintValidationException() {
    handler.handleException(new ArithmeticException(), null, callback);
    verifyNoInteractions(callback);
  }

  @Test
  void closesConnectionOnViolationWithInvalidArgument() {
    givenViolations(DummyConstraintViolation.make(TEST_PATH, TEST_MESSAGE));

    whenExceptionHandled();

    verify(callback).closeWithStatus(statusCaptor.capture(), metadataCaptor.capture());

    assertThat(statusCaptor.getValue()).satisfies(s -> {
      assertThat(s.getCode()).isEqualTo(Code.INVALID_ARGUMENT);
      assertThat(s.getCause()).isEqualTo(exception);
      assertThat(s.getDescription()).contains(TEST_PATH).contains(TEST_MESSAGE);
    });

    assertThat(metadataCaptor.getValue()).satisfies(m -> {
      var key = makeKey(BadRequest.getDefaultInstance());
      assertThat(m.containsKey(key)).isTrue();

      var badRequest = m.get(key);
      assertThat(badRequest).isNotNull();
      assertThat(badRequest.getFieldViolationsCount()).isEqualTo(1);

      var fieldViolation = badRequest.getFieldViolations(0);
      assertThat(fieldViolation.getField()).contains(TEST_PATH);
      assertThat(fieldViolation.getDescription()).contains(TEST_MESSAGE);
    });
  }

  private void givenViolations(ConstraintViolation<?>... violations) {
    assert (violations.length > 0);

    exception = new ConstraintViolationException(new HashSet<>(Arrays.asList(violations.clone())));
  }

  private void whenExceptionHandled() {
    handler.handleException(exception, null, callback);
  }
}
