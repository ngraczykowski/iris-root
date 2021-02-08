package com.silenteight.serp.governance.policy.step.edit;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.policy.domain.exception.StepsOrderListsSizeMismatch;
import com.silenteight.serp.governance.policy.domain.exception.WrongIdsListInSetStepsOrder;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Slf4j
@ControllerAdvice
class EditStepControllerAdvice {

  @ExceptionHandler(StepsOrderListsSizeMismatch.class)
  public ResponseEntity<String> handle(StepsOrderListsSizeMismatch e) {
    log(e);
    return new ResponseEntity<>(e.getMessage(), UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(WrongIdsListInSetStepsOrder.class)
  public ResponseEntity<String> handle(WrongIdsListInSetStepsOrder e) {
    log(e);
    return new ResponseEntity<>(e.getMessage(), UNPROCESSABLE_ENTITY);
  }

  private static void log(Exception e) {
    log.error("Error occurred", e);
  }
}
