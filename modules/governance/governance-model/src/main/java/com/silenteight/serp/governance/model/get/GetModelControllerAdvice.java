package com.silenteight.serp.governance.model.get;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.model.domain.exception.ModelNotFoundException;
import com.silenteight.serp.governance.model.domain.exception.TooManyModelsException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@ControllerAdvice
class GetModelControllerAdvice {

  @ExceptionHandler(ModelNotFoundException.class)
  public ResponseEntity<String> handle(ModelNotFoundException e) {
    log(e);
    return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
  }

  @ExceptionHandler(TooManyModelsException.class)
  public ResponseEntity<String> handle(TooManyModelsException e) {
    log(e);
    return new ResponseEntity<>(e.getMessage(), INTERNAL_SERVER_ERROR);
  }

  private static void log(Exception e) {
    log.error("Error occurred", e);
  }
}
