package com.silenteight.serp.governance.common.web.exception;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
@Order(ControllerAdviceOrder.UNKNOWN)
public class UnknownExceptionControllerAdvice extends AbstractErrorControllerAdvice {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDto> handle(Exception e) {
    return handle(e, "InternalServerError", INTERNAL_SERVER_ERROR);
  }
}
