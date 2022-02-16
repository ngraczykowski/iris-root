package com.silenteight.warehouse.report.create;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.common.web.exception.ErrorDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@ControllerAdvice
class CreateReportControllerAdvice {

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ErrorDto> handle(IllegalStateException e) {
    log(e);
    return new ResponseEntity<>(new ErrorDto(e.getClass().getSimpleName(),
        Map.of("details", e.getMessage())), BAD_REQUEST);
  }

  @ExceptionHandler(ReportNotAvailableException.class)
  public ResponseEntity<ErrorDto> handle(ReportNotAvailableException e) {
    log.info(e.getMessage());
    return new ResponseEntity<>(new ErrorDto(e.getClass().getSimpleName(),
        Map.of("details", e.getMessage())), NOT_FOUND);
  }

  private static void log(Exception e) {
    log.error("Error occurred", e);
  }
}
