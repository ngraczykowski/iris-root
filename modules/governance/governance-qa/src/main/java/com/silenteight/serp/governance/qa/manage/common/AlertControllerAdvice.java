package com.silenteight.serp.governance.qa.manage.common;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.common.web.exception.ControllerAdviceOrder;
import com.silenteight.serp.governance.qa.manage.analysis.details.DecisionAlreadyExistsException;
import com.silenteight.serp.governance.qa.manage.domain.exception.AlertAlreadyProcessedException;
import com.silenteight.serp.governance.qa.manage.domain.exception.WrongAlertIdException;
import com.silenteight.serp.governance.qa.manage.domain.exception.WrongAlertNameException;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.format.DateTimeParseException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@ControllerAdvice
@Order(ControllerAdviceOrder.GLOBAL)
public class AlertControllerAdvice {

  @ExceptionHandler(WrongAlertIdException.class)
  public ResponseEntity<String> handleWrongAlertIdException(WrongAlertIdException e) {
    log(e);
    return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
  }

  @ExceptionHandler(WrongAlertNameException.class)
  public ResponseEntity<String> handleWrongAlertNameException(WrongAlertNameException e) {
    log(e);
    return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
  }

  @ExceptionHandler(AlertAlreadyProcessedException.class)
  public ResponseEntity<String> handleAlertAlreadyProcessedException(
      AlertAlreadyProcessedException e) {

    log(e);
    return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
  }

  @ExceptionHandler(DecisionAlreadyExistsException.class)
  public ResponseEntity<String> handleDecisionAlreadyExistsException(
      DecisionAlreadyExistsException e) {

    log(e);
    return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
  }

  @ExceptionHandler(DateTimeParseException.class)
  public ResponseEntity<String> handleDateTimeParseException(DateTimeParseException e) {
    log(e);
    return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
  }

  private static void log(Exception e) {
    log.error("Error occurred", e);
  }
}
