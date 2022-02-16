package com.silenteight.warehouse.report.status;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.common.web.exception.ErrorDto;
import com.silenteight.warehouse.report.persistence.ReportNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

@Slf4j
@ControllerAdvice
class StatusReportControllerAdvice {

  @ExceptionHandler(ReportNotFoundException.class)
  public ResponseEntity<ErrorDto> handle(ReportNotFoundException e) {
    log(e);
    return new ResponseEntity<>(new ErrorDto(e.getClass().getSimpleName(),
        Map.of("details", e.getMessage())), NOT_ACCEPTABLE);
  }

  private static void log(Exception e) {
    log.error("Error occurred", e);
  }
}
