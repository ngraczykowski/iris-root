package com.silenteight.sens.webapp.backend.decisiontree.rest.exception;

import com.silenteight.sens.webapp.backend.decisiontree.exception.DecisionTreeNotFoundException;
import com.silenteight.sens.webapp.common.rest.exception.AbstractErrorControllerAdvice;
import com.silenteight.sens.webapp.common.rest.exception.ControllerAdviceOrder;
import com.silenteight.sens.webapp.common.rest.exception.dto.ErrorDto;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(ControllerAdviceOrder.DECISION_TREE)
public class DecisionTreeControllerAdvice extends AbstractErrorControllerAdvice {

  @ExceptionHandler(DecisionTreeNotFoundException.class)
  public ResponseEntity<ErrorDto> handle(DecisionTreeNotFoundException e) {
    return handle(e, "DecisionTreeNotFoundException", HttpStatus.NOT_FOUND);
  }
}
