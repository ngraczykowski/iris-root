package com.silenteight.adjudication.engine.common.grpc;

import com.silenteight.sep.base.common.grpc.GrpcExceptionHandler;

import com.google.protobuf.Message;
import com.google.rpc.BadRequest;
import io.grpc.Metadata;
import io.grpc.Metadata.Key;
import io.grpc.Status;
import io.grpc.protobuf.lite.ProtoLiteUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;

import static java.util.stream.Collectors.joining;

public class ValidationGrpcExceptionHandler implements GrpcExceptionHandler {

  @Override
  public void handleException(
      RuntimeException exception, @Nullable Metadata metadata, Callback callback) {

    if (exception instanceof ConstraintViolationException) {
      var cve = (ConstraintViolationException) exception;
      var badRequest = makeBadRequestInfo(cve);
      var trailers = enrichErrorDetails(metadata, badRequest);
      var status = Status.INVALID_ARGUMENT
          .withDescription(makeDescription(cve))
          .withCause(exception);

      callback.closeWithStatus(status, trailers);
    }
  }

  @Nonnull
  private static String makeDescription(ConstraintViolationException exception) {
    return exception.getConstraintViolations().stream()
        .filter(Objects::nonNull)
        .map(cv -> sanitizePropertyPath(cv.getPropertyPath()) + ": " + cv.getMessage())
        .collect(joining(", "));
  }

  @Nonnull
  private static Metadata enrichErrorDetails(@Nullable Metadata metadata, BadRequest badRequest) {
    Metadata.Key<BadRequest> key = makeKey(badRequest.getDefaultInstanceForType());

    var trailers = metadata != null ? metadata : new Metadata();
    trailers.put(key, badRequest);

    return trailers;
  }

  @Nonnull
  static <T extends Message> Key<T> makeKey(T defaultInstance) {
    return Key.of(
        defaultInstance.getDescriptorForType().getFullName() + Metadata.BINARY_HEADER_SUFFIX,
        ProtoLiteUtils.metadataMarshaller(defaultInstance));
  }

  @Nonnull
  private static BadRequest makeBadRequestInfo(ConstraintViolationException exception) {
    var builder = BadRequest.newBuilder();

    exception.getConstraintViolations().stream().filter(Objects::nonNull).forEach(cv -> builder
        .addFieldViolationsBuilder()
        .setField(sanitizePropertyPath(cv.getPropertyPath()))
        .setDescription(cv.getMessage()));

    return builder.build();
  }

  @Nonnull
  private static String sanitizePropertyPath(Path propertyPath) {
    // NOTE(ahaczewski): Removes underscores because the fields in the code generated for
    // Protocol Buffer messages are suffixed with `_`, e.g., for `alert_matches` field in
    // message `BatchCreateMatchesRequest`, the field in class is called `alertMatches_`.
    // To make the error messages more user-friendly, we're removing the unnecessary underscore.
    return StringUtils.replace(propertyPath.toString(), "_", "");
  }
}
