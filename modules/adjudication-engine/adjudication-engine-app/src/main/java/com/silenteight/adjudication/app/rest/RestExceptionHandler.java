package com.silenteight.adjudication.app.rest;

import org.springframework.beans.TypeMismatchException;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;

@Order
@RestControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler({
      MissingPathVariableException.class,
      UnsatisfiedServletRequestParameterException.class,
      TypeMismatchException.class,
      ValidationException.class
  })
  public ResponseEntity<Void> handleBadRequestExceptions() {
    return ResponseEntity.badRequest().build();
  }
}
