package com.silenteight.warehouse.report.billing.create;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.billing.domain.exception.ReportNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

@Slf4j
@ControllerAdvice
class CreateBillingReportControllerAdvice {

  @ExceptionHandler(ReportNotFoundException.class)
  public ResponseEntity<String> handle(ReportNotFoundException e) {
    log(e);
    return new ResponseEntity<>(e.getMessage(), NOT_ACCEPTABLE);
  }

  private static void log(Exception e) {
    log.error("Error occurred", e);
  }
}
