package com.silenteight.serp.governance.policy.step.edit;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.policy.domain.exception.WrongPolicyStateException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Slf4j
@ControllerAdvice
class SetPolicyStepsOrderControllerAdvice {

  @ExceptionHandler(WrongPolicyStateException.class)
  public ResponseEntity<String> handle(WrongPolicyStateException e) {
    log(e);
    return new ResponseEntity<>(e.getMessage(), UNPROCESSABLE_ENTITY);
  }

  private static void log(Exception e) {
    log.error("Error occurred", e);
  }
}
