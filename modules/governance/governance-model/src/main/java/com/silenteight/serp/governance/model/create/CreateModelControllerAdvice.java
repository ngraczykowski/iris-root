package com.silenteight.serp.governance.model.create;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.model.domain.exception.ModelAlreadyExistsException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.CONFLICT;

@Slf4j
@ControllerAdvice
class CreateModelControllerAdvice {

  @ExceptionHandler(ModelAlreadyExistsException.class)
  public ResponseEntity<String> handle(ModelAlreadyExistsException e) {
    log(e);
    return new ResponseEntity<>(e.getMessage(), CONFLICT);
  }

  private static void log(Exception e) {
    log.error("Error occurred", e);
  }
}
