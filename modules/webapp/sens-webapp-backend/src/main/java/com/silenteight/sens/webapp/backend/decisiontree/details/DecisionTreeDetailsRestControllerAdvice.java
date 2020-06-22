package com.silenteight.sens.webapp.backend.decisiontree.details;

import com.silenteight.sens.webapp.common.rest.exception.AbstractErrorControllerAdvice;
import com.silenteight.sens.webapp.common.rest.exception.ControllerAdviceOrder;
import com.silenteight.sens.webapp.common.rest.exception.dto.ErrorDto;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
@Order(ControllerAdviceOrder.DECISION_TREE)
class DecisionTreeDetailsRestControllerAdvice extends AbstractErrorControllerAdvice {

  @ExceptionHandler(DecisionTreeNotFound.class)
  public ResponseEntity<ErrorDto> handle(DecisionTreeNotFound e) {
    return handle(e, "Decision tree not found (id=" + e.getTreeId() + ").", NOT_FOUND);
  }
}
