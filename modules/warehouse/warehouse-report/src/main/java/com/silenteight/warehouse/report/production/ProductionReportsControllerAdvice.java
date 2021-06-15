package com.silenteight.warehouse.report.production;

import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaClientException;
import com.silenteight.warehouse.common.web.exception.AbstractErrorControllerAdvice;
import com.silenteight.warehouse.common.web.exception.ErrorDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.util.Map.of;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice(basePackageClasses = ProductionReportsRestController.class)
class ProductionReportsControllerAdvice extends AbstractErrorControllerAdvice {

  @ExceptionHandler(OpendistroKibanaClientException.class)
  public ResponseEntity<ErrorDto> handle(OpendistroKibanaClientException e) {
    if (401 == e.getStatusCode()) {
      return handle(e, "KibanaUnauthorizedError", UNAUTHORIZED, of("url", e.getUrl()));
    }

    if (403 == e.getStatusCode()) {
      return handle(e, "KibanaForbiddenError", FORBIDDEN, of("url", e.getUrl()));
    }

    if (500 == e.getStatusCode()) {
      return handle(e, "KibanaInternalServerError", INTERNAL_SERVER_ERROR, of("url", e.getUrl()));
    }

    throw e;
  }
}
