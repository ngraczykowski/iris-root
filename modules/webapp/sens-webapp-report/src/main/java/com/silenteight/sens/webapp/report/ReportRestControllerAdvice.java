package com.silenteight.sens.webapp.report;

import com.silenteight.sens.webapp.common.rest.exception.AbstractErrorControllerAdvice;
import com.silenteight.sens.webapp.common.rest.exception.ControllerAdviceOrder;
import com.silenteight.sens.webapp.common.rest.exception.dto.ErrorDto;
import com.silenteight.sens.webapp.report.exception.IllegalParameterException;
import com.silenteight.sens.webapp.report.exception.ReportNotFoundException;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
@Order(ControllerAdviceOrder.REPORT)
class ReportRestControllerAdvice extends AbstractErrorControllerAdvice {

  @ExceptionHandler(ReportNotFoundException.class)
  public ResponseEntity<ErrorDto> handle(ReportNotFoundException e) {
    return handle(e, "ReportNotFoundException", HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(IllegalParameterException.class)
  public ResponseEntity<ErrorDto> handle(IllegalParameterException e) {
    return handle(
        e, "IllegalParameterException", HttpStatus.BAD_REQUEST, Map.of("message", e.getMessage()));
  }
}
