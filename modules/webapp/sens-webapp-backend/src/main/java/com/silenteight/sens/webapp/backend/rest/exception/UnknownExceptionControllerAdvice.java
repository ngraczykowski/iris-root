package com.silenteight.sens.webapp.backend.rest.exception;

import com.silenteight.sens.webapp.backend.rest.exception.dto.ErrorDto;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(ControllerAdviceOrder.UNKNOWN)
public class UnknownExceptionControllerAdvice extends ErrorControllerAdvice {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDto> handle(Exception e) {
    return handle(e, "InternalServerError", HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
