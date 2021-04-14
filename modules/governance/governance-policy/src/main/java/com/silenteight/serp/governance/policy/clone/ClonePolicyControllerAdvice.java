package com.silenteight.serp.governance.policy.clone;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.common.web.exception.ControllerAdviceOrder;
import com.silenteight.serp.governance.policy.domain.exception.WrongBasePolicyException;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Slf4j
@ControllerAdvice
@Order(ControllerAdviceOrder.GLOBAL)
class ClonePolicyControllerAdvice {

  @ExceptionHandler(WrongBasePolicyException.class)
  public ResponseEntity<String> handle(WrongBasePolicyException e) {
    log(e);
    return new ResponseEntity<>(e.getMessage(), UNPROCESSABLE_ENTITY);
  }

  private static void log(Exception e) {
    log.error("Error occurred", e);
  }
}
