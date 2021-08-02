package com.silenteight.payments.bridge.firco.service;

import com.silenteight.payments.bridge.firco.dto.common.AckDto;

import org.springframework.http.ResponseEntity;

//@ControllerAdvice
public class DefaultRestErrorHandler {

  //@ExceptionHandler(Exception.class)
  public ResponseEntity<AckDto> handleError() {
    return ResponseEntity.internalServerError().build();
  }
}
