package com.silenteight.simulator.dataset.get;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.simulator.dataset.domain.DatasetNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@ControllerAdvice
class GetDatasetRestControllerAdvice {

  @ExceptionHandler(DatasetNotFoundException.class)
  public ResponseEntity<String> handle(DatasetNotFoundException e) {
    log(e);
    return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
  }

  private static void log(Exception e) {
    log.error("Error occurred", e);
  }
}
