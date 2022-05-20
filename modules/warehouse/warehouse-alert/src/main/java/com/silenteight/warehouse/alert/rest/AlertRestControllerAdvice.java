package com.silenteight.warehouse.alert.rest;

import com.silenteight.warehouse.alert.rest.service.AlertNotFoundException;
import com.silenteight.warehouse.common.web.exception.AbstractErrorControllerAdvice;
import com.silenteight.warehouse.common.web.exception.ErrorDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice(basePackageClasses = AlertRestController.class)
class AlertRestControllerAdvice extends AbstractErrorControllerAdvice {

  @ExceptionHandler(AlertNotFoundException.class)
  public ResponseEntity<ErrorDto> handle(AlertNotFoundException e) {
    return handle(e, "AlertNotFound", NOT_FOUND);
  }
}
