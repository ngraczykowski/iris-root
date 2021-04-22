package com.silenteight.serp.governance.model.get;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.model.domain.exception.ModelNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@ControllerAdvice
class GetModelControllerAdvice {

  @ExceptionHandler(ModelNotFoundException.class)
  public ResponseEntity<String> handle(ModelNotFoundException e) {
    log(e);
    return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
  }

  private static void log(Exception e) {
    log.error("Error occurred", e);
  }
}
