package com.silenteight.warehouse.report.production;

import com.silenteight.warehouse.common.opendistro.kibana.KibanaReportEmptyDataException;
import com.silenteight.warehouse.common.opendistro.kibana.KibanaReportGenerationFailedException;
import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaClientException;
import com.silenteight.warehouse.common.web.exception.AbstractErrorControllerAdvice;
import com.silenteight.warehouse.common.web.exception.ErrorDto;
import com.silenteight.warehouse.report.reporting.ReportInstanceNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.util.Map.of;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestControllerAdvice(basePackageClasses = ProductionReportsRestController.class)
class ProductionReportsControllerAdvice extends AbstractErrorControllerAdvice {

  @ExceptionHandler(OpendistroKibanaClientException.class)
  public ResponseEntity<ErrorDto> handle(OpendistroKibanaClientException e) {
    return handle(e, "KibanaUpstreamServiceError", INTERNAL_SERVER_ERROR, of(
        "url", e.getUrl(),
        "errorCode", e.getStatusCode()));
  }

  @ExceptionHandler(ReportInstanceNotFoundException.class)
  public ResponseEntity<ErrorDto> handle(ReportInstanceNotFoundException e) {
    return handle(e, "Report not found", NOT_FOUND, e.toMap());
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
