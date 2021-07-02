package com.silenteight.warehouse.management.country;

import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClientException;
import com.silenteight.warehouse.common.web.exception.AbstractErrorControllerAdvice;
import com.silenteight.warehouse.common.web.exception.ErrorDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.util.Map.of;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
class CountriesControllerAdvice extends AbstractErrorControllerAdvice {

  @ExceptionHandler(OpendistroElasticClientException.class)
  public ResponseEntity<ErrorDto> handle(OpendistroElasticClientException e) {
    if (401 == e.getStatusCode())
      return handle(e, "EsUnauthorizedError", UNAUTHORIZED, of("url", e.getUrl()));

    if (403 == e.getStatusCode())
      return handle(e, "EsForbiddenError", FORBIDDEN, of("url", e.getUrl()));

    if (404 == e.getStatusCode())
      return handle(e, "EsNotFound", NOT_FOUND, of("url", e.getUrl()));

    return handle(e, "EsInternalServerError", INTERNAL_SERVER_ERROR, of("url", e.getUrl()));
  }
}
