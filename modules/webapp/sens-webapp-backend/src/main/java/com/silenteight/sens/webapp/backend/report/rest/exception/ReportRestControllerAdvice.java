package com.silenteight.sens.webapp.backend.report.rest.exception;

import com.silenteight.sens.webapp.backend.report.exception.ReportNotFoundException;
import com.silenteight.sens.webapp.common.rest.exception.AbstractErrorControllerAdvice;
import com.silenteight.sens.webapp.common.rest.exception.ControllerAdviceOrder;
import com.silenteight.sens.webapp.common.rest.exception.dto.ErrorDto;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(ControllerAdviceOrder.REPORT)
class ReportRestControllerAdvice extends AbstractErrorControllerAdvice {

  @ExceptionHandler(ReportNotFoundException.class)
  public ResponseEntity<ErrorDto> handle(ReportNotFoundException e) {
    return handle(e, "ReportNotFoundException", HttpStatus.NOT_FOUND);
  }
}
