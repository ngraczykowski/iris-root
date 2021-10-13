package com.silenteight.warehouse.report.simulation;

import com.silenteight.warehouse.common.web.exception.AbstractErrorControllerAdvice;
import com.silenteight.warehouse.common.web.exception.ErrorDto;
import com.silenteight.warehouse.indexer.simulation.analysis.AnalysisDoesNotExistException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice(basePackageClasses = DeprecatedSimulationReportsRestController.class)
class DeprecatedSimulationReportsControllerAdvice extends AbstractErrorControllerAdvice {

  @ExceptionHandler(AnalysisDoesNotExistException.class)
  public ResponseEntity<ErrorDto> handle(AnalysisDoesNotExistException e) {
    return handle(e, "AnalysisDoesNotExistsError", NOT_FOUND);
  }
}
