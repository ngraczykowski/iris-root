package com.silenteight.warehouse.report.simulation;

import com.silenteight.warehouse.common.opendistro.kibana.KibanaReportEmptyDataException;
import com.silenteight.warehouse.common.opendistro.kibana.KibanaReportGenerationFailedException;
import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaClientException;
import com.silenteight.warehouse.common.web.exception.AbstractErrorControllerAdvice;
import com.silenteight.warehouse.common.web.exception.ErrorDto;
import com.silenteight.warehouse.indexer.analysis.AnalysisDoesNotExistException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.util.Map.of;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice(basePackageClasses = SimulationReportsRestController.class)
class SimulationReportsControllerAdvice extends AbstractErrorControllerAdvice {

  @ExceptionHandler(OpendistroKibanaClientException.class)
  public ResponseEntity<ErrorDto> handle(OpendistroKibanaClientException e) {
    return handle(e, "KibanaUpstreamServiceError", INTERNAL_SERVER_ERROR, of(
        "url", e.getUrl(),
        "errorCode", e.getStatusCode()));
  }

  @ExceptionHandler(AnalysisDoesNotExistException.class)
  public ResponseEntity<ErrorDto> handle(AnalysisDoesNotExistException e) {
    return handle(e, "AnalysisDoesNotExistsError", NOT_FOUND);
  }

  @ExceptionHandler(KibanaReportGenerationFailedException.class)
  public ResponseEntity<ErrorDto> handle(KibanaReportGenerationFailedException e) {
    return handle(e, "KibanaReportGenerationFailed", INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(KibanaReportEmptyDataException.class)
  public ResponseEntity<ErrorDto> handle(KibanaReportEmptyDataException e) {
    return handle(e, "KibanaReportEmptyData", NO_CONTENT);
  }
}
