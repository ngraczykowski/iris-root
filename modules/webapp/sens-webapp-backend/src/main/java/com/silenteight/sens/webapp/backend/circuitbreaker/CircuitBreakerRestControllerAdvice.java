package com.silenteight.sens.webapp.backend.circuitbreaker;

import com.silenteight.sens.webapp.common.rest.exception.AbstractErrorControllerAdvice;
import com.silenteight.sens.webapp.common.rest.exception.dto.ErrorDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
class CircuitBreakerRestControllerAdvice extends AbstractErrorControllerAdvice {

  @ExceptionHandler(InvalidBranchIdException.class)
  public ResponseEntity<ErrorDto> handle(InvalidBranchIdException e) {
    return handle(e, "Invalid Branch ID. branchId=" + e.getBranchId(), BAD_REQUEST);
  }
}
