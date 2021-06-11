package com.silenteight.serp.governance.qa.analysis.details;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.common.web.exception.ControllerAdviceOrder;
import com.silenteight.serp.governance.qa.domain.exception.WrongAlertIdException;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@ControllerAdvice
@Order(ControllerAdviceOrder.GLOBAL)
class GetAnalysisAlertDetailsControllerAdvice {

  @ExceptionHandler(WrongAlertIdException.class)
  public ResponseEntity<String> handle(WrongAlertIdException e) {
    log(e);
    return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
  }

  private static void log(Exception e) {
    log.error("Error occurred", e);
  }
}
