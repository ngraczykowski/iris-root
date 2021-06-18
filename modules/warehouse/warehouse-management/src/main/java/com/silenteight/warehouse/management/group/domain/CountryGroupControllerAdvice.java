package com.silenteight.warehouse.management.group.domain;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.management.group.domain.exception.CountryGroupAlreadyExistsException;
import com.silenteight.warehouse.management.group.domain.exception.CountryGroupDoesNotExistException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@ControllerAdvice
class CountryGroupControllerAdvice {

  @ExceptionHandler(CountryGroupDoesNotExistException.class)
  public ResponseEntity<String> handle(CountryGroupDoesNotExistException e) {
    log(e);
    return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handle(IllegalArgumentException e) {
    log(e);
    return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
  }

  @ExceptionHandler(CountryGroupAlreadyExistsException.class)
  public ResponseEntity<String> handle(CountryGroupAlreadyExistsException e) {
    log(e);
    return new ResponseEntity<>(e.getMessage(), CONFLICT);
  }

  private static void log(Exception e) {
    log.error("Error occurred", e);
  }
}
