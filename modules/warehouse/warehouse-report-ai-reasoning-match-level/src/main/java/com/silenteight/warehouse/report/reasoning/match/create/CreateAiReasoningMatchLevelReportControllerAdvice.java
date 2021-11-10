package com.silenteight.warehouse.report.reasoning.match.create;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.reporting.exception.InvalidDateFromParameterException;
import com.silenteight.warehouse.report.reporting.exception.InvalidDateRangeParametersOrderException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@ControllerAdvice
class CreateAiReasoningMatchLevelReportControllerAdvice {

  @ExceptionHandler(InvalidDateRangeParametersOrderException.class)
  public ResponseEntity<String> handle(InvalidDateRangeParametersOrderException e) {
    log(e);
    return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
  }

  @ExceptionHandler(InvalidDateFromParameterException.class)
  public ResponseEntity<String> handle(InvalidDateFromParameterException e) {
    log(e);
    return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
  }

  private static void log(Exception e) {
    log.error("Error occurred", e);
  }
}
