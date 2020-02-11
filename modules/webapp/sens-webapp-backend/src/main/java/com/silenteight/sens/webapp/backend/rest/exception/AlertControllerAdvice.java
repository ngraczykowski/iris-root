package com.silenteight.sens.webapp.backend.rest.exception;

import com.silenteight.sens.webapp.backend.presentation.exception.AlertRestrictedException;
import com.silenteight.sens.webapp.common.rest.exception.AbstractErrorControllerAdvice;
import com.silenteight.sens.webapp.common.rest.exception.ControllerAdviceOrder;
import com.silenteight.sens.webapp.common.rest.exception.dto.ErrorDto;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(ControllerAdviceOrder.RESTRICTION)
public class AlertControllerAdvice extends AbstractErrorControllerAdvice {

  @ExceptionHandler(AlertRestrictedException.class)
  public ResponseEntity<ErrorDto> handle(AlertRestrictedException e) {
    return handle(e, HttpStatus.FORBIDDEN);
  }
}
