package com.silenteight.simulator.management.list;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.simulator.management.domain.InvalidModelNameException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@ControllerAdvice
public class ListSimulationControllerAdvice {

  @ExceptionHandler(InvalidModelNameException.class)
  public ResponseEntity<String> handle(InvalidModelNameException e) {
    log(e);
    return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
  }

  private static void log(Exception e) {
    log.error("Error occurred", e);
  }
}
