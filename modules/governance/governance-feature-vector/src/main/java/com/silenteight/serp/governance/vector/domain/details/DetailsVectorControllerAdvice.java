package com.silenteight.serp.governance.vector.domain.details;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.exception.EntityNotFoundException;
import com.silenteight.serp.governance.common.web.exception.ControllerAdviceOrder;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@ControllerAdvice
@Order(ControllerAdviceOrder.GLOBAL)
public class DetailsVectorControllerAdvice {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
    log.error("Error occurred", e);
    return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
  }
}
