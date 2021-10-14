package com.silenteight.warehouse.report.metrics.create;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.metrics.domain.exception.ReportNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@ControllerAdvice
class CreateMetricsReportControllerAdvice {

  @ExceptionHandler(ReportNotFoundException.class)
  public ResponseEntity<String> handle(ReportNotFoundException e) {
    log(e);
    return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
  }

  private static void log(Exception e) {
    log.error("Error occurred", e);
  }
}
