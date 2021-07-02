package com.silenteight.warehouse.report.production;

import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaClientException;
import com.silenteight.warehouse.common.web.exception.AbstractErrorControllerAdvice;
import com.silenteight.warehouse.common.web.exception.ErrorDto;
import com.silenteight.warehouse.report.reporting.ReportInstanceNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.util.Map.of;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice(basePackageClasses = ProductionReportsRestController.class)
class ProductionReportsControllerAdvice extends AbstractErrorControllerAdvice {

  @ExceptionHandler(OpendistroKibanaClientException.class)
  public ResponseEntity<ErrorDto> handle(OpendistroKibanaClientException e) {
    if (401 == e.getStatusCode())
      return handle(e, "KibanaUnauthorizedError", UNAUTHORIZED, of("url", e.getUrl()));

    if (403 == e.getStatusCode())
      return handle(e, "KibanaForbiddenError", FORBIDDEN, of("url", e.getUrl()));

    return handle(e, "KibanaInternalServerError", INTERNAL_SERVER_ERROR, of("url", e.getUrl()));
  }

  @ExceptionHandler(ReportInstanceNotFoundException.class)
  public ResponseEntity<ErrorDto> handle(ReportInstanceNotFoundException e) {
    return handle(e, "Report not found", NOT_FOUND,e.toMap());
  }
}
