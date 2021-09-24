package com.silenteight.universaldatasource.app.exceptionhandling;

import com.silenteight.universaldatasource.app.category.service.CategoryException;
import com.silenteight.universaldatasource.app.category.service.CategoryValueException;
import com.silenteight.universaldatasource.app.feature.service.MatchFeatureInputMappingException;

import io.grpc.Status;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import javax.validation.ConstraintViolationException;

@GrpcAdvice
public class GrpcExceptionAdvice {

  @GrpcExceptionHandler(CategoryValueException.class)
  public Status handleCategoryValueException(CategoryValueException e) {
    return getStatusWithDescription(Status.INVALID_ARGUMENT, e);
  }

  @GrpcExceptionHandler(CategoryException.class)
  public Status handleCategoryException(CategoryException e) {
    return getStatusWithDescription(Status.INVALID_ARGUMENT, e);
  }

  @GrpcExceptionHandler(MatchFeatureInputMappingException.class)
  public Status handleMatchFeatureInputMappingException(MatchFeatureInputMappingException e) {
    return getStatusWithDescription(Status.INVALID_ARGUMENT, e);
  }

  @GrpcExceptionHandler(NoSuchElementException.class)
  public Status handleNoSuchElementExceptionException(NoSuchElementException e) {
    return getStatusWithDescription(Status.NOT_FOUND, e);
  }

  @GrpcExceptionHandler(ConstraintViolationException.class)
  public Status handleConstraintViolationException(ConstraintViolationException e) {
    return getStatusWithDescription(Status.INVALID_ARGUMENT, e);
  }

  @NotNull
  private Status getStatusWithDescription(Status status, Exception e) {
    return status.withDescription(e.getMessage()).withCause(e);
  }
}
