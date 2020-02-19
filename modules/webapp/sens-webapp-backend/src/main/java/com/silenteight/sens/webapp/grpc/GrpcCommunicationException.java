package com.silenteight.sens.webapp.grpc;

import lombok.NonNull;

import com.google.rpc.Code;
import com.google.rpc.Status;
import io.grpc.StatusException;
import io.grpc.StatusRuntimeException;
import io.vavr.control.Try;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Predicate;

import static io.grpc.protobuf.StatusProto.fromThrowable;
import static io.vavr.control.Try.failure;

public class GrpcCommunicationException extends RuntimeException {

  private static final long serialVersionUID = 1775536480348716595L;

  private final Status status;

  private GrpcCommunicationException(Status status) {
    super(status.getMessage());
    this.status = status;
  }

  public static <T> Try<T> mapStatusExceptionsToCommunicationException(Try<T> vavrTry) {
    return vavrTry
        .recoverWith(StatusRuntimeException.class, e -> failure(GrpcCommunicationException.from(e)))
        .recoverWith(StatusException.class, e -> failure(GrpcCommunicationException.from(e)));
  }

  public static GrpcCommunicationException from(@NonNull StatusException exception) {
    return new GrpcCommunicationException(getStatus(exception));
  }

  public static GrpcCommunicationException from(@NonNull StatusRuntimeException exception) {
    return new GrpcCommunicationException(getStatus(exception));
  }

  @NotNull
  private static Status getStatus(@NonNull Throwable exception) {
    Optional<Status> status = Optional.ofNullable(fromThrowable(exception));

    return status.orElse(statusWithCodeOnly(exception));
  }

  @NotNull
  private static Status statusWithCodeOnly(@NonNull Throwable exception) {
    return Status.newBuilder().setCode(
        io.grpc.Status.fromThrowable(exception).getCode().value()
    ).build();
  }

  public static Predicate<GrpcCommunicationException> codeIs(Code code) {
    return exception -> exception.is(code);
  }

  boolean is(Code code) {
    return getCode() == code;
  }

  public Code getCode() {
    return Code.forNumber(status.getCode());
  }
}
