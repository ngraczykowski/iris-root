package com.silenteight.serp.governance.qa.manage.analysis.view;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.common.web.exception.ControllerAdviceOrder;
import com.silenteight.serp.governance.qa.manage.domain.exception.AlertAlreadyProcessedException;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.CONFLICT;

@Slf4j
@ControllerAdvice
@Order(ControllerAdviceOrder.GLOBAL)
class ViewAlertAnalysisControllerAdvice {

  @ExceptionHandler(AlertAlreadyProcessedException.class)
  public ResponseEntity<String> handle(AlertAlreadyProcessedException e) {
    log(e);
    return new ResponseEntity<>(e.getMessage(), CONFLICT);
  }

  private static void log(Exception e) {
    log.error("Error occurred", e);
  }
}
