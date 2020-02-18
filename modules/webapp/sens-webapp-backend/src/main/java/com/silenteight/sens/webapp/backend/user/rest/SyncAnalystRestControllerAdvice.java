package com.silenteight.sens.webapp.backend.user.rest;

import com.silenteight.sens.webapp.backend.user.rest.SyncAnalystRestController.SyncAnalystNotAvailableException;
import com.silenteight.sens.webapp.common.rest.exception.AbstractErrorControllerAdvice;
import com.silenteight.sens.webapp.common.rest.exception.ControllerAdviceOrder;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.ResponseEntity.status;

@ControllerAdvice
@Order(ControllerAdviceOrder.SYNC_ANALYST)
public class SyncAnalystRestControllerAdvice extends AbstractErrorControllerAdvice {

  @ExceptionHandler(SyncAnalystNotAvailableException.class)
  public ResponseEntity<String> handle(SyncAnalystNotAvailableException e) {
    return status(SERVICE_UNAVAILABLE).body(e.getMessage());
  }
}
