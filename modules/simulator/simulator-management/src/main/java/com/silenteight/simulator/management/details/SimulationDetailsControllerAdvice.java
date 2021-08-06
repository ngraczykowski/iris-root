package com.silenteight.simulator.management.details;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.simulator.management.domain.exception.SimulationNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@ControllerAdvice
class SimulationDetailsControllerAdvice {

  @ExceptionHandler(SimulationNotFoundException.class)
  public ResponseEntity<String> handle(SimulationNotFoundException e) {
    log(e);
    return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
  }

  private static void log(Exception e) {
    log.error("Error occurred", e);
  }
}
