package com.silenteight.serp.governance.common.web.exception;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
@Order(ControllerAdviceOrder.GLOBAL)
public class GenericExceptionControllerAdvice extends AbstractErrorControllerAdvice {

  private static final String ERRORS = "errors";

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorDto> handle(AccessDeniedException e) {
    return handle(e, "AccessDenied", FORBIDDEN);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorDto> handle(MethodArgumentNotValidException e) {
    return handle(
        e, "MethodArgumentNotValid", BAD_REQUEST, Map.of(ERRORS, errorDescriptionsOf(e)));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorDto> handle(HttpMessageNotReadableException e) {
    return handle(e, "HttpMessageNotReadable", BAD_REQUEST, Map.of(ERRORS, e.getMessage()));
  }

  private static Stream<String> errorDescriptionsOf(MethodArgumentNotValidException e) {
    BindingResult bindingResult = e.getBindingResult();
    Stream<String> globalErrors = bindingResult
        .getGlobalErrors()
        .stream()
        .map(ObjectError::getDefaultMessage);

    Stream<String> fieldErrors =
        bindingResult.getFieldErrors()
            .stream()
            .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage());

    return Stream.concat(globalErrors, fieldErrors);
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorDto> handle(MissingServletRequestParameterException e) {
    Map<String, Object> extras = new HashMap<>();
    extras.put("parameterName", e.getParameterName());
    extras.put("parameterType", e.getParameterType());

    return handle(e, "MissingServletRequestParameter", BAD_REQUEST, extras);
  }

  @ExceptionHandler(UnsatisfiedServletRequestParameterException.class)
  public ResponseEntity<ErrorDto> handle(UnsatisfiedServletRequestParameterException e) {
    Map<String, Object> extras = new HashMap<>();
    extras.put("actualParams", e.getActualParams());
    extras.put("paramConditions", e.getParamConditionGroups());

    return handle(e, "UnsatisfiedServletRequestParameter", BAD_REQUEST, extras);
  }

  @ExceptionHandler(MissingPathVariableException.class)
  public ResponseEntity<ErrorDto> handle(MissingPathVariableException e) {
    Map<String, Object> extras = new HashMap<>();
    extras.put("parameter", e.getParameter());
    extras.put("variableName", e.getVariableName());

    return handle(e, "MissingPathVariable", BAD_REQUEST, extras);
  }

  @ExceptionHandler(MissingRequestHeaderException.class)
  public ResponseEntity<ErrorDto> handle(MissingRequestHeaderException e) {
    return handle(
        e, "MissingRequestHeader", BAD_REQUEST, Map.of("headerName", e.getHeaderName()));
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorDto> handle(MethodArgumentTypeMismatchException e) {
    Map<String, Object> extras = new HashMap<>();
    extras.put("parameterName", e.getName());
    extras.put("expectedParameterType", e.getParameter().getParameterType());

    return handle(e, "MethodArgumentTypeMismatch", BAD_REQUEST, extras);
  }

  @ExceptionHandler(ServletRequestBindingException.class)
  public ResponseEntity<ErrorDto> handle(ServletRequestBindingException e) {
    return handle(e, "ServletRequestBinding", BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorDto> handle(ConstraintViolationException e) {
    return handle(e, "ConstraintViolation", BAD_REQUEST, Map.of(ERRORS, e.getMessage()));
  }

  @ExceptionHandler({
      EntityNotFoundException.class,
      com.silenteight.sep.base.common.exception.EntityNotFoundException.class
  })
  public ResponseEntity<ErrorDto> handle(Exception e) {
    return handle(e, "EntityNotFound", NOT_FOUND);
  }

  @ExceptionHandler(BindException.class)
  public ResponseEntity<ErrorDto> handle(BindException e) {
    return handle(e, "Bind", BAD_REQUEST, Map.of(ERRORS, e.getMessage()));
  }
}
