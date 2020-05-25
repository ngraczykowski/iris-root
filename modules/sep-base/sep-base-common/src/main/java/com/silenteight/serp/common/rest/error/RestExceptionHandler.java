package com.silenteight.serp.common.rest.error;

import io.grpc.StatusRuntimeException;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;

import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

@RestControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler(StatusRuntimeException.class)
  public ResponseEntity<Map<String, Object>> handleStatusRuntimeException(
      StatusRuntimeException exception, WebRequest request) {

    HttpStatus httpStatus = new GrpcHttpStatusSupplier().apply(exception);
    return new ResponseEntity<>(getErrorBody(httpStatus, request), httpStatus);
  }

  @ExceptionHandler({
      MissingPathVariableException.class,
      MultipartException.class,
      UnsatisfiedServletRequestParameterException.class,
  })
  @ResponseStatus(BAD_REQUEST)
  public ResponseEntity<Map<String, Object>> handleBadRequestExceptions(WebRequest request) {
    return new ResponseEntity<>(getErrorBody(BAD_REQUEST, request), BAD_REQUEST);
  }

  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  @ResponseStatus(NOT_ACCEPTABLE)
  public ResponseEntity<Map<String, Object>> handleHttpMediaTypeNotSupportedException(
      WebRequest request) {

    return new ResponseEntity<>(getErrorBody(NOT_ACCEPTABLE, request), NOT_ACCEPTABLE);
  }

  private static Map<String, Object> getErrorBody(HttpStatus httpStatus, WebRequest request) {
    Map<String, Object> errorAttributes = getErrorAttributes(request);
    addStatus(errorAttributes, httpStatus);
    return errorAttributes;
  }

  private static void addStatus(Map<String, Object> errorAttributes, HttpStatus httpStatus) {
    errorAttributes.put("status", httpStatus.value());
    errorAttributes.put("error", httpStatus.getReasonPhrase());
  }

  private static Map<String, Object> getErrorAttributes(WebRequest request) {
    DefaultErrorAttributes errorAttributes = new DefaultErrorAttributes(true);
    return errorAttributes.getErrorAttributes(request, false);
  }
}
