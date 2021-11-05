package com.silenteight.warehouse.common.opendistro.elastic;

import com.silenteight.warehouse.common.opendistro.elastic.exception.OpendistroElasticClientException;
import com.silenteight.warehouse.common.web.exception.AbstractErrorControllerAdvice;
import com.silenteight.warehouse.common.web.exception.ControllerAdviceOrder;
import com.silenteight.warehouse.common.web.exception.ErrorDto;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.util.Map.of;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
@Order(ControllerAdviceOrder.GLOBAL)
class OpendistroElasticGlobalControllerAdvice extends AbstractErrorControllerAdvice {

  @ExceptionHandler(OpendistroElasticClientException.class)
  public ResponseEntity<ErrorDto> handle(OpendistroElasticClientException e) {
    return handle(e, "EsUpstreamServiceError", INTERNAL_SERVER_ERROR, of(
        "url", e.getUrl(),
        "errorCode", e.getStatusCode()));
  }
}
