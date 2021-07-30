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
    if (401 == e.getStatusCode())
      return handle(e, "KibanaUnauthorizedError", UNAUTHORIZED, of("url", e.getUrl()));

    if (403 == e.getStatusCode())
      return handle(e, "KibanaForbiddenError", FORBIDDEN, of("url", e.getUrl()));

    return handle(e, "KibanaInternalServerError", INTERNAL_SERVER_ERROR, of("url", e.getUrl()));
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
