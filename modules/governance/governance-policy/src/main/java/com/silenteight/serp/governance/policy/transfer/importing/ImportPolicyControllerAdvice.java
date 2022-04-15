package com.silenteight.serp.governance.policy.transfer.importing;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.policy.domain.exception.EmptyFeatureConfiguration;
import com.silenteight.serp.governance.policy.domain.exception.EmptyMatchConditionValueException;
import com.silenteight.serp.governance.policy.domain.exception.WrongToFulfillValue;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Slf4j
@ControllerAdvice
class ImportPolicyControllerAdvice {

  @ExceptionHandler(PolicyImportException.class)
  public ResponseEntity<String> handle(PolicyImportException e) {
    log(e);
    return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
  }

  @ExceptionHandler(WrongToFulfillValue.class)
  public ResponseEntity<String> handle(WrongToFulfillValue e) {
    log(e);
    return new ResponseEntity<>(e.getMessage(), UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(EmptyMatchConditionValueException.class)
  public ResponseEntity<String> handle(EmptyMatchConditionValueException e) {
    log(e);
    return new ResponseEntity<>(e.getMessage(), UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(EmptyFeatureConfiguration.class)
  public ResponseEntity<String> handle(EmptyFeatureConfiguration e) {
    log(e);
    return new ResponseEntity<>(e.getMessage(), UNPROCESSABLE_ENTITY);
  }

  private static void log(Exception e) {
    log.error("Error occurred", e);
  }
}
