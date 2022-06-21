package com.silenteight.sens.webapp.report;

import com.silenteight.sens.webapp.report.exception.IllegalParameterException;
import com.silenteight.sens.webapp.report.exception.ReportNotFoundException;
import com.silenteight.serp.governance.common.web.exception.AbstractErrorControllerAdvice;
import com.silenteight.serp.governance.common.web.exception.ControllerAdviceOrder;
import com.silenteight.serp.governance.common.web.exception.ErrorDto;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
@Order(ControllerAdviceOrder.REPORT)
class ReportRestControllerAdvice extends AbstractErrorControllerAdvice {

  @ExceptionHandler(ReportNotFoundException.class)
  public ResponseEntity<ErrorDto> handle(ReportNotFoundException e) {
    return handle(e, "ReportNotFound", NOT_FOUND);
  }

  @ExceptionHandler(IllegalParameterException.class)
  public ResponseEntity<ErrorDto> handle(IllegalParameterException e) {
    return handle(
        e, "IllegalParameter", BAD_REQUEST, Map.of("message", e.getMessage()));
  }
}
